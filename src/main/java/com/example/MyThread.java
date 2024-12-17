package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyThread extends Thread{
    private Socket s;
    public MyThread(Socket s) throws IOException{
        this.s = s;
    }

    public void run(){
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String firstLine = in.readLine();
            System.out.println(firstLine);
            String[] request = firstLine.split(" ");

            String method = request[0];
            String resource = request[1];
            String version = request[2];
            String header;

            do {
                header = in.readLine();
                System.out.println(header);
            } while (!header.isEmpty());

            System.out.println("Richiesta terminata");

            if(resource.equals("/")){
                resource = "/index.html";
            }

            if(resource.endsWith("/")){
                resource = resource + "index.html";
            }

            File file = new File("htdocs/PROGETTO-FINALE-FABIANI" + resource);
            
            if(file.isDirectory()){
                out.writeBytes("HTTP/1.1 301 Moved Permanently\n");
                out.writeBytes("Content-Length: 0" + "\n");
                out.writeBytes("Location: " + resource +"/\n");
                out.writeBytes("\n");
            } else if (file.exists()) {
                InputStream input = new FileInputStream(file);
                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Type: " + this.getContentType(resource) + "\n");
                out.writeBytes("Content-Length: " + file.length() + "\n");
                out.writeBytes("\n");
                
                byte[] buf = new byte[8192];
                int n;

                while ((n = input.read(buf)) != -1) {
                    out.write(buf, 0, n);
                }
                input.close();
            } else{
                out.writeBytes("HTTP/1.1 404 Not found\n");
                    out.writeBytes("Content-Length: 0" + "\n");
                    out.writeBytes("\n");
            }

            
            s.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public String getContentType(String resource){
        String[] arr = resource.split("\\.");
        String extention = arr[arr.length - 1];
        switch (extention) {
            case "htm":
                
            case "html":
                return "text/html";

            case "txt":
                return "text/plain";

            case "png":
                return "image/png";

            case "css":
                return "text/css";

            default:
                return "";
        }
    }
    }