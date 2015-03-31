package com.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;

import com.functions.Transmission;
import com.object.Voter;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;

public class ClientVote {

	protected Shell shlVoting;
	// private String servName;
	private String ip;
	private int port;
	private Voter voter;
	private DatagramSocket aSocket;
	private Transmission tran;
	private InetAddress host;
	private ArrayList<Candidate> candList;
	private List list;
	private Thread upd;
	private boolean stopDaemon;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ClientVote window = new ClientVote();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class Candidate {
		String usr;
		String fn;
		String ln;

		public Candidate(String u, String f, String l) {
			usr = u;
			fn = f;
			ln = l;
		}
	}
	
	

	public ClientVote(String n, String i, int p, String u, String pwd,
			String fn, String ln, String addr) {
		// servName = n;
		ip = i;
		port = p;
		voter = new Voter(u, ln, fn, n, addr, pwd);
		candList = new ArrayList<Candidate>();
		stopDaemon = false;
		// updCandList();
		//
		try {
			aSocket = new DatagramSocket();
			tran = new Transmission(aSocket);
			host = InetAddress.getByName(ip);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		upd = new Thread(new Runnable() {
			public void run() {
				while (!stopDaemon) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							updCandList();
						}
					});
				}
			}
		});
	}

	public ClientVote() {
		// servName = "null";
		ip = "null";
		port = 0;
		voter = new Voter();
		stopDaemon = false;
		candList = new ArrayList<Candidate>();
		upd = new Thread(new Runnable() {
			public void run() {
				while (!stopDaemon) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							updCandList();
						}
					});
				}
			}
		});
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlVoting.open();
		shlVoting.layout();
		while (!shlVoting.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void updCandList() {
		String candis = tran.sendData("4:", port, host);
		int numOfCand = (candis.split(":").length - 1) / 3;
		System.out.println(candis);
		list.removeAll();
		candList.clear();
		if (candis.split(":").length == 4) {// only one candidate
			candList.add(new Candidate(candis.split(":")[1],
					candis.split(":")[2], candis.split(":")[3]));
		} else {
			for (int i = 0; i < numOfCand; ++i) {
				// System.out.println("\ni = "+i);
				// System.out.println(candis.split(":")[3*i+1]+
				// candis.split(":")[3*i+2]+ candis.split(":")[3*i+3]);
				candList.add(new Candidate(candis.split(":")[3 * i + 1], candis
						.split(":")[3 * i + 2], candis.split(":")[3 * i + 3]));
			}
		}
		if (!candList.isEmpty()) {
			for (int i = 0; i < numOfCand; i++) {
				list.add(candList.get(i).fn + " " + candList.get(i).ln);
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlVoting = new Shell(SWT.CLOSE | SWT.TITLE);
		shlVoting.setSize(593, 434);
		shlVoting.setText("2015 Carleton Voting");

		Label lblUsrName = new Label(shlVoting, SWT.NONE);
		lblUsrName.setBounds(37, 75, 204, 28);
		lblUsrName.setText(voter.getFirstName() + " " + voter.getLastName());

		Label lblStatus = new Label(shlVoting, SWT.NONE);
		lblStatus.setAlignment(SWT.RIGHT);
		lblStatus.setBounds(10, 116, 66, 14);
		lblStatus.setText("Status:");

		final Label lblVotedFor = new Label(shlVoting, SWT.NONE);
		lblVotedFor.setBounds(37, 154, 60, 14);
		lblVotedFor.setText("Voted For:");

		final Label lblVotedName = new Label(shlVoting, SWT.NONE);
		lblVotedName.setBounds(63, 174, 181, 37);
		lblVotedName.setText("Voted Name");

		Label lblCanditate = new Label(shlVoting, SWT.NONE);
		lblCanditate.setBounds(244, 143, 86, 14);
		lblCanditate.setText("Candidates:");

		Label label_1 = new Label(shlVoting, SWT.CENTER);
		label_1.setText(voter.getDistrictName());
		label_1.setFont(SWTResourceManager.getFont(
				".Helvetica Neue DeskInterface", 16, SWT.BOLD));
		label_1.setAlignment(SWT.CENTER);
		label_1.setBounds(97, 23, 385, 74);

		list = new List(shlVoting, SWT.BORDER);
		list.setBounds(247, 164, 140, 151);
		updCandList();

		final Button btnVote = new Button(shlVoting, SWT.NONE);
		btnVote.setBounds(278, 333, 95, 28);
		btnVote.setText("Vote");

		final Label lblVoted = new Label(shlVoting, SWT.NONE);
		lblVoted.setBounds(82, 116, 95, 22);
		String votedInfo;
		votedInfo = tran.sendData("6:" + voter.getUserName(), port, host);
		if (votedInfo.split(":")[1].equals("2")) {// voted
			lblVoted.setText("Voted");
			lblVoted.setForeground(new Color(Display.getCurrent(), 0, 255, 0));
			lblVotedName.setText(votedInfo.split(":")[2] + " "
					+ votedInfo.split(":")[3]);
			lblVotedName.setText(votedInfo.split(":")[2] + " "
					+ votedInfo.split(":")[3]);
			btnVote.setGrayed(true);
			btnVote.setEnabled(false);
		} else {// not voted
			lblVoted.setText("Not Voted");
			lblVoted.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
			lblVotedName.setText("none");
			lblVotedName.setVisible(false);
			lblVotedFor.setVisible(false);
		}

		btnVote.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(candList.get(list.getSelectionIndex()).fn
						+ " " + candList.get(list.getSelectionIndex()).ln);
				String rtnMsg;
				rtnMsg = tran.sendData("2:" + voter.getUserName() + ":"
						+ candList.get(list.getSelectionIndex()).usr, port,
						host);
				int style2 = SWT.ICON_INFORMATION;
				MessageBox noInfo = new MessageBox(shlVoting, style2);
				noInfo.setMessage(rtnMsg.split(":")[1]);
				shlVoting.setEnabled(false);
				noInfo.open();
				shlVoting.setEnabled(true);
				if (rtnMsg.split(":")[0].equals("2")) {// vote succeeded
					String votedInfo2 = tran.sendData(
							"6:" + voter.getUserName(), port, host);
					lblVotedFor.setVisible(true);
					lblVotedName.setVisible(true);
					lblVoted.setText("Voted");
					lblVoted.setForeground(new Color(Display.getCurrent(), 0,
							255, 0));
					lblVotedName.setText(votedInfo2.split(":")[2] + " "
							+ votedInfo2.split(":")[3]);
					btnVote.setGrayed(true);
					btnVote.setEnabled(false);
				} else {// vote failed
						// currently do nothing(info messagebox already
						// displayed)
				}
			}
		});

		Button btnLogOff = new Button(shlVoting, SWT.NONE);
		btnLogOff.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// logoff
				String rtnMsg;
				rtnMsg = tran.sendData("5:" + voter.getUserName(), port, host);
				int style2 = SWT.ICON_INFORMATION;
				MessageBox noInfo = new MessageBox(shlVoting, style2);
				noInfo.setMessage(rtnMsg.split(":")[1]);
				shlVoting.setEnabled(false);
				noInfo.open();
				shlVoting.setEnabled(true);
				shlVoting.dispose();
				upd.stop();
				stopDaemon = true;
				
				if (!rtnMsg.split(":")[0].equals("2")) {
					// log out fail
				}
			}
		});
		btnLogOff.setBounds(488, 374, 95, 28);
		btnLogOff.setText("Log Out");

		shlVoting.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {
				String rtnMsg;
				rtnMsg = tran.sendData("5:" + voter.getUserName(), port, host);
				int style2 = SWT.ICON_INFORMATION;
				MessageBox noInfo = new MessageBox(shlVoting, style2);
				noInfo.setMessage(rtnMsg.split(":")[1]);
				shlVoting.setEnabled(false);
				noInfo.open();
				shlVoting.setEnabled(true);
				upd.stop();
				stopDaemon = true;
				
				if (!rtnMsg.split(":")[0].equals("2")) {
					// log out fail
				}
			}
		});

		
		upd.start();
	}
}
