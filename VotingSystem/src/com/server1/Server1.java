// udpServer.java: A simple UDP server program.
package com.server1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import com.functions.HibernateUtil;
import com.functions.Property;
import com.functions.Transmission;
import com.object.Candidate;
import com.object.User;
import com.object.Voter;

public class Server1 implements Runnable {

	/*
	 * Server 1 functionality
	 */

	// -----5. after a certain amount of time, the information of this server
	// will be sent to the server 2

	// -----the socket and the port number for this server
	private DatagramSocket aSocket = null;

	// -----data transimission
	private Transmission tran = null;

	// -----the host and the port for the server 2
	private static String server2host = "localhost";
	private static String server2port = "8081";

	// -----district for this server
	private String district = null;

	// -----active user list
	private ArrayList<User> activeUsers = null;

	// -----lock for the critical section
	private Lock lock1 = new ReentrantLock();
	private Lock lock2 = new ReentrantLock();
	private Lock lock3 = new ReentrantLock();

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
			activeUsers = new ArrayList<User>(userNumber);

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

	public void stop() {

		aSocket.close();

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

	public void analyseDataFromClient(byte[] data, int length, int port,
			InetAddress host) {

		// -----request form : "[flag]:[value]"
		// ---[flag] = 1 , [value] =
		// [flag2]:[userName]:[lastName]:[firstName]:[address]:[password]
		// (regist account,
		// flag2 = 1 is voter, flag2 = 2 is candidate)
		// ---[flag] = 2 , [value] = [userName]:[candidateUserName] (voting)
		// ---[flag] = 3 , [value] = [userName]:[password] (login)
		// ---[flag] = 4 , [value] = null (get candidate list)
		// ---[flag] = 5 , [value] = [userName] (logout the server1)

		// -----reply form : "[flag]:[value]"
		// ---[flag] = 1 , [value] = string (error message)
		// ---[flag] = 2 , [value] = success
		// ---[flag] = 3 , [value] = [candidate name]:[candidate name]:...
		// (candidate name consist of [userName]:[FirstName]:[LastName])

		// -----get the data exclude check sum value
		byte[] dataByte = Arrays.copyOfRange(data, 9, length);
		String message = new String(dataByte);

		String[] dataArray = message.split(":");

		if (dataArray[0].compareTo("1") == 0) {

			// -----regist an account
			if (dataArray[1].compareTo("1") == 0) {

				Voter voter = new Voter(dataArray[2], dataArray[3],
						dataArray[4], district, dataArray[5], dataArray[6]);

				String replyMessage = new String(
						"2:Successfully regist for voter");

				try {

					// -----get the restrict number for the voter
					int userNumber = Integer.parseInt(new Property()
							.loadProperties("totalVoterNumberForOneDistrict"));

					// store the voter information to the database
					Session session = HibernateUtil.getSessionFactory()
							.openSession();
					session.beginTransaction();

					int existUserNumber = ((Long) session
							.createCriteria(Voter.class)
							.setProjection(Projections.rowCount())
							.uniqueResult()).intValue();

					if (existUserNumber < userNumber) {

						session.save(voter);

					} else {

						replyMessage = new String(
								"1:Voter number reach the limits for this district");

					}

					session.getTransaction().commit();
					session.close();

				} catch (JDBCException e) {

					// fail to insert the voter because the userName already
					// being used
					replyMessage = new String("1:Voter user name already exist");

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// reply to the client
				tran.replyData(replyMessage, port, host);

			} else if (dataArray[1].compareTo("2") == 0) {

				Candidate candidate = new Candidate(dataArray[2], dataArray[3],
						dataArray[4], district, dataArray[5], dataArray[6]);

				String replyMessage = new String(
						"2:Sucessfully regist for candidate");

				try {

					// -----get the restrict number for the voter
					int userNumber = Integer.parseInt(new Property()
							.loadProperties("totalVoterNumberForOneDistrict"));

					// store the candidate information to the database
					Session session = HibernateUtil.getSessionFactory()
							.openSession();
					session.beginTransaction();

					int existUserNumber = ((Long) session
							.createCriteria(Candidate.class)
							.setProjection(Projections.rowCount())
							.uniqueResult()).intValue();

					if (existUserNumber < userNumber) {

						session.save(candidate);

					} else {

						replyMessage = new String(
								"1:Candidate number reach the limits for this district");

					}

					session.getTransaction().commit();
					session.close();

				} catch (JDBCException e) {

					replyMessage = new String(
							"1:Candidate user name already exist");

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				tran.replyData(replyMessage, port, host);

			}

		} else if (dataArray[0].compareTo("2") == 0) {

			// -----need to add a mutex lock here
			lock1.tryLock();

			// -----voter vote for the candidate
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			Candidate candidate = (Candidate) session.get(Candidate.class,
					dataArray[2]);
			Voter voter = (Voter) session.get(Voter.class, dataArray[1]);

			if (voter.getCandidateName().isEmpty()) {

				candidate.setPolls(candidate.getPolls() + 1);
				session.update(candidate);
				voter.setCandidateName(dataArray[2]);
				session.update(voter);
				tran.replyData("2:Sucessfully vote", port, host);

			} else {

				tran.replyData("1:Voter already voted", port, host);

			}

			session.getTransaction().commit();
			session.close();

			lock1.unlock();

		} else if (dataArray[0].compareTo("3") == 0) {

			// -----login, only user belong to this district can login to this
			// server, login can't have same user login the same time
			String userName = dataArray[1];
			String password = dataArray[2];

			try {

				Session session = HibernateUtil.getSessionFactory()
						.openSession();
				session.beginTransaction();
				Voter voter = (Voter) session.get(Voter.class, userName);

				// lock these lines of code in case same user name login at the
				// same time
				lock2.tryLock();

				if (password.compareTo(voter.getPassword()) == 0) {

					if (!checkExist(voter)) {

						if (voter.getDistrictName().compareTo(district) == 0) {

							tran.replyData("2:Successfully login", port, host);
							activeUsers.add(voter);

						} else {

							tran.replyData(
									"1:Voter doesn't belong to this destrict",
									port, host);

						}

					} else {

						tran.replyData("1:Voter already login", port, host);

					}

				} else {

					tran.replyData("1:Invalid Password", port, host);

				}
				lock2.unlock();
				session.getTransaction().commit();
				session.close();

			} catch (JDBCException e) {

				tran.replyData(
						"1:Can't find the voter name, please regist an account or login as a voter",
						port, host);
			}

		} else if (dataArray[0].compareTo("4") == 0) {

			// -----get the candidate list
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			List<Candidate> candidates = session
					.createCriteria(Candidate.class).list();
			String candidateData = "";
			for (int i = 0; i < candidates.size(); i++) {
				if (i == (candidates.size() - 1)) {
					candidateData = candidateData
							+ candidates.get(i).getUserName() + ":"
							+ candidates.get(i).getFirstName() + ":"
							+ candidates.get(i).getLastName();

				} else {
					candidateData = candidateData
							+ candidates.get(i).getUserName() + ":"
							+ candidates.get(i).getFirstName() + ":"
							+ candidates.get(i).getLastName() + ":";
				}

			}
			tran.replyData(candidateData, port, host);
			session.getTransaction().commit();
			session.close();

		} else if (dataArray[0].compareTo("5") == 0) {

			// -----user logout the server
			String userName = dataArray[1];

			// ------only one user can logout at a time
			lock3.tryLock();

			boolean success = removeActiveUser(userName);

			lock3.unlock();

			if (success) {
				
				tran.replyData("2:Successfully logout", port, host);

			} else {

				tran.replyData("1:Voter already logout", port, host);
			
			}
		}
	}

	public boolean removeActiveUser(String userName) {

		for (int i = 0; i < activeUsers.size(); i++) {

			if (activeUsers.get(i).getUserName().compareTo(userName) == 0) {

				activeUsers.remove(i);
				return true;

			}

		}

		return false;

	}

	public boolean checkExist(User user) {

		for (int i = 0; i < activeUsers.size(); i++) {

			if (user.getUserName().compareTo(activeUsers.get(i).getUserName()) == 0) {

				// the user already login
				return true;

			}

		}

		return false;

	}

}