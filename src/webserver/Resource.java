/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;
import java.io.*;

/**
 *
 * @author tanmay
 */
public class Resource {
    
    private FileInputStream ifstream;
    private long size;
    private String type;
    
    public String getContentType(){
        return("text/html");                                                                                                                                                                                                                                                                                                                                            
    }
    public InputStream getInputStream(){                                                                                                                                                                                                                                                                                                                                                                            
        return ifstream;
    }
    
    public long getContentLength(){
        return size;
    }
    
    private Resource()
    {
        
    }
    
    public static Resource getResource(String uri, WebServer ws) throws java.net.MalformedURLException, java.io.FileNotFoundException
    {
        if(uri.indexOf("//") != -1 )
        {
            if (uri.startsWith("http://")){
                uri = uri.substring(uri.indexOf("/", 7) + 1);
            }
            else
                throw new java.net.MalformedURLException("Not a valid http URL.");
        }
        else{
            
            if (uri.startsWith("/"))
                uri = uri.substring(1);
        }
        //System.out.println(ws.rootDir + "/" + uri);
        
        Resource res = new Resource();
        File f = new File(ws.rootDir + "/" + uri);
        if (!(f.exists()))
            throw new java.io.FileNotFoundException();
        res.size = f.length();
        
        FileInputStream filestream = new FileInputStream(f);
        
        res.ifstream = filestream;
        return res;
    }
}
