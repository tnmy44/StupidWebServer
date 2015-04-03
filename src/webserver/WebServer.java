package webserver;

import java.io.*;
import java.net.*;

/**
 *
 * @author tanmay
 */
public class WebServer extends Thread {
    
    public String rootDir;
    private int port;
    public int status=0;       // 0 means stopped, 1 means running.
    public static String serverName = "TheStupidServer";
    
    
    public WebServer(int port, String rootDir){
        this.port=port;
        this.rootDir = rootDir;
    }
    
    public void run(){
        
        System.out.println("Starting Stupid Web Server at port " + port + " and root Directory '" + rootDir + "'");
        ServerSocket listener;
        
        try{
            listener = new ServerSocket(port);
            listener.setSoTimeout(2000);
            
        }catch(IOException e){
            System.out.println("Fatal Error:\n" + e.getMessage() + "\n\nExiting.");
            return;
        }
        
        
        status=1;
        
        while(true){
            Socket client;
            try{
                 client = listener.accept();
                 System.out.println("Client connected: " + client.getRemoteSocketAddress());
            }catch(IOException err){
                if (status==1)
                   continue;
                else
                    break;
            }
            new RequestHandler(client,this).start();
        }
        
        System.out.println("Exiting WebServer");
    }
    
    public static void main(String[] args) {
        int port = 8080;
        String rootDir = System.getProperty("user.dir");
        if (rootDir.endsWith("/")) rootDir = rootDir.substring(0, rootDir.length()-1);
        WebServer ws;
        if(args.length==0){
            ws = new WebServer(port,rootDir);
            ws.start();
        }
        else                 
        {
            try{
                port = Integer.parseInt(args[0]);
                rootDir = args[1];
                if (rootDir.endsWith("/")) rootDir = rootDir.substring(0, rootDir.length()-1);
                
            }catch(NumberFormatException e){        
                printHelp("");
                return;
            }catch(ArrayIndexOutOfBoundsException e)
            {}
            ws = new WebServer(port,rootDir);
            ws.start();
        }
        
               
    }
    
    public static void printHelp(String text){
        System.out.print(text);
        System.out.print("Usage:\n\nWebServer [Port [RootDir]]\n\nDefault values for port " + 
                "and root dir are 8080 and current working directory respectively.");
    }
}
