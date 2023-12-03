from http.server import HTTPServer, SimpleHTTPRequestHandler
import sys

def get_file_content(file_path):
    try:
        file_to_open = open(file_path).read()
        return file_to_open, 200
    except:
        return "Archivo no encontrado", 404

class Servidor(SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory='./public', **kwargs)

    def do_GET(self):
        if self.path == "/":
            self.path = "/index.html"
            file_to_open, status_code = get_file_content('.'+self.path)
            self.send_response(status_code)
            self.end_headers()
            self.wfile.write(bytes(file_to_open, 'utf-8'))
        if self.path.startswith("/ventas"):
            file_to_open, status_code = get_file_content('.'+self.path)
            self.send_response(status_code)
            self.end_headers()
            self.wfile.write(bytes(file_to_open, 'utf-8'))
    
if __name__ == "__main__":
    port = int(sys.argv[1]) if len(sys.argv) > 1 else 8080
    print("Servidor iniciado en http://localhost:"+str(port))
    server = HTTPServer(('localhost', port), Servidor)
    server.serve_forever()

