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
public class ResponseHandler {
    
    
    public static final String httpHeader = "HTTP/1.1"; 
    public static final String endl = "\r\n";
    
    public static void respond(Socket client,String errcode)
    {
        String responseText = httpHeader + " " + errcode + endl+endl;
        
        try{
            OutputStream outstream = client.getOutputStream();
            outstream.write(responseText.getBytes("ASCII"));
        }catch(Exception e){
            e.printStackTrace();
        
        }finally{
            try{
                System.out.println("Closing connection: " + client.getRemoteSocketAddress().toString());
                client.close();
            }catch(IOException e){
            }
        }
    }
    
    public static void respond(Socket client,String uri, boolean excludeBody, WebServer ws)
    {
        String statusCode= "200 OK";
        
        Resource res;
        try{
            res = Resource.getResource(uri,ws);
        }catch(MalformedURLException e){
            respond(client,"400 Bad Request");
            return;
        }catch(FileNotFoundException e){
            respond(client,"404 Not Found");
            return;
        }catch(SecurityException e){
            respond(client,"403 Forbidden");
            return;
        }
        long size = res.getContentLength();
        String type = res.getContentType();
        
        String responseHeader = httpHeader + " " + statusCode + endl+
                "Server: " + ws.serverName + endl+
                "Content-Length: " + size +endl +
                ((type != null)?("Content-Type: " + type + endl):("")) +
                endl;
        
        try{
            
            OutputStream outstream = client.getOutputStream();
            outstream.write(responseHeader.getBytes("ASCII"));
            
            if(!excludeBody){
                InputStream resstream  = res.getInputStream();



                byte [] buf = new byte[1000];
                while(size > 0){
                    int l = resstream.read(buf);
                    if (l<=0) break;


                    outstream.write(buf,0,(int)(((long)l<size)?(long)l:size));
                    size-=l;
                }
            }
        }catch(Exception e){
            
        }
        finally{
            try{
                System.out.println("Closing connection: " + client.getRemoteSocketAddress().toString());
                client.close();
                res.getInputStream().close();
            }catch(IOException e){
            }
        }
    }
    
}
