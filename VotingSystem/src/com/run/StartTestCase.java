package com.run;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.functions.Transmission;
import com.testframework.testframework_launch;

public class StartTestCase {
	public static void main(String args[]){
		//testframework_launch run = new testframework_launch();
		//testframework_launch.main(args);
		
		
		try {
			DatagramSocket bSocket;
			bSocket = new DatagramSocket();
			byte[] buffer = new byte[80];
			InetAddress bhost = InetAddress.getByName("go.joyclick.org");
			
	        DatagramPacket request = new DatagramPacket(buffer,
	                                                    buffer.length);
	        
	        String a = "1:test2ed:losdfst:8089:";
			request.setAddress(bhost);
			request.setData(a.getBytes());
			request.setPort(8089);
			
			bSocket.send(request);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
