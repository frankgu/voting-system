// udpServer.java: A simple UDP server program.
package com.server1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.functions.HibernateUtil;
import com.functions.Property;
import com.object.User;

public class Server1 implements Runnable {

	/*
	 * Server 1 functionality
	 */

	// the socket and the port number for this server
	private DatagramSocket aSocket = null;

	// server1 thread pools
	private ExecutorService executor = null;

	// -----active user list
	private ArrayList<User> activeUsers = null;

	// for the lab
	// private static String host = "134.117.59.109";
	// private static String port = "60010";

	public Server1(String district, int portNumber, String host) {

		try {

			InetAddress aHost = InetAddress.getByName(host);

			// initialize the socket for this server
			aSocket = new DatagramSocket(portNumber, aHost);

			// initialize the available active user list
			activeUsers = new ArrayList<User>(Integer.parseInt(new Property()
					.loadProperties("activeUserForServer1")));

			// initialize the connection instance
			Connection.getInstance().initialize(aSocket, district, activeUsers);

			// initialize the thread pools
			executor = Executors
					.newFixedThreadPool(Integer.parseInt(new Property()
							.loadProperties("threadPoolsSize")));

			// initial the hibernate factory
			HibernateUtil.getSessionFactory();

			// for the lab in HP4115
			// int socket_no = 60009; // server 1 port number
			// InetAddress temp = InetAddress.getByName("134.117.59.109");
			// aSocket = new DatagramSocket(socket_no, temp);

		} catch (SocketException e) {

			System.out.println("Socket: " + e.getMessage());

		} catch (IOException e) {

			System.out.println("IO: " + e.getMessage());

		}

	}

	public void stop() {

		aSocket.close();

	}

	@Override
	public void run() {

		// -----the main thread for the server1 is to add packet to the queue
		// for processing
		while (true) {
			// ------receive the datagram packet from the client and simulate
			// the loss and modification
			byte[] buffer = new byte[10000];

			DatagramPacket request = new DatagramPacket(buffer, buffer.length);

			try {

				aSocket.receive(request);
			} catch (SocketTimeoutException e) {

				// do nothing

			} catch (IOException e) {

				e.printStackTrace();

			}

			// for every incoming packet, the server1 will start a new
			// thread to handle this packet.
			executor.submit(new ThreadHandler(request));

		}
	}

}
