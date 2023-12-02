const http = require('http');
const { argv } = require('process');
const fs = require('fs');
const path = require('path');

function getFileContent(filename, res) {
    let filePath = path.join(__dirname, 'public', filename);
    const extname = path.extname(filePath);
    let contentType = 'text/html';
    switch (extname) {
        case '.js':
            contentType = 'text/javascript';
            break;
        case '.css':
            contentType = 'text/css';
            break;
        case '.json':
            contentType = 'application/json';
            break;
        case '.png':
            contentType = 'image/png';
            break;
        case '.jpg':
            contentType = 'image/jpg';
            break;
    }

    fs.readFile(filePath, (err, content) => {
        if (err) {
            if (err.code === 'ENOENT') {
                res.writeHead(404, { 'Content-Type': 'text/html' });
                res.end('404 - Archivo no encontrado');
            } else {
                res.writeHead(500);
                res.end(`Error del servidor: ${err.code}`);
            }
        } else {
            res.writeHead(200, { 'Content-Type': contentType });
            res.end(content, 'utf8');
        }
    });
}

const server = http.createServer((req, res) => {
    if (req.url === '/') {
        getFileContent('index.html', res);
        return;
    }
    if (req.url.startsWith('/public')) {
        const filename = req.url.split('/')[2];
        getFileContent(filename, res);
        return;
    }
    if (req.url.startsWith('/almacen')) {
        const params = req.url.split('/');
        const id = params[2];
        if (!isNaN(id)) {
            if (req.method === 'GET') {
                console.log('GET for id ' + id);
                res.end();
            }
            if (req.method === 'PATCH') {
                console.log('PATCH for id ' + id);
                res.end();
            }
            if (req.method === 'DELETE') {
                console.log('DELETE for id ' + id);
                res.end();
            }
            return;
        }
        if (req.method === 'GET') {
            console.log('GET all');
            res.end();
        }
        if (req.method === 'POST') {
            console.log('POST new');
            res.end();
        }
    }
});

port = argv[2] || 3000;

server.listen(port, () => {
    console.log(`Server running on port ${port}`);
});