package com.client;

import java.io.*;
import java.net.*;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;


//login panel
public class ClientLogin {
	protected Shell shlLogin;
	private Text text;
	private Text text_1;
	public String district;
	/**
	 * Launch the application.
	 * @param args
	 */
    
    public ClientLogin(){
    	district = "null";
    }
    
    public ClientLogin(String d){
    	district = d;
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
	public void loginHandler(){
		System.out.println("login btn handler");
	}
	
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
				loginHandler();
				
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
		//lblDistrict.setSize(14);
		lblDistrict.setText(district);

	}
}
