package com.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;

import com.functions.Transmission;
import com.object.Voter;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class ClientVote {

	protected Shell shlVoting;
	private String servName;
	private String ip;
	private int port;
	private Voter voter;
	private DatagramSocket aSocket;
	private Transmission tran;
	private InetAddress host;

	/**
	 * Launch the application.
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
	
	public ClientVote(String n, String i, int p, String u, String pwd, String fn, String ln, String addr){
		servName = n;
		ip       = i;
		port     = p;
		voter      = new Voter(u, ln, fn, n, addr, pwd);
		try {
			aSocket = new DatagramSocket();
			tran = new Transmission(aSocket);
			host = InetAddress.getByName(ip);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ClientVote(){
		servName = "null";
		ip       = "null";
		port     = 0;
		voter    = new Voter();
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

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlVoting = new Shell();
		shlVoting.setSize(593, 434);
		shlVoting.setText("Voting");
		
		Label lblUsrName = new Label(shlVoting, SWT.NONE);
		lblUsrName.setBounds(37, 84, 60, 14);
		lblUsrName.setText(voter.getFirstName()+" "+voter.getLastName());
		
		Label lblStatus = new Label(shlVoting, SWT.NONE);
		lblStatus.setBounds(37, 116, 60, 14);
		lblStatus.setText("Status:");
		
		Label lblVoted = new Label(shlVoting, SWT.NONE);
		lblVoted.setBounds(82, 116, 86, 14);
		lblVoted.setText("voted/not voted green red");
		
		Label lblCanditate = new Label(shlVoting, SWT.NONE);
		lblCanditate.setBounds(199, 150, 60, 14);
		lblCanditate.setText("Canditates:");
		
		Label label = new Label(shlVoting, SWT.NONE);
		label.setText("2015 Election");
		label.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 13, SWT.NORMAL));
		label.setBounds(315, 63, 86, 28);
		
		Label label_1 = new Label(shlVoting, SWT.CENTER);
		label_1.setText(voter.getDistrictName());
		label_1.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 16, SWT.BOLD));
		label_1.setAlignment(SWT.CENTER);
		label_1.setBounds(112, 31, 337, 74);
		
		Label lblVotedFor = new Label(shlVoting, SWT.NONE);
		lblVotedFor.setBounds(37, 154, 60, 14);
		lblVotedFor.setText("Voted For:");
		
		Label lblVotedName = new Label(shlVoting, SWT.NONE);
		lblVotedName.setBounds(60, 175, 108, 37);
		lblVotedName.setText("Voted Name");
		
		List list = new List(shlVoting, SWT.BORDER);
		list.setBounds(199, 170, 140, 151);
		
		Button btnVote = new Button(shlVoting, SWT.NONE);
		btnVote.setBounds(221, 339, 95, 28);
		btnVote.setText("Vote");
		
		Button btnLogOff = new Button(shlVoting, SWT.NONE);
		btnLogOff.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//logoff
				String rtnMsg;
				rtnMsg = tran.sendData("5:"+voter.getUserName(), port, host);
				shlVoting.dispose();
			}
		});
		btnLogOff.setBounds(488, 374, 95, 28);
		btnLogOff.setText("Log Off");
		
		shlVoting.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox(shlVoting, style);
				messageBox.setText("Exit");
				messageBox.setMessage("Are You Sure to Exit?");
				event.doit = messageBox.open() == SWT.YES;
				String rtnMsg;
				rtnMsg = tran.sendData("5:"+voter.getUserName(), port, host);
				int style2 = SWT.ICON_INFORMATION;
				MessageBox noInfo = new MessageBox(shlVoting, style2);
				noInfo.setMessage(rtnMsg.split(":")[1]);
				shlVoting.setEnabled(false);
				noInfo.open();
				shlVoting.setEnabled(true);
                if(!rtnMsg.split(":")[0].equals("2")){
					//log out fail
				}
			}
		});

	}
}
