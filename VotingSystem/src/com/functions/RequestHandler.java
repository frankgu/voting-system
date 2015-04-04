package com.functions;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

public class RequestHandler implements Runnable {

	private DatagramPacket request;
	private int port;
	private InetAddress host;
	private DatagramSocket aSocket;
	private String reqData;

	public RequestHandler(String str, InetAddress addr, int iport, int p, String h) {

        reqData = "";
        reqData = str;
		port = p;
		try {
			host = InetAddress.getByName(h);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] buffer = new byte[80];
        DatagramPacket request = new DatagramPacket(buffer,
                                                    buffer.length);
		request.setAddress(addr);
		request.setData(str.getBytes());
		request.setPort(iport);

		/*try {
			aSocket = new DatagramSocket(p, host);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public void run() {
		String rtnMsg = new String(reqData);
		String data = rtnMsg.substring(2);

		try {
			System.out.println("Processing request content: "
					+ new String(reqData) + "\n");

			if (rtnMsg.split(":")[0].equals("0")) {// add server
				File inputFile = new File("sl.txt");
				/*File tempFile = new File("slTempFile.txt");

				BufferedReader reader = new BufferedReader(new FileReader(inputFile));
				BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

				String lineToRemove = data;
				String currentLine;

				while((currentLine = reader.readLine()) != null) {
				    // trim newline when comparing with lineToRemove
				    String trimmedLine = currentLine.trim();
				    if(trimmedLine.equals(lineToRemove)) continue;
				    writer.write(currentLine + System.getProperty("line.separator"));
				}
				writer.close(); 
				reader.close(); 
				boolean successful = tempFile.renameTo(inputFile);*/
				
				System.out.println(data+"endline");
				BufferedWriter output;
				output = new BufferedWriter(new FileWriter(inputFile, true));
				
				output.write(data);
				output.write(System.getProperty("line.separator"));
				output.close();

			} else if (rtnMsg.split(":")[0].equals("1")) {//del server
				File inputFile = new File("sl.txt");
				File tempFile = new File("slTemp.txt");

				BufferedReader reader = new BufferedReader(new FileReader(inputFile));
				BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

				String lineToRemove = "";
				lineToRemove = data;
				String currentLine;

				while((currentLine = reader.readLine()) != null) {
				    // trim newline when comparing with lineToRemove
				    String trimmedLine = currentLine.trim();
				    if(trimmedLine.equals(lineToRemove)) continue;
				    writer.write(currentLine + System.getProperty("line.separator"));
				}
				writer.close(); 
				reader.close(); 
				boolean successful = tempFile.renameTo(inputFile);
			} else {
				//do nothing
			}
			/*DatagramPacket reply = new DatagramPacket(request
                    .getData(), request.getLength(), request
                    .getAddress(), request.getPort());

			aSocket.send(reply);*/
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

}