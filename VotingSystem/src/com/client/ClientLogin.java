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
	public String district;
	private String usr;
	private String pwd;
	private User user;
	private String servName;
	private String ip;
	private int port;
	
	
	/**
	 * Launch the application.
	 * @param args
	 */
    
    public ClientLogin(){
    	district = "null";
    	usr      = "null";
    	pwd      = "null";
    	user     = new User();
    	servName = "null";
    	ip       = "null";
    	port     = 0;
    }
    
    public ClientLogin(String d, String n, String i, int p){
    	district = d;
    	usr      = "null";
    	pwd      = "null";
    	user     = new User();
    	servName = n;
    	ip       = i;
    	port     = p;
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
		shlLogin = new Shell();
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
				//to do register
			}
		});
		btnRegister.setBounds(104, 186, 95, 28);
		btnRegister.setText("Register");
		
		Button btnLogin = new Button(shlLogin, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//to do login
				usr = text.getText();
				pwd = text_1.getText();
				

				if(usr.length() == 0 || pwd.length() == 0){
					int style = SWT.ICON_ERROR;
					MessageBox noInfo = new MessageBox(shlLogin, style);
			        noInfo.setMessage("Invalid login information!");
			        noInfo.open();
				}else{
					int style = SWT.ICON_INFORMATION;
					MessageBox noInfo = new MessageBox(shlLogin, style);
					try {
				    DatagramSocket aSocket = new DatagramSocket();
					Transmission tran = new Transmission(aSocket);
					InetAddress host = InetAddress.getByName("127.0.0.1");
					
						noInfo.setMessage(tran.sendData(
								"3:"+usr+":"+pwd, port,
								host));
						shlLogin.setEnabled(false);
				        noInfo.open();
						shlLogin.setEnabled(true);
					}catch (SocketException e1) {
						e1.printStackTrace();
					}catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnLogin.setBounds(244, 186, 95, 28);
		btnLogin.setText("Login");
		
		Label lblPassword = new Label(shlLogin, SWT.NONE);
		lblPassword.setBounds(216, 141, 70, 22);
		lblPassword.setText("Password:");
		
		text_1 = new Text(shlLogin, SWT.PASSWORD | SWT.BORDER);
		text_1.setBounds(284, 138, 86, 19);
		
		Label lblDistrictElection = new Label(shlLogin, SWT.NONE);
		lblDistrictElection.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 13, SWT.NORMAL));
		lblDistrictElection.setBounds(253, 69, 86, 28);
		lblDistrictElection.setText("2015 Election");
		
		Label lblDistrict = new Label(shlLogin, SWT.CENTER);
		lblDistrict.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 16, SWT.BOLD));
		lblDistrict.setAlignment(SWT.CENTER);
		lblDistrict.setBounds(46, 35, 337, 62);
		lblDistrict.setText(district);
		
		shlLogin.addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event) {
		        int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		        MessageBox messageBox = new MessageBox(shlLogin, style);
		        messageBox.setText("Exit");
		        messageBox.setMessage("Are You Sure to Exit?");
		        event.doit = messageBox.open() == SWT.YES;
		      }
		    });

	}
}
