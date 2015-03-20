// udpServer.java: A simple UDP server program.
package com.server2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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

		} catch (SocketException e) {

			System.out.println("Socket: " + e.getMessage());

		} catch (IOException e) {

			System.out.println("IO: " + e.getMessage());

		}
	}

	@Override
	public void run() {
		// run the poll updater
		new Thread(new PollUpdater()).start();

		while (true) {

			try {
				
				byte[] buffer = new byte[10000];
				DatagramPacket request = new DatagramPacket(buffer,
						buffer.length);

				aSocket.receive(request);
				
				// -----every time the server 1 receive a packet, it start a new
				// thread to handle this
				new Thread(new Responder(aSocket, request)).start();
				
			} catch (SocketTimeoutException e) {

				// do nothing if the receive socket time out.
				
			} catch (IOException e) {

				e.printStackTrace();

			}
		
		}

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
		// retrieve all the candidate data from the database and then store them
		// as the candidate object
		// into the list(below)
		List<Candidate> candidates = session.createCriteria(Candidate.class)
				.list();

		Collections.sort(candidates, new PollComparator());

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
