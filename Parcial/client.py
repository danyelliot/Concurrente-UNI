import socket
import threading

host = "localhost"
port = 222

try:
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((host, port))
    print(f"Client has connected to server on port {port}")

    def receive_messages():
        while True:
            message = client.recv(1024).decode()
            if message == "EXIT":
                print("Server has closed the connection. Exiting...")
                break
            print(f"Server says: {message}")

    receive_thread = threading.Thread(target=receive_messages)
    receive_thread.start()

    while True:
        message = input("Enter a message to send to the server: ")
        client.sendall((message + "\n").encode())

except Exception as e:
    raise RuntimeError(str(e))