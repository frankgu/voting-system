package com.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.functions.Transmission;

public class ClientRegister {

	protected Shell shlRegistration;
	private Text text;
	private Text text_4;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Label lblFirstName;
	private Label lblPassword;
	private Label lblUserName;
	private Text text_5;
	private Label lblUserName_1;
	private Label lblAddress;
	private Button btnReg;
	private Button btnCancel;
	private String ip;
	private int    port;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ClientRegister window = new ClientRegister();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ClientRegister(){
		ip   = "null";
		port = 0;
	}
	
	public ClientRegister(String i, int p){
		ip   = i;
		port = p;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlRegistration.open();
		shlRegistration.layout();
		while (!shlRegistration.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlRegistration = new Shell();
		shlRegistration.setSize(450, 300);
		shlRegistration.setText("Registration");
		
		text_5 = new Text(shlRegistration, SWT.BORDER);
		text_5.setBounds(210, 50, 91, 19);
		
		text = new Text(shlRegistration, SWT.BORDER);
		text.setBounds(210, 75, 91, 19);
		
		text_1 = new Text(shlRegistration, SWT.BORDER);
		text_1.setBounds(210, 100, 91, 19);
		
		text_2 = new Text(shlRegistration, SWT.BORDER);
		text_2.setBounds(210, 125, 91, 19);
		
		text_3 = new Text(shlRegistration, SWT.BORDER);
		text_3.setBounds(210, 150, 91, 19);
		
		text_4 = new Text(shlRegistration, SWT.BORDER);
		text_4.setBounds(120, 178, 267, 19);
		
		Label lblLast = new Label(shlRegistration, SWT.NONE);
		lblLast.setAlignment(SWT.RIGHT);
		lblLast.setBounds(130, 153, 74, 14);
		lblLast.setText("Last Name");
		
		lblFirstName = new Label(shlRegistration, SWT.NONE);
		lblFirstName.setAlignment(SWT.RIGHT);
		lblFirstName.setBounds(120, 128, 84, 14);
		lblFirstName.setText("First Name");
		
		lblPassword = new Label(shlRegistration, SWT.NONE);
		lblPassword.setAlignment(SWT.RIGHT);
		lblPassword.setBounds(98, 103, 106, 14);
		lblPassword.setText("Repeat Password");
		
		lblUserName = new Label(shlRegistration, SWT.NONE);
		lblUserName.setAlignment(SWT.RIGHT);
		lblUserName.setBounds(120, 78, 84, 14);
		lblUserName.setText("Password");
		
		lblUserName_1 = new Label(shlRegistration, SWT.NONE);
		lblUserName_1.setAlignment(SWT.RIGHT);
		lblUserName_1.setBounds(120, 53, 84, 14);
		lblUserName_1.setText("User Name");
		
		lblAddress = new Label(shlRegistration, SWT.NONE);
		lblAddress.setAlignment(SWT.RIGHT);
		lblAddress.setBounds(40, 181, 74, 14);
		lblAddress.setText("Address");
		
		btnReg = new Button(shlRegistration, SWT.NONE);
		btnReg.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//reg btn todo
				String pwd     = text.getText();
				String pwd_rep = text_1.getText();
				String fn      = text_2.getText();
				String ln      = text_3.getText();
				String addr    = text_4.getText();
				String usr     = text_5.getText();
				
				if (usr.length() == 0 
					|| pwd.length() == 0
					|| pwd_rep.length() == 0
					|| fn.length() == 0
					|| ln.length() == 0
					|| addr.length() == 0) {
					int style = SWT.ICON_ERROR;
					MessageBox noInfo = new MessageBox(shlRegistration, style);
					noInfo.setMessage("Invalid registration information!");
					noInfo.open();
				} else if(!pwd.equals(pwd_rep)){
					int style = SWT.ICON_ERROR;
					MessageBox noInfo = new MessageBox(shlRegistration, style);
					noInfo.setMessage("Two passwords do not match!");
					noInfo.open();
				}else {
					int style = SWT.ICON_INFORMATION;
					MessageBox noInfo = new MessageBox(shlRegistration, style);
					try {
						DatagramSocket aSocket = new DatagramSocket();
						Transmission tran = new Transmission(aSocket);
						InetAddress host = InetAddress.getByName("127.0.0.1");
						String rtnMsg;
						//rtnMsg = tran.sendData("1:1:" + usr + ":" + ln + ":" + fn + ":" + addr + ":" + pwd, port, host);
						rtnMsg = tran.sendData("1:1:usrName:ln:fn:addr:pwd", port, host);
						noInfo.setMessage(rtnMsg.split(":")[1]);
						shlRegistration.setEnabled(false);
						noInfo.open();
						shlRegistration.setEnabled(true);
						if(rtnMsg.split(":")[0].equals("2")){
							shlRegistration.dispose();//end of register window, registered successfully
						}
					} catch (SocketException e1) {
						e1.printStackTrace();
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnReg.setBounds(109, 220, 95, 28);
		btnReg.setText("Register");
		
		btnCancel = new Button(shlRegistration, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shlRegistration.dispose();
			}
		});
		btnCancel.setBounds(262, 220, 95, 28);
		btnCancel.setText("Cancel");

	}
}
