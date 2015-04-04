// udpServer.java: A simple UDP server program.
package com.server1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.functions.HibernateUtil;
import com.functions.Property;
import com.functions.Transmission;
import com.object.User;

public class Server1 implements Runnable {

	/*
	 * Server 1 functionality
	 */

	// the socket and the port number for this server
	private DatagramSocket aSocket = null;

	// server1 thread pools
	private ExecutorService executor = null;

	// active user list
	private ArrayList<User> activeUsers = null;

	// Determine whether the election has been closed or not
	private boolean electionClosed;
	
	private String servInfo;

	public boolean isElectionClosed() {
		return electionClosed;
	}

	public void setElectionClosed(boolean electionClosed) {
		this.electionClosed = electionClosed;
	}

	public Server1(String district, int portNumber, String host) {

		try {

			// start the election
			electionClosed = false;

			InetAddress aHost = InetAddress.getByName(host);

			// initialize the socket for this server
			aSocket = new DatagramSocket(portNumber, aHost);

			// initialize the available active user list
			activeUsers = new ArrayList<User>(Integer.parseInt(new Property()
					.loadProperties("activeUserForServer1")));

			// initialize the connection instance
			Connection.getInstance().initialize(aSocket, district, activeUsers, this);

			// initialize the thread pools
			executor = Executors
					.newFixedThreadPool(Integer.parseInt(new Property()
							.loadProperties("threadPoolsSize")));

			// initial the hibernate factory
			HibernateUtil.getSessionFactory();


		} catch (SocketException e) {

			System.out.println("Socket: " + e.getMessage());

		} catch (IOException e) {

			System.out.println("IO: " + e.getMessage());

		}
		servInfo = "0:"+district+":"+host+":"+portNumber;
		//------------add server to serverList-------------
				DatagramSocket bSocket;
				try {
					bSocket = new DatagramSocket();
					Transmission btran = new Transmission(bSocket);
					InetAddress bhost = InetAddress.getByName("go.joyclick.org");
					btran.sendData("0:"+district+":"+host+":"+portNumber, 8888, bhost);
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		//-------------------------------------------------

	}

	public void stop() {
		
		//------------add server to serverList-------------
		DatagramSocket bSocket;
		try {
			bSocket = new DatagramSocket();
			Transmission btran = new Transmission(bSocket);
			InetAddress bhost = InetAddress.getByName("go.joyclick.org");
			btran.sendData(servInfo, 8888, bhost);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//-------------------------------------------------
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

				if (electionClosed) {

					// if the election has been closed , then return the error
					// message
					String replydata = "1:The election for this district has been closed, you can check the result on TV";
					Transmission tran = new Transmission(aSocket);
					tran.replyData(replydata, request.getPort(),
							request.getAddress());

				} else {
					// for every incoming packet, the server1 will start a new
					// thread to handle this packet.
					executor.submit(new ThreadHandler(request));
				}
				
			} catch (SocketTimeoutException e) {

				// do nothing

			} catch (IOException e) {

				e.printStackTrace();

			}

		

		}
	}

}
