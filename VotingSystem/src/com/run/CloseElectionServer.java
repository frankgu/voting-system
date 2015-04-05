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

public class CloseElectionServer {
	
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
    
	protected Shell shlCloseElection;
    public String serverIP, serverPort, serverName;
    //public static final int MAX_DISTRICT_SERVER = 10;
    public ArrayList<serverInfo> serverList = new ArrayList<serverInfo>();
    public String ip;
	public int port;
	public String name;
	public boolean quit;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CloseElectionServer window = new CloseElectionServer();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CloseElectionServer(){
		quit = false;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlCloseElection.open();
		shlCloseElection.layout();
		while (!shlCloseElection.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlCloseElection = new Shell(SWT.CLOSE | SWT.TITLE);
		shlCloseElection.setSize(450, 276);
		shlCloseElection.setText("Close District Election");
		
		final Combo combo = new Combo(shlCloseElection, SWT.READ_ONLY);
		combo.setBounds(196, 82, 206, 28);
		
		URL servers;
		try {
			servers = new URL("http://go.joyclick.org/sl.txt");
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
		
		Label label = new Label(shlCloseElection, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setText("Select District:");
		label.setBounds(55, 88, 132, 20);
		
		Button btnOk = new Button(shlCloseElection, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( combo.getText().length() == 0){
					int style = SWT.ICON_ERROR;
					MessageBox noInfo = new MessageBox(shlCloseElection, style);
					noInfo.setMessage("Please select a district!");
					shlCloseElection.setEnabled(false);
					noInfo.open();
					shlCloseElection.setEnabled(true);
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
					
				
						int style = SWT.ICON_INFORMATION;
						MessageBox noInfo = new MessageBox(shlCloseElection, style);
						try {
							DatagramSocket aSocket = new DatagramSocket();
							Transmission tran = new Transmission(aSocket);
							InetAddress host = InetAddress.getByName(ip);
							String rtnMsg;
							rtnMsg = tran.sendData("0:", port, host);
							if(!rtnMsg.equals("null")){
								noInfo.setMessage(rtnMsg.split(":")[1]);
								shlCloseElection.setEnabled(false);
								noInfo.open();
								shlCloseElection.setEnabled(true);
							}else{//server no response
								noInfo.setMessage("Server no response!");
								shlCloseElection.setEnabled(false);
								noInfo.open();
								shlCloseElection.setEnabled(true);
							}
						} catch (SocketException e1) {
							e1.printStackTrace();
						} catch (UnknownHostException e1) {
							e1.printStackTrace();
						}
					
				}	
			}
		});
		btnOk.setBounds(147, 134, 164, 28);
		btnOk.setText("Close Election");
		shlCloseElection.setDefaultButton(btnOk);
		
		shlCloseElection.addListener(SWT.Close, new Listener() {
		      @Override
			public void handleEvent(Event event) {
		        int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		        MessageBox messageBox = new MessageBox(shlCloseElection, style);
		        messageBox.setText("Exit");
		        messageBox.setMessage("Are You Sure to Exit?");
		        event.doit = messageBox.open() == SWT.YES;
		        if(event.doit)
		        	quit = true;
		      }
		    });

	}
}
