package org.example;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Ventas {
    private static final String RPC_QUEUE_NAME = "go-java-queue";
    private static String process_consulta(String msg) throws SQLException, IOException, TimeoutException, ExecutionException, InterruptedException {
        boolean id_no_unico = true;
        Random rand = new Random();
        int id_fact = 0;
        while (id_no_unico){
            // Genera id random de factura
            id_fact=rand.nextInt(5000)+1;
            id_no_unico = ConsultaSql.consultar_id_repetido(id_fact);
        }
        // Generando fecha random
        RandomDate rd = new RandomDate(LocalDate.of(1990, 1, 1), LocalDate.of(2020, 1, 1));
        LocalDate fecha = rd.nextDate();

        String[] msg_sep = msg.split(" ",5);
        String msg2almacen = "modc "+msg_sep[3]+" "+msg_sep[4];

        RPCClient ventasCli = new RPCClient();
        String response=ventasCli.call(msg2almacen);

        String[] resp_sep = response.split(" ",5);
        // Calculando importe
        float importe = Integer.parseInt(msg_sep[4])*Float.parseFloat(resp_sep[4]);
        try{
            ConsultaSql.anadirElemento(new DatoSql(id_fact,msg_sep[0],msg_sep[1],msg_sep[2],
                                        Integer.parseInt(msg_sep[3]),resp_sep[1],resp_sep[2],
                                        Integer.parseInt(msg_sep[4]),fecha,importe));
            //ConsultaSql.consultar();
            return "Se ha agregado la factura correctamente";
        }catch(Exception e){
            return "No se pudo generar la factura";
        }
    }

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("venta_host");
        factory.setUsername("guest");
        factory.setPassword("guest");


        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        channel.queuePurge(RPC_QUEUE_NAME);

        channel.basicQos(1);

        System.out.println(" [x] Awaiting RPC requests");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

            String response = "";
            try {
                String message = new String(delivery.getBody(), "UTF-8");

                System.out.println(" [.] Recibido " + message);
                response += process_consulta(message);
            } catch (RuntimeException | SQLException | TimeoutException | ExecutionException | InterruptedException e) {
                System.out.println(" [.] " + e);
            } finally {
                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> {}));
    }
}
