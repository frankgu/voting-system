package com.server1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import com.functions.Property;
import com.object.User;

public class Connection {

	private static Connection instance = new Connection();
	private Semaphore sem;
	private Responder res = null;

	public void initialize(DatagramSocket socket, String district,
			ArrayList<User> activeUsers, Server1 server1) {
		
		// initialize the semaphore number
		int semNum = 0;
		try {
			semNum = Integer.parseInt(new Property()
					.loadProperties("semaphoreNumber"));
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sem = new Semaphore(semNum, true);

		// initialize the responder

		res = new Responder(socket, district, activeUsers, server1);
		
	}

	private Connection() {

	}

	public static Connection getInstance() {
		return instance;
	}

	public void processPacket(DatagramPacket packet) throws IOException {

		// get the resource to process the packet
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			res.processPacket(packet);
		} finally {
			sem.release();
		}
	}
	
}