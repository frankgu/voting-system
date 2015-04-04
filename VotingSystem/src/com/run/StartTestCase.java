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
		testframework_launch run = new testframework_launch();
		testframework_launch.main(args);
		
	}
}
