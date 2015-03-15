package com.run;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.functions.Transmission;

public class testServer1Function {

	private int port;
	private InetAddress host;
	private Transmission tran;

	public void begin() {

		/*
		 * test the server1 functionality
		 */
		try {

			port = 8080;
			host = InetAddress.getByName("127.0.0.1");

			DatagramSocket aSocket = new DatagramSocket();
			tran = new Transmission(aSocket);

			// -----test
			// InetAddress host = InetAddress.getByName("go.joyclick.org"); //
			// the remote server

			System.out.println(tran.sendData(
					"1:2:candidate:gu:dongfeng:1591 riverside", port, host));

			System.out
					.println(tran.sendData(
							"1:2:candidate2:gau:dodngfeng:1591 riverdside",
							port, host));
			System.out.println(tran.sendData(
					"1:1:voter:tom:liu:1591 riverside:hyfgdf", port, host));

			System.out.println(tran.sendData(
					"1:1:voter2:hello:liu:1591 riverside:hyfgdf", port, host));
			System.out.println(tran.sendData(
					"1:1:voter3:tom:liu:1591 riverside:hyfgdf", port, host));

			System.out.println(tran.sendData(
					"1:1:voter4:hello:liu:1591 riverside:hyfgdf", port, host));
			System.out.println(tran.sendData(
					"1:1:voter5:tom:liu:1591 riverside:hyfgdf", port, host));

			System.out.println(tran.sendData(
					"1:1:voter6:hello:liu:1591 riverside:hyfgdf", port, host));

			// -----test the concurrenty voting
			new Thread(new testVoting("voter", "candidate")).start();
			new Thread(new testVoting("voter2", "candidate")).start();
			new Thread(new testVoting("voter3", "candidate")).start();
			new Thread(new testVoting("voter4", "candidate")).start();
			new Thread(new testVoting("voter5", "candidate")).start();
			new Thread(new testVoting("voter6", "candidate")).start();

			// ----test the login
			new Thread(new testLogin("voter", "hyfgdf")).start();
			new Thread(new testLogin("voter2", "hyfgdf")).start();
			new Thread(new testLogin("voter3", "hyfgdf")).start();
			new Thread(new testLogin("voter4", "hyfgdf")).start();
			new Thread(new testLogin("voter5", "hyfgdf")).start();
			new Thread(new testLogin("voter6", "hyfgdf")).start();

			System.out.println(tran.sendData("4:", port, host));

			// -----test the logout of the system
			new Thread(new testLogout("voter")).start();
			new Thread(new testLogout("voter2")).start();
			new Thread(new testLogout("voter3")).start();
			new Thread(new testLogout("voter4")).start();
			new Thread(new testLogout("voter5")).start();
			new Thread(new testLogout("voter6")).start();

			System.out.println(tran.sendData("6:voter", port, host));

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (UnknownHostException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public class testLogout implements Runnable {

		private String name;

		public testLogout(String name) {
			this.name = name;
		}

		@Override
		public void run() {

			String data = "5:" + name;
			System.out.println(tran.sendData(data, port, host));
		}

	}

	public class testLogin implements Runnable {

		private String name;
		private String password;

		public testLogin(String name, String password) {
			this.name = name;
			this.password = password;
		}

		@Override
		public void run() {

			String data = "3:" + name + ":" + password;
			System.out.println(tran.sendData(data, port, host));

		}

	}

	public class testVoting implements Runnable {

		private String voter;
		private String candidate;

		public testVoting(String voter, String candidate) {
			this.voter = voter;
			this.candidate = candidate;
		}

		@Override
		public void run() {

			String data = "2:" + voter + ":" + candidate;
			System.out.println(tran.sendData(data, port, host));

		}

	}
}
