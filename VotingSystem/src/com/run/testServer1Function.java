package com.run;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.functions.Transmission;

public class testServer1Function {

	public static void main(String[] arg) {
		
		/*
		 * test the server1 functionality
		 */
		try {

			DatagramSocket aSocket = new DatagramSocket();
			Transmission tran = new Transmission(aSocket);

			// -----test
			InetAddress host = InetAddress.getByName("go.joyclick.org");


			System.out.println(tran.sendData(
					"1:2:candidate:gu:dongfeng:1591 riverside", 8088,
					host));
			
			System.out.println(tran.sendData(
					"1:2:candidate22:gau:dodngfeng:1591 riverdside", 8088,
					host));
			System.out.println(tran.sendData(
					"1:1:voter:tom:liu:1591 riverside:hyfgdf", 8080, host));

			System.out.println(tran.sendData("2:voter:candidate", 8080, host));

			System.out.println(tran.sendData("3:voter:hyfgdf", 8080, host));

			System.out.println(tran.sendData("4:", 8080, host));

			System.out.println(tran.sendData("5:voter", 8080, host));
			System.out.println(tran.sendData("5:voter", 8080, host));
			
			System.out.println(tran.sendData("6:voter", 8080, host));

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (UnknownHostException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}
}
