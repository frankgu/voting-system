package com.functions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Transmission {

	private DatagramSocket aSocket;

	public Transmission(DatagramSocket socket) {

		this.aSocket = socket;
		// ------set the time out of the socket to 1000 ms
		try {
			aSocket.setSoTimeout(1000);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// reply the data without checking the data has been valid or not.
	public void replyData(String data, int port, InetAddress host) {
		try {

			byte[] buffer = new String(data).getBytes("UTF-8");
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length,
					host, port);
			aSocket.send(reply);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// send the data, with valid crc32 code.
	public String sendData(String data, int port, InetAddress host) {

		return sendData(data, port, host, 1);

	}

	public String sendData(String data, int port, InetAddress host, int count) {

		try {

			byte[] m = new String(data).getBytes("UTF-8");

			// ---------get the checksum value for 'm'
			Checksum checksum = new CRC32();
			checksum.update(m, 0, m.length);
			long checksumValue = checksum.getValue();
			byte[] CVByte = ByteBuffer.allocate(9).putLong(checksumValue)
					.array();

			// ---------combine the checksum value to the datagram packet
			byte[] combineValue = new byte[m.length + CVByte.length];
			System.arraycopy(CVByte, 0, combineValue, 0, CVByte.length);
			System.arraycopy(m, 0, combineValue, CVByte.length, m.length);

			// ------send the packet
			DatagramPacket request = new DatagramPacket(combineValue,
					combineValue.length, host, port);
			aSocket.send(request);

			// ------receive the data
			byte[] buffer = new byte[10000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			
			// ------check the reply message
			byte[] dataByte = Arrays.copyOfRange(reply.getData(), 0,
					reply.getLength());
			String validResult = new String(dataByte);
			if (validResult.compareTo("resend") != 0) {

				return validResult;

			} else {

				Thread.sleep(100);
				// ------send the data again, because the data is invalid
				System.out.println("Broken data, data being resent!");
				return sendData(data, port, host);				

			}

		} catch (SocketTimeoutException e) {

			// Resends the data
			if (count <= 2) {

				count++;
				System.out.println("Timeout, data being resent!");
				return sendData(data, port, host, count);

			} else {
				
				return "null";
				
			}

		} catch (SocketException e) {

			System.out.println("Socket: " + e.getMessage());

		} catch (IOException e) {

			System.out.println("IO: " + e.getMessage());

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "null";

	}

	public boolean dataVlidated(byte[] value, int length) {

		// ------check the checksum value to see if it matches
		byte[] checksumByte = Arrays.copyOfRange(value, 0, 8);
		ByteBuffer buffer2 = ByteBuffer.wrap(checksumByte);
		long checksumValue = buffer2.getLong();

		// ------get the checksum value for the data.
		byte[] dataByte = Arrays.copyOfRange(value, 9, length);
		String data = null;
		try {
			data = new String(dataByte, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Checksum checksum = new CRC32();
		checksum.update(dataByte, 0, dataByte.length);
		long newchecksumValue = checksum.getValue();

		//System.out.println(newchecksumValue + " " + data);

		if (checksumValue != newchecksumValue) {
			return false;
		} else {
			return true;
		}

	}
}
