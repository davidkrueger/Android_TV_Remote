#opyright Jon Berg , turtlemeat.com

import string,cgi,time
import os
from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import re
from cgi import parse_qs, escape
import urllib
import traceback

import voice_commands


class MyHandler(BaseHTTPRequestHandler):
    
    def do_GET(self):
        try:
                print self.path
                query = self.path[1:]
                voice_commands.launch(query)
                self.send_response(200)
                self.send_header('Content-type',    'text/html')
                self.end_headers()
                
        except Exception:
        #    print Exception.message
        #    #print str(Error.message)
            self.send_error(404,'File Not Found: %s' % self.path)
        #    #traceback.print_stack()
     

    def do_POST(self):
        global rootnode
        print "post"
        try:
            ctype, pdict = cgi.parse_header(self.headers.getheader('content-type'))
            if ctype == 'multipart/form-data':
                query=cgi.parse_multipart(self.rfile, pdict)
            self.send_response(301)
            
            self.end_headers()
            upfilecontent = query.get('upfile')
            print "filecontent", upfilecontent[0]
            self.wfile.write("<HTML>POST OK.<BR><BR>");
            self.wfile.write(upfilecontent[0]);
            
        except :
            pass

def main():
    try:
        server = HTTPServer(('', 80), MyHandler)
        print 'started httpserver...'
        server.serve_forever()
    except KeyboardInterrupt:
        print '^C received, shutting down server'
        server.socket.close()

if __name__ == '__main__':
    main()


