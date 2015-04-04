package com.run;


import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.Random;
import java.util.Stack;

import com.functions.*;

public class ServerListManager {
    
    public static DatagramSocket aSocket;
    
    
    
    // public static int threadIndex = 0;
    
    public static void main(String args[]) throws Exception {
        
        ExecutorService executor = Executors.newFixedThreadPool(50);
        // DatagramSocket aSocket = null;
        final Semaphore sem = new Semaphore(1);
        if (args.length < 2) {
            System.out.println("Usage: java UDPServer <Host IP> <Port Number>");
            System.exit(1);
        }

        try {
            int socket_no = Integer.valueOf(args[1]).intValue();
			InetAddress aHost = InetAddress.getByName(args[0]);
			
            aSocket = new DatagramSocket(socket_no, aHost);
            
            while (true) {

        		
                byte[] buffer = new byte[80];
                DatagramPacket request = new DatagramPacket(buffer,
                                                            buffer.length);
                aSocket.receive(request);
                //Thread.sleep(2000);
                new Thread(new RequestHandler(new String(request.getData()), request.getAddress(),request.getPort(), socket_no, args[0])).start();
            }
            
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        }
    }
}
