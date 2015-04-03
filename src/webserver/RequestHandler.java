/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;
import java.io.*;
import java.net.*;
/**
 *
 * @author tanmay
 */
public class RequestHandler extends Thread{
    Socket client;
    WebServer webServer;
    
    public RequestHandler(Socket client, WebServer webServer){
        this.client=client;
        this.webServer = webServer;
    }
    
    
    public void run(){
        InputStream instream;
        byte [] b = new byte[3000];
        int length;
        try{
            instream = client.getInputStream();
            length = instream.read(b);
        }catch(IOException e){
            e.printStackTrace();
            return;
        }
        
        
        int linebreak=0;
        while(linebreak<length && b[linebreak]!=13 && b[linebreak]!=10) linebreak++;
        if (linebreak==length){
            ResponseHandler.respond(client,"431 Request Header Fields Too Large");
            return;
        }
        String firstLine = new String(b,0,linebreak);
        String fields[] = firstLine.split(" ");
        if (fields.length != 3){
            ResponseHandler.respond(client,"400 Bad Reqest");
            return;
        }
    
    
        boolean excludeBody;
        if (fields[0].equals("GET"))
            excludeBody=false;
        else if(fields[0].equals("HEAD"))
            excludeBody=true;
        else
        {
            ResponseHandler.respond(client,"501 Not Implemented");
            return;
        }
        
        String uri = fields[1];
        if (!(fields[2].equals("HTTP/1.1")))
        {
            ResponseHandler.respond(client,"505 HTTP Version Not Supported");
            return;
        
        }
        ResponseHandler.respond(client,uri,excludeBody, webServer);
    }
}
