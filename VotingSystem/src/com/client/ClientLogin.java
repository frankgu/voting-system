package com.client;

import java.io.*;
import java.net.*;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

import com.functions.Transmission;
import com.object.User;

//login panel
public class ClientLogin {
	protected Shell shlLogin;
	private Text text;
	private Text text_1;
	public  String district;
	public String usr;
	public String pwd;
	private String servName;
	private String ip;
	private int port;
	public  String usrInfo;
	public boolean quit;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */

	public ClientLogin() {
		district = "null";
		usr      = "null";
		pwd      = "null";
		servName = "null";
		ip       = "null";
		port     = 0;
		usrInfo  = "";
		quit     = false;
	}

	public ClientLogin(String d, String n, String i, int p) {
		district = d;
		usr = "null";
		pwd = "null";
		servName = n;
		ip = i;
		port = p;
		usrInfo  = "";
		quit     = false;
	}

	public static void main(String[] args) {
		try {
			ClientLogin window = new ClientLogin();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlLogin.open();
		shlLogin.layout();
		while (!shlLogin.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */

	protected void createContents() {
		shlLogin = new Shell(SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
		shlLogin.setSize(450, 300);
		shlLogin.setText("Login");

		text = new Text(shlLogin, SWT.BORDER);
		text.setBounds(117, 138, 86, 19);

		Label lblUsername = new Label(shlLogin, SWT.NONE);
		lblUsername.setBounds(46, 141, 65, 16);
		lblUsername.setText("Username:");

		Button btnRegister = new Button(shlLogin, SWT.NONE);
		btnRegister.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// to do register
				// 1.pop register window
				// 2.disable login window
				// 3.back from register window, enable login window
				shlLogin.setEnabled(false);
				try {
					ClientRegister reg = new ClientRegister(ip,port);
					reg.open();
				} catch (Exception ev) {
					ev.printStackTrace();
				}
				shlLogin.setEnabled(true);
			}
		});
		btnRegister.setBounds(104, 186, 95, 28);
		btnRegister.setText("Register");

		Button btnLogin = new Button(shlLogin, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// to do login
				usr = text.getText();
				pwd = text_1.getText();

				if (usr.length() == 0 || pwd.length() == 0) {
					int style = SWT.ICON_ERROR;
					MessageBox noInfo = new MessageBox(shlLogin, style);
					noInfo.setMessage("Invalid login information!");
					shlLogin.setEnabled(false);
					noInfo.open();
					shlLogin.setEnabled(true);
				} else {
					int style = SWT.ICON_INFORMATION;
					MessageBox noInfo = new MessageBox(shlLogin, style);
					try {
						DatagramSocket aSocket = new DatagramSocket();
						Transmission tran = new Transmission(aSocket);
						System.out.println(ip);
						InetAddress host = InetAddress.getByName(ip);
						String rtnMsg = tran.sendData("3:" + usr + ":" + pwd, port, host);
						if(!rtnMsg.equals("null")){
							if(rtnMsg.split(":")[0].equals("2")){
								noInfo.setMessage("Logged in successfully!");
								shlLogin.setEnabled(false);
								noInfo.open();
								shlLogin.setEnabled(true);
								usrInfo = rtnMsg.substring(2);
								shlLogin.dispose();//end of login window, logged successfully
							}else{
								noInfo.setMessage(rtnMsg.split(":")[1]);
								shlLogin.setEnabled(false);
								noInfo.open();
								shlLogin.setEnabled(true);
							}
						}else{//no server response
							noInfo.setMessage("Server no response!");
							shlLogin.setEnabled(false);
							noInfo.open();
							shlLogin.setEnabled(true);
						}
					} catch (SocketException e1) {
						e1.printStackTrace();
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnLogin.setBounds(244, 186, 95, 28);
		btnLogin.setText("Login");
		shlLogin.setDefaultButton(btnLogin);

		Label lblPassword = new Label(shlLogin, SWT.NONE);
		lblPassword.setBounds(216, 141, 70, 22);
		lblPassword.setText("Password:");

		text_1 = new Text(shlLogin, SWT.PASSWORD | SWT.BORDER);
		text_1.setBounds(284, 138, 86, 19);

		Label lblDistrictElection = new Label(shlLogin, SWT.NONE);
		lblDistrictElection.setFont(SWTResourceManager.getFont(
				".Helvetica Neue DeskInterface", 13, SWT.NORMAL));
		lblDistrictElection.setBounds(253, 69, 166, 28);
		lblDistrictElection.setText("2015 Election");

		Label lblDistrict = new Label(shlLogin, SWT.CENTER);
		lblDistrict.setFont(SWTResourceManager.getFont(
				".Helvetica Neue DeskInterface", 16, SWT.BOLD));
		lblDistrict.setAlignment(SWT.CENTER);
		lblDistrict.setBounds(46, 35, 373, 62);
		lblDistrict.setText(district);

		shlLogin.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox(shlLogin, style);
				messageBox.setText("Exit");
				messageBox.setMessage("Are You Sure to Exit?");
				event.doit = messageBox.open() == SWT.YES;
				if(event.doit)
		        	quit = true;
			}
		});

	}
}
