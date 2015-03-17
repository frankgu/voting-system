package com.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Text;

import com.functions.Transmission;

public class CandidateReg {
	
    public class serverInfo{
    	public String ip;
    	public int port;
    	public String name;
    	
    	public serverInfo(){
    		ip   = "";
    		port = 0;
    		name = "";
    	}
    	
    	public serverInfo(String n, String i, int p){
    		ip = i;
    		port = p;
    		name = n;
    	}
    }
    
	protected Shell shlCandidateReg;
    public String serverIP, serverPort, serverName;
    //public static final int MAX_DISTRICT_SERVER = 10;
    public ArrayList<serverInfo> serverList = new ArrayList<serverInfo>();
    public String ip;
	public int port;
	public String name;
	public boolean quit;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CandidateReg window = new CandidateReg();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CandidateReg(){
		quit = false;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlCandidateReg.open();
		shlCandidateReg.layout();
		while (!shlCandidateReg.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlCandidateReg = new Shell(SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
		shlCandidateReg.setSize(450, 276);
		shlCandidateReg.setText("Candidate Registration");
		
		final Combo combo = new Combo(shlCandidateReg, SWT.READ_ONLY);
		combo.setBounds(203, 38, 138, 22);
		
		URL servers;
		try {
			servers = new URL("https://raw.githubusercontent.com/frankgu/voting-system/master/VotingSystem/server1List.txt?token=AFlT_Jp7ANM1-WVg1W0z19o2HND1OT6Lks5VDbTKwA%3D%3D");
			BufferedReader in;
			in = new BufferedReader(
					new InputStreamReader(servers.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null){
				combo.add(inputLine.split(":")[0]);
				serverInfo srv = new serverInfo(inputLine.split(":")[0], inputLine.split(":")[1], Integer.parseInt(inputLine.split(":")[2]));
				serverList.add(srv);
				combo.select(0);
			}
			
			in.close();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*combo.addSelectionListener(new SelectionListener() {
		      public void widgetSelected(SelectionEvent e) {
		        System.out.println(combo.getText());
		      }

		      public void widgetDefaultSelected(SelectionEvent e) {
		        System.out.println(combo.getText());
		      }
		    });*/
		
		Label label = new Label(shlCandidateReg, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setText("Select District:");
		label.setBounds(100, 41, 97, 14);
		
		Button btnOk = new Button(shlCandidateReg, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( combo.getText().length() == 0){
					int style = SWT.ICON_ERROR;
					MessageBox noInfo = new MessageBox(shlCandidateReg, style);
					noInfo.setMessage("Please select a district!");
					shlCandidateReg.setEnabled(false);
					noInfo.open();
					shlCandidateReg.setEnabled(true);
				}else{
					int n = 0;
					while(serverList.get(n) != null){
						if( combo.getText().equals(serverList.get(n).name) ){
							name = serverList.get(n).name;
							ip   = serverList.get(n).ip;
							port = serverList.get(n).port;
							break;
						}
						n++;
					}
					
					String usr     = text.getText();
					String fn      = text_1.getText();
					String ln      = text_2.getText();
					String addr     = text_3.getText();
					
					if (usr.length() == 0 
						|| fn.length() == 0
						|| ln.length() == 0
						|| addr.length() == 0) {
						int style = SWT.ICON_ERROR;
						MessageBox noInfo = new MessageBox(shlCandidateReg, style);
						noInfo.setMessage("Invalid registration information!");
						noInfo.open();
					} else {
						int style = SWT.ICON_INFORMATION;
						MessageBox noInfo = new MessageBox(shlCandidateReg, style);
						try {
							DatagramSocket aSocket = new DatagramSocket();
							Transmission tran = new Transmission(aSocket);
							InetAddress host = InetAddress.getByName(ip);
							String rtnMsg;
							rtnMsg = tran.sendData("1:2:" + usr + ":" + ln + ":" + fn + ":" + addr, port, host);
							if(!rtnMsg.equals("null")){
								noInfo.setMessage(rtnMsg.split(":")[1]);
								shlCandidateReg.setEnabled(false);
								noInfo.open();
								shlCandidateReg.setEnabled(true);
							}else{//server no response
								noInfo.setMessage("Server no response!");
								shlCandidateReg.setEnabled(false);
								noInfo.open();
								shlCandidateReg.setEnabled(true);
							}
						} catch (SocketException e1) {
							e1.printStackTrace();
						} catch (UnknownHostException e1) {
							e1.printStackTrace();
						}
					}
					//1:2:usrName:ln:fn:addr
					//shlCandidateReg.dispose();
				}	
			}
		});
		btnOk.setBounds(168, 196, 95, 28);
		btnOk.setText("Register");
		shlCandidateReg.setDefaultButton(btnOk);
		
		Label lblUserName = new Label(shlCandidateReg, SWT.NONE);
		lblUserName.setAlignment(SWT.RIGHT);
		lblUserName.setBounds(104, 69, 106, 22);
		lblUserName.setText("User Name:");
		
		text = new Text(shlCandidateReg, SWT.BORDER);
		text.setBounds(210, 66, 97, 19);
		
		Label lblCandidateFirstName = new Label(shlCandidateReg, SWT.NONE);
		lblCandidateFirstName.setText("Candidate First Name:");
		lblCandidateFirstName.setAlignment(SWT.RIGHT);
		lblCandidateFirstName.setBounds(52, 100, 158, 22);
		
		Label lblCandidateLastName = new Label(shlCandidateReg, SWT.NONE);
		lblCandidateLastName.setText("Candidate Last Name:");
		lblCandidateLastName.setAlignment(SWT.RIGHT);
		lblCandidateLastName.setBounds(52, 131, 158, 22);
		
		text_1 = new Text(shlCandidateReg, SWT.BORDER);
		text_1.setBounds(210, 97, 97, 19);
		
		text_2 = new Text(shlCandidateReg, SWT.BORDER);
		text_2.setBounds(210, 128, 97, 19);
		
		text_3 = new Text(shlCandidateReg, SWT.BORDER);
		text_3.setBounds(210, 158, 186, 19);
		
		Label lblAddress = new Label(shlCandidateReg, SWT.NONE);
		lblAddress.setText("Address:");
		lblAddress.setAlignment(SWT.RIGHT);
		lblAddress.setBounds(52, 161, 158, 22);
		
		shlCandidateReg.addListener(SWT.Close, new Listener() {
		      @Override
			public void handleEvent(Event event) {
		        int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		        MessageBox messageBox = new MessageBox(shlCandidateReg, style);
		        messageBox.setText("Exit");
		        messageBox.setMessage("Are You Sure to Exit?");
		        event.doit = messageBox.open() == SWT.YES;
		        if(event.doit)
		        	quit = true;
		      }
		    });

	}
}
