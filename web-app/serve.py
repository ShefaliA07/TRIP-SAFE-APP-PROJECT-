#!/usr/bin/env python3
"""
TripSafe Web Application Local Server
Serves the TripSafe interactive frontend web application.
"""
import http.server
import socketserver
import os
import sys
import webbrowser

PORT = 3000
DIRECTORY = os.path.dirname(os.path.abspath(__file__))

class Handler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

def run():
    os.chdir(DIRECTORY)
    socketserver.TCPServer.allow_reuse_address = True
    port = PORT
    for candidate_port in [3000, 3001, 8000, 8080, 5000]:
        try:
            httpd = socketserver.TCPServer(("0.0.0.0", candidate_port), Handler)
            port = candidate_port
            break
        except OSError:
            continue
    url = f"http://localhost:{port}"
    print("=" * 60)
    print(f"  TripSafe Web Application Running!")
    print(f"  Access URL: {url}")
    print("=" * 60)
    sys.stdout.flush()
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print("\nShutting down server.")
        httpd.server_close()

if __name__ == '__main__':
    run()
