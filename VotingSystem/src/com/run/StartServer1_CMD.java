package com.run;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.server1.Server1;

public class StartServer1_CMD {

	public static void main(String[] arg){
		if(!arg[0].isEmpty() && !arg[1].isEmpty() && !arg[2].isEmpty()){
			final String servInfo = arg[0]+":"+arg[1]+":"+arg[2];
			Runtime.getRuntime().addShutdownHook(new Thread() {
				//exit handler
			    public void run() {
			    	//------------delete server to serverList-------------
					try {
						DatagramSocket cSocket;
						cSocket = new DatagramSocket();
						byte[] buffer = new byte[80];
						InetAddress bhost = InetAddress.getByName("go.joyclick.org");
						
				        DatagramPacket request = new DatagramPacket(buffer,
				                                                    buffer.length);
				        String a = "1:"+servInfo+":";
						request.setAddress(bhost);
						request.setData(a.getBytes());
						request.setPort(8089);
						
						cSocket.send(request);
						cSocket.close();
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
			      //-------------------------------------------------
			    }
			 });
			//------------add server to serverList-------------
			try {
				DatagramSocket bSocket;
				bSocket = new DatagramSocket();
				byte[] buffer = new byte[80];
				InetAddress bhost = InetAddress.getByName("go.joyclick.org");
				
		        DatagramPacket request = new DatagramPacket(buffer,
		                                                    buffer.length);
		        String a = "0:"+servInfo+":";
				request.setAddress(bhost);
				request.setData(a.getBytes());
				request.setPort(8089);
				
				bSocket.send(request);
				bSocket.close();
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
			//-------------------------------------------------
			Server1 server1 = new Server1(arg[0], Integer.parseInt(arg[2]), arg[1]);
			Thread thread = new Thread(server1);
			thread.start();
		}else{
			System.out.println("Invalid Inputs!\njava -jar name ip port");
		}
		
		
	}
}
