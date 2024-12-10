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
            String responseBody;
            switch (resource) {
                case "/":

                case "/index.html":
                    File file = new File("htdocs/index.html");
                    InputStream input = new FileInputStream(file);
                    

                    out.writeBytes("HTTP/1.1 200 OK\n");
                    out.writeBytes("Content-Type: text/html\n");
                    out.writeBytes("Content-Length: " + file.length() + "\n");
                    out.writeBytes("\n");
                    
                    byte[] buf = new byte[8192];
                    int n;

                    while ((n = input.read(buf)) != -1) {
                        out.write(buf, 0, n);
                    }
                    input.close();
                    break;

                case "/file.txt":
                    File file2 = new File("htdocs/file.txt");
                    InputStream input2 = new FileInputStream(file2);
                    out.writeBytes("HTTP/1.1 200 OK\n");
                    out.writeBytes("Content-Type: text/plain\n");
                    out.writeBytes("Content-Length: " + file2.length() + "\n");
                    out.writeBytes("\n");
                    byte[] buf2 = new byte[8192];
                    int n2;

                    while ((n2 = input2.read(buf2)) != -1) {
                        out.write(buf2, 0, n2);
                    }
                    input2.close();
                    break;
            
                default:
                    out.writeBytes("HTTP/1.1 404 Not found\n");
                    out.writeBytes("Content-Length: 0" + "\n");
                    out.writeBytes("\n");
                    break;
            }
            s.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    }