package main

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"strconv"
	"strings"
	"time"

	_ "github.com/go-sql-driver/mysql"
	amqp "github.com/rabbitmq/amqp091-go"
)

type Prod struct {
	Id       int
	Name     string
	Category string
	Amount   int
	Cost     float64
}

func sendProducts(db *sql.DB) string {
	consultaF := "SELECT ID_PROD, NAME_PROD, COST FROM products"
	rows, err := db.Query(consultaF)
	if err != nil {
		log.Fatal(err)
	}
	defer rows.Close()

	var id int
	var name string
	var cost float64

	var msg string
	for rows.Next() {
		err := rows.Scan(&id, &name, &cost)
		if err != nil {
			log.Fatal(err)
		}
		msg += strconv.Itoa(id) + "," + name + "," + strconv.FormatFloat(cost, 'f', -1, 64) + ";"
	}
	err = rows.Err()
	if err != nil {
		log.Fatal(err)
	}
	return msg
}

func checkExistence(db *sql.DB, id int, amount int) bool {
	consultaF := "SELECT AMOUNT FROM products WHERE ID_PROD = ?"
	rows, err := db.Query(consultaF, id)
	if err != nil {
		log.Fatal(err)
	}
	defer rows.Close()

	var amountDB int
	for rows.Next() {
		err := rows.Scan(&amountDB)
		if err != nil {
			log.Fatal(err)
		}
	}
	err = rows.Err()
	if err != nil {
		log.Fatal(err)
	}
	return amountDB >= amount
}

func checkAllExistences(db *sql.DB, values []string) bool {
	counter := 0
	for i := 0; i < len(values); i++ {
		prod := strings.Split(values[i], ",")
		id, _ := strconv.Atoi(prod[0])
		amount, _ := strconv.Atoi(prod[1])
		if checkExistence(db, id, amount) {
			counter++
		}
	}
	return counter == len(values)
}

func updateProducts(values []string, db *sql.DB) {
	for i := 0; i < len(values); i++ {
		prod := strings.Split(values[i], ",")
		id, _ := strconv.Atoi(prod[0])
		amount, _ := strconv.Atoi(prod[1])
		consultaF := "UPDATE products SET AMOUNT = AMOUNT - ? WHERE ID_PROD = ?"
		_, err := db.Exec(consultaF, amount, id)
		if err != nil {
			log.Fatal(err)
		}
	}
}

func failOnError(err error, msg string) {
	if err != nil {
		log.Panicf("%s: %s", msg, err)
	}
}

func arrayToString(arr []string) string {
	str := strings.Trim(strings.Join(strings.Fields(fmt.Sprint(arr)), ", "), "[]")
	return str
}

func main() {
	db, err := sql.Open("mysql", "root:2206@tcp(127.0.0.1:3306)/storageDB")
	defer db.Close()

	if err != nil {
		log.Fatal(err)
	} else {
		log.Printf("Conectado a la bd")
	}

	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/venta_host")
	failOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	q, err := ch.QueueDeclare(
		"go-java-queue",
		false,
		false,
		false,
		false,
		nil,
	)
	failOnError(err, "Failed to declare a queue")
	err = ch.Qos(
		1,
		0,
		false,
	)
	failOnError(err, "Failed to set QoS")

	msgs, err := ch.Consume(
		q.Name,
		"",
		false,
		false,
		false,
		false,
		nil,
	)
	failOnError(err, "Failed to register a consumer")

	var forever chan struct{}
	go func() {
		ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		for d := range msgs {
			msg := string(d.Body)
			var response = ""
			if msg == "get_products" {
				response = sendProducts(db)
			} else {
				log.Printf(" [.] Recibido: %v", msg)
				resp := strings.Split(msg, ";")
				/*user := resp[0]
				ruc := resp[1]*/
				values := strings.Split(resp[2], "/")
				if checkAllExistences(db, values) {
					response = "Venta realizada"
					updateProducts(values, db)
				} else {
					response = "No se pudo realizar la venta"
				}
			}
			err = ch.PublishWithContext(ctx,
				"",
				d.ReplyTo,
				false,
				false,
				amqp.Publishing{
					ContentType:   "text/plain",
					CorrelationId: d.CorrelationId,
					Body:          []byte(response),
				})
			failOnError(err, "Failed to publish a message")

			d.Ack(false)
		}
	}()

	log.Printf(" [*] Awaiting RPC requests")
	<-forever
}
