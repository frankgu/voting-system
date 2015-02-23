// udpServer.java: A simple UDP server program.
package com.server2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Server2 {
	public static void main(String args[]) {
		DatagramSocket aSocket = null;

		try {
			int socket_no = 8081; // ------the port number for the server 2 is
									// 8081
			aSocket = new DatagramSocket(socket_no);
			
			//for the lab HP4115
			//int socket_no = 60010; 
			//InetAddress temp = InetAddress.getByName("134.117.59.109");
			//aSocket = new DatagramSocket(socket_no,temp);

			byte[] buffer = new byte[80];
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer,
						buffer.length);
				aSocket.receive(request);

				// ------check the checksum value to see if it matches
				byte[] value = request.getData();
				byte[] checksumByte = Arrays.copyOfRange(value, 0, 8);
				ByteBuffer buffer2 = ByteBuffer.wrap(checksumByte);
				long checksumValue = buffer2.getLong();

				// ------get the checksum value for the data.
				byte[] dataByte = Arrays.copyOfRange(value, 9,
						request.getLength());
				Checksum checksum = new CRC32();
				checksum.update(dataByte, 0, dataByte.length);
				long newchecksumValue = checksum.getValue();

				if (checksumValue != newchecksumValue) {
					byte[] replyByte = new String("fail to send the data")
							.getBytes();
					// ------send the message back to server 1
					DatagramPacket reply = new DatagramPacket(replyByte,		
							replyByte.length, request.getAddress(),
							request.getPort());
					aSocket.send(reply);
				} else {
					// ------reply to the server 1
					DatagramPacket reply = new DatagramPacket(dataByte,
							dataByte.length, request.getAddress(),
							request.getPort());
					aSocket.send(reply);
				}

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
}
