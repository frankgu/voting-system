// udpServer.java: A simple UDP server program.
package com.server1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Properties;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.functions.Property;
import com.functions.Transmission;

public class Server1 {

	/*
	 * Server 1 functionality
	 */

	// -----1. server 1 should have the district information, each server 1
	// corresponds to only one district.
	// -----2. server 1 contain the user and the candidate information belongs
	// to one specific server1
	// -----3. when voter or candidate create account from client side and send
	// the information to server 1, server 1 should record the information to
	// the txt file.
	// -----4. when the voter voter for one candidate, the candidate information
	// should change (care for the conflict problem)
	// -----5. after a certain amount of time, the information of this server
	// will be sent to the server 2

	// -----the socket and the port number for this server
	private DatagramSocket aSocket = null;

	// -----data transimission
	private Transmission tran = null;

	// -----the host and the port for the server 2
	private static String host = "localhost";
	private static String port = "8081";

	// -----district for this server
	private String district = null;

	// -----active user list
	private String[] activeUsers = null;

	// for the lab
	// private static String host = "134.117.59.109";
	// private static String port = "60010";

	public Server1(String district, int portNumber, String host) {

		try {

			this.district = district;
			InetAddress aHost = InetAddress.getByName(host);

			// -----initialization
			int userNumber = Integer.parseInt(new Property()
					.loadProperties("activeUserForServer1"));
			aSocket = new DatagramSocket(portNumber, aHost);
			activeUsers = new String[userNumber];
			tran = new Transmission(aSocket);

			// for the lab in HP4115
			// int socket_no = 60009; // server 1 port number
			// InetAddress temp = InetAddress.getByName("134.117.59.109");
			// aSocket = new DatagramSocket(socket_no, temp);

			// ------receive the datagram packet from the client and simulate
			// the loss and modification
			byte[] buffer = new byte[80];
			while (true) {

				DatagramPacket request = new DatagramPacket(buffer,
						buffer.length);
				aSocket.receive(request);
				// -----every time the server 1 receive a packet, it start a new
				// thread to handle this
				new Thread(new Responder(aSocket, request)).start();

			}

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
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

	public void analyseDataFromClient(byte[] data, int length, int port,
			InetAddress host) {

		// -----request form : "[flag]:[value]"
		// [flag] = 1 , [value] = [flag2]:[userName] (regist account)
		// [flag] = 2 , [value] = [userName]:[candidateName] (voting)
		// [flag] = 3 , [value] = [userName] (login)
		// [flag] = 4 , [value] = null (get candidate list)
		// [flag] = 5 , [value] = [userName] (logout the server1)

		// -----reply form : "[flag]:[value]"
		// [flag] = 1 , [value] = string (error message)
		// [flag] = 2 , [value] = success (success message)
		
		// -----get the data exclude check sum value
		byte[] dataByte = Arrays.copyOfRange(data, 9, length);
		String message = new String(dataByte);

		String[] dataArray = message.split(":");
		
		if (dataArray[0].compareTo("1") == 0) {

			// -----regist an account
			if (dataArray[1].compareTo("1") == 0) {

				// voter
				tran.replyData("2:success", port, host);
				
			} else if (dataArray[1].compareTo("2") == 0) {

				// candidate
			}

		} else if (dataArray[0].compareTo("2") == 0) {

			// -----voting

		} else if (dataArray[0].compareTo("3") == 0) {

			// -----login

		} else if (dataArray[0].compareTo("4") == 0) {

			// -----get the candidate list

		} else if (dataArray[0].compareTo("5") == 0) {

			// -----user logout the server
		}
	}

}