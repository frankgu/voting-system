package com.server1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import com.functions.HibernateUtil;
import com.functions.Property;
import com.functions.Transmission;
import com.object.Candidate;
import com.object.User;
import com.object.Voter;

public class Responder {

	private String district = null;
	private Transmission tran = null;
	private Server1 server1 = null;

	// -----lock for the critical section
	private static Object lock1 = new Object();
	private static Object lock2 = new Object();

	private ArrayList<User> activeUsers = null;

	public Responder(DatagramSocket socket, String district,
			ArrayList<User> activeUsers, Server1 server1) {

		this.activeUsers = activeUsers;
		this.district = district;
		tran = new Transmission(socket);
		this.server1 = server1;

	}

	public void processPacket(DatagramPacket packet) {

		// -----check if the data is valid or not, if the data is
		// invalid, tell the client to send the message again

		if (!tran.dataVlidated(packet.getData(), packet.getLength())) {

			String resendMessage = new String("resend");
			tran.replyData(resendMessage, packet.getPort(), packet.getAddress());

		} else {

			// -----data is valid, process the data
			analyseDataFromClient(packet.getData(), packet.getLength(),
					packet.getPort(), packet.getAddress());

		}

	}

	private void analyseDataFromClient(byte[] data, int length, int port,
			InetAddress host) {

		byte[] dataByte = Arrays.copyOfRange(data, 9, length);
		String message = new String(dataByte);
		System.out.println(message);
		String[] dataArray = message.split(":");

		if (dataArray[0].compareTo("0") == 0) {

			// close the election
			server1.setElectionClosed(true);
			tran.replyData(
					"2:Successfully close the election of this district", port,
					host);

		} else if (dataArray[0].compareTo("1") == 0) {

			String userName = dataArray[2];
			String lastName = dataArray[3];
			String firstName = dataArray[4];
			String address = dataArray[5];

			// -----regist an account
			if (dataArray[1].compareTo("1") == 0) {

				String password = dataArray[6];
				voterRegistration(userName, lastName, firstName, address,
						password, port, host);

			} else if (dataArray[1].compareTo("2") == 0) {

				candidateRegistration(userName, lastName, firstName, address,
						"", port, host);

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

		} else if (dataArray[0].compareTo("7") == 0) {

			// -----return the district information to the client
			getDistrictInfo(port, host);
		}
	}

	private void getDistrictInfo(int port, InetAddress host) {

		tran.replyData("5:" + district, port, host);

	}

	private void checkVoteState(String userName, int port, InetAddress host) {

		// ---[flag] = 4 , [value] =
		// [flag2]:[candidateFirstName]:[candidateLastName] (check the voter
		// vote state) [flag2] = 1 (voter hasn't vote) , [flag2] = 2 (voter
		// already vote and return the candidate name)
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Voter voter = (Voter) session.get(Voter.class, userName);
		Candidate cand = (Candidate) session.get(Candidate.class,
				voter.getCandidateName());// to fix reply msg

		if (voter.getCandidateName().isEmpty()) {

			tran.replyData("4:1", port, host);

		} else {

			tran.replyData(
					"4:2:" + cand.getFirstName() + ":" + cand.getLastName(),
					port, host);

		}
		session.getTransaction().commit();
		session.close();

	}

	private void voterRegistration(String userName, String lastName,
			String firstName, String address, String password, int port,
			InetAddress host) {

		Voter voter = new Voter(userName, lastName, firstName, district,
				address, password);

		String replyMessage = new String("2:(" + userName
				+ ") has successfully registered");

		try {

			// -----get the restrict number for the voter
			int userNumber = Integer.parseInt(new Property()
					.loadProperties("totalVoterNumberForOneDistrict"));

			// store the voter information to the database
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			int existUserNumber = ((Long) session.createCriteria(Voter.class)
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();

			if (existUserNumber < userNumber) {

				session.save(voter);

			} else {

				replyMessage = new String(
						"1:Voter number has reached the limit for this district");

			}

			session.getTransaction().commit();
			session.close();

		} catch (JDBCException e) {

			// fail to insert the voter because the userName already
			// being used
			replyMessage = new String("1:Voter username (" + userName
					+ ") already exists");

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// reply to the client
		tran.replyData(replyMessage, port, host);

	}

	private void candidateRegistration(String userName, String lastName,
			String firstName, String address, String password, int port,
			InetAddress host) {

		Candidate candidate = new Candidate(userName, lastName, firstName,
				district, address, password);

		String replyMessage = new String("2:(" + userName
				+ ") has sucessfully registered");

		try {

			// -----get the restrict number for the voter
			int userNumber = Integer.parseInt(new Property()
					.loadProperties("totalCandidateNumberForOneDistrict"));

			// store the candidate information to the database
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			List<Candidate> candidatelist = session.createCriteria(
					Candidate.class).list();
			int existUserNumber = 0;
			for (int i = 0; i < candidatelist.size(); i++) {
				if (candidatelist.get(i).getDistrictName().compareTo(district) == 0) {
					existUserNumber++;
				}
			}

			if (existUserNumber < userNumber) {

				session.save(candidate);

			} else {

				replyMessage = new String(
						"1:Candidate number has reached the limit for this district");

			}

			session.getTransaction().commit();
			session.close();

		} catch (JDBCException e) {

			replyMessage = new String("1:Candidate username (" + userName
					+ ") already exists");

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tran.replyData(replyMessage, port, host);

	}

	private void voterVote(String voterUserName, String candidateUserName,
			int port, InetAddress host) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		// -----need to add a mutex lock here
		synchronized (lock1) {

			Candidate candidate = (Candidate) session.get(Candidate.class,
					candidateUserName);
			Voter voter = (Voter) session.get(Voter.class, voterUserName);

			if (voter.getCandidateName().isEmpty()) {

				candidate.setPolls(candidate.getPolls() + 1);
				session.update(candidate);
				voter.setCandidateName(candidateUserName);
				session.update(voter);
				tran.replyData("2:(" + voterUserName
						+ ") has sucessfully voted", port, host);

			} else {

				tran.replyData("1:Voter (" + voterUserName
						+ ") has already voted", port, host);

			}
		}
		session.getTransaction().commit();
		session.close();

	}

	private void login(String userName, String password, int port,
			InetAddress host) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Voter voter = (Voter) session.get(Voter.class, userName);

		if (voter != null) {

			if (password.compareTo(voter.getPassword()) == 0) {

				synchronized (activeUsers) {

					if (!checkExist(voter)) {

						int availableUserNum = 0;
						try {
							availableUserNum = Integer.parseInt(new Property()
									.loadProperties("activeUserForServer1"));
						} catch (NumberFormatException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (activeUsers.size() == availableUserNum) {

							tran.replyData(
									"1:Active users have reached the limit for this server",
									port, host);

						} else {

							if (voter.getDistrictName().compareTo(district) == 0) {

								Candidate candidate = (Candidate) session.get(
										Candidate.class,
										voter.getCandidateName());
								String voterString = "";
								if (!voter.getCandidateName().isEmpty()) {
									voterString = voter.getFirstName() + ":"
											+ voter.getLastName() + ":"
											+ voter.getAddress() + ":"
											+ candidate.getFirstName() + ":"
											+ candidate.getLastName();
								} else {

									voterString = voter.getFirstName() + ":"
											+ voter.getLastName() + ":"
											+ voter.getAddress() + ":1";

								}
								tran.replyData("2:" + voterString, port, host);
								activeUsers.add(voter);

							} else {

								tran.replyData("1:Voter (" + userName
										+ ") doesn't belong to this destrict",
										port, host);

							}

						}

					} else {

						tran.replyData("1:Voter (" + userName
								+ ") already login", port, host);

					}
				}
			} else {

				tran.replyData("1:Invalid Password", port, host);

			}

			session.getTransaction().commit();
			session.close();

		} else {

			tran.replyData("1:Can't find the voter username (" + userName
					+ ") , please register an account or login as a voter",
					port, host);
		}

	}

	private void getCandidateList(int port, InetAddress host) {

		// -----get the candidate list
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Candidate> candidates = session.createCriteria(Candidate.class)
				.list();
		String candidateData = "3:";
		for (int i = 0; i < candidates.size(); i++) {
			if (candidates.get(i).getDistrictName().compareTo(district) == 0) {
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
		}
		tran.replyData(candidateData, port, host);
		session.getTransaction().commit();
		session.close();

	}

	private void logout(String userName, int port, InetAddress host) {

		// ------only one user can logout at a time

		boolean success;
		synchronized (lock2) {

			success = removeActiveUser(userName);
		}

		if (success) {

			tran.replyData("2:" + userName + " has successfully logged out",
					port, host);

		} else {

			tran.replyData("1:Voter (" + userName + ") has already logged out",
					port, host);

		}

	}

	private boolean removeActiveUser(String userName) {

		for (int i = 0; i < activeUsers.size(); i++) {

			if (activeUsers.get(i).getUserName().compareTo(userName) == 0) {

				activeUsers.remove(i);
				return true;

			}

		}

		return false;

	}

	private boolean checkExist(User user) {

		for (int i = 0; i < activeUsers.size(); i++) {

			if (user.getUserName().compareTo(activeUsers.get(i).getUserName()) == 0) {

				// the user already login
				return true;

			}

		}

		return false;

	}
}
