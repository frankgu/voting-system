// udpServer.java: A simple UDP server program.
package com.server2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

import com.functions.HibernateUtil;
import com.functions.Transmission;
import com.server1.Server1.Responder;

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
			// aSocket = new DatagramSocket(socket_no, temp);

		} catch (SocketException e) {

			System.out.println("Socket: " + e.getMessage());

		} catch (IOException e) {

			System.out.println("IO: " + e.getMessage());

		}
	}

	@Override
	public void run() {

		// ------receive the datagram packet from the client and simulate
		// the loss and modification
		byte[] buffer = new byte[10000];
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

		// -----request form : "[flag]:[value]"
		// ---[flag] = 1 , [value] = null (get all the candidate polls)
		// ---[flag] = 2 , [value] = [district name] (get the candidate polls
		// depends on the district)

		// -----reply form : "[flag]:[value]"
		// ---[flag] = 1 , [value] = string (error message)
		// ---[flag] = 2 , [value] =
		// [candidateFirstName]:[candidateLastName]:[polls]:...... (get all the
		// candidate polls)
		// ---[flag] = 3 , [value] =
		// [candidateFirstName]:[candidateLastName]:[polls]:...... (get the
		// candidate polls depends on the district)

		// -----get the data exclude check sum value
		byte[] dataByte = Arrays.copyOfRange(data, 9, length);
		String message = new String(dataByte);

		String[] dataArray = message.split(":");

		if (dataArray[0].compareTo("1") == 0) {

			String userName = dataArray[2];
			String lastName = dataArray[3];
			String firstName = dataArray[4];
			String address = dataArray[5];
			String password = dataArray[6];

			// -----regist an account
			if (dataArray[1].compareTo("1") == 0) {

				voterRegistration(userName, lastName, firstName, address,
						password, port, host);

			} else if (dataArray[1].compareTo("2") == 0) {

				candidateRegistration(userName, lastName, firstName, address,
						password, port, host);

			}

		} else if (dataArray[0].compareTo("2") == 0) {

			String voterUserName = dataArray[1];
			String candidateUserName = dataArray[2];

			// -----voter vote for the candidate
			voterVote(voterUserName, candidateUserName, port, host);

		} else if (dataArray[0].compareTo("3") == 0) {

			String userName = dataArray[1];
			String password = dataArray[2];

			// -----voter login
			login(userName, password, port, host);

		} else if (dataArray[0].compareTo("4") == 0) {

			// -----get the candidate list
			getCandidateList(port, host);

		} else if (dataArray[0].compareTo("5") == 0) {

			String userName = dataArray[1];

			// -----user logout the server
			logout(userName, port, host);

		} else if (dataArray[0].compareTo("6") == 0) {

			String userName = dataArray[1];

			// -----check the voter vote state
			checkVoteState(userName, port, host);

		}
	}

	public void stop() {

		aSocket.close();

	}
}
