// udpServer.java: A simple UDP server program.
package com.server2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;

import com.functions.HibernateUtil;
import com.functions.Transmission;
import com.object.Candidate;

public class Server2 implements Runnable {

	// -----the socket and the port number for this server
	private DatagramSocket aSocket = null;

	// -----data transimission
	private Transmission tran = null;

	public Server2(int portNumber, String host) {

		try {

			InetAddress aHost = InetAddress.getByName(host);
			// -----initialization
			aSocket = new DatagramSocket(portNumber, aHost);

			tran = new Transmission(aSocket);

			// initial the hibernate factory
			HibernateUtil.getSessionFactory();

			// for the lab in HP4115
			// int socket_no = 60009; // server 1 port number
			// InetAddress temp = InetAddress.getByName("134.117.59.109");
			// aSocket = new DatagramSocket(socket_no, temp)

		} catch (SocketException e) {

			System.out.println("Socket: " + e.getMessage());

		} catch (IOException e) {

			System.out.println("IO: " + e.getMessage());

		}
	}

	@Override
	public void run() {
		//run the poll updater
		new Thread(new PollUpdater()).start();
		
		// ------receive the datagram packet from the client and simulate
		// the loss and modification
		byte[] buffer = new byte[10000];
		/*
		while (true) {

			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			try {
				aSocket.receive(request);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// -----every time the server 1 receive a packet, it start a new
			// thread to handle this
			new Thread(new Responder(aSocket, request)).start();

		}*/

	}

	public class Responder implements Runnable {

		DatagramSocket socket = null;
		DatagramPacket packet = null;

		public Responder(DatagramSocket socket, DatagramPacket packet) {
			this.socket = socket;
			this.packet = packet;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			// -----check if the data is valid or not, if the data is
			// invalid, tell the client to send the message again
			if (!tran.dataVlidated(packet.getData(), packet.getLength())) {

				String resendMessage = new String("resend");
				tran.replyData(resendMessage, packet.getPort(),
						packet.getAddress());

			} else {
				
				// -----data is valid, process the data
				analyseDataFromClient(packet.getData(), packet.getLength(),
						packet.getPort(), packet.getAddress());
			}
		}
	}

	private void analyseDataFromClient(byte[] data, int length, int port,
			InetAddress host) {

		// -----get the data exclude check sum value
		byte[] dataByte = Arrays.copyOfRange(data, 9, length);
		String message = new String(dataByte);

		String[] dataArray = message.split(":");

		if (dataArray[0].compareTo("1") == 0) {

			getCandidatePolls("", port, host);

		} else if (dataArray[0].compareTo("2") == 0) {

			String distrctName = dataArray[1];
			getCandidatePolls(distrctName, port, host);

		}
	}

	private void getCandidatePolls(String distrct, int port, InetAddress host) {

		// -----get the candidate list
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		//retrieve all the candidate data from the database and then store them as the candidate object
		//into the list(below)
		List<Candidate> candidates = session.createCriteria(Candidate.class)
				.list();
		
		Collections.sort(candidates,new PollComparator());

		String candidatePollsData = "";
		for (int i = 0; i < candidates.size(); i++) {

			if (distrct.isEmpty()
					|| candidates.get(i).getDistrictName().compareTo(distrct) == 0) {

				if (i == (candidates.size() - 1)) {
					candidatePollsData = candidatePollsData + ":"
							+ candidates.get(i).getFirstName() + ":"
							+ candidates.get(i).getLastName() + ":"
							+ candidates.get(i).getPolls();

				} else {
					candidatePollsData = candidatePollsData + ":"
							+ candidates.get(i).getFirstName() + ":"
							+ candidates.get(i).getLastName() + ":"
							+ candidates.get(i).getPolls() + ":";
				}
			}
		}
		tran.replyData(candidatePollsData, port, host);

		session.getTransaction().commit();
		session.close();

	}

	public void stop() {

		aSocket.close();

	}
}
