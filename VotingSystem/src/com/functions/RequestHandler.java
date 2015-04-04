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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;

public class RequestHandler implements Runnable {

	private DatagramPacket request;
	private int port;
	private InetAddress host;
	private DatagramSocket aSocket;
	private String reqData;

	public RequestHandler(String str, InetAddress addr, int iport, int p,
			String h) {

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
		DatagramPacket request = new DatagramPacket(buffer, buffer.length);
		request.setAddress(addr);
		request.setData(str.getBytes());
		request.setPort(iport);

		/*
		 * try { aSocket = new DatagramSocket(p, host); } catch (SocketException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
	}

	public void run() {
		String rtnMsg = new String(reqData);
		String data = rtnMsg.substring(2);
		
			System.out.println("Processing request content: "
					+ new String(reqData) + "\n");

			if (rtnMsg.split(":")[0].equals("0")) {// add server
				addLineToFile("sl.txt", data);
				System.out.println("Added: "+ data);
			} else if (rtnMsg.split(":")[0].equals("1")) {// del server
				removeLineFromFile("sl.txt", data);
				System.out.println("Deleted: " + data);
			} else {
				// do nothing
			}
	}

	public void removeLineFromFile(String file, String lineToRemove) {

		try {

			File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}

			// Construct the new file that will later be renamed to the original
			// filename.
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			// Read from the original file and write to the new
			// unless content matches data to be removed.
			while ((line = br.readLine()) != null) {
				if (line.split(":")[0].equals(lineToRemove.split(":")[0])&& 
					line.split(":")[1].equals(lineToRemove.split(":")[1])&&
					line.split(":")[2].equals(lineToRemove.split(":")[2])) {
					// skip write
				} else {
					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();

			// Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void addLineToFile(String file, String lineToAdd) {

		try {

			File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}

			// Construct the new file that will later be renamed to the original
			// filename.
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			// Read from the original file and write to the new
			// unless content matches data to be removed.
			while ((line = br.readLine()) != null) {
				pw.println(line);
				pw.flush();
			}
			String newLine = lineToAdd.split(":")[0] + ":" + lineToAdd.split(":")[1] +":"+ lineToAdd.split(":")[2];
			pw.println(newLine);
			pw.flush();
			
			pw.close();
			br.close();

			// Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}