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


//login panel
public class ClientLogin {
    public String str = "init";
	protected Shell shlLogin;
	private Text text;
	private Text text_1;
	private String abc = "";
	private String abc2 = "";
	/**
	 * Launch the application.
	 * @param args
	 */
    
    public ClientLogin(){
    	
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
		str = "edited";
		System.out.println("login btn handler");
		abc = "asdf";
		abc2 = "sdfdfe";
	}
	
	protected void createContents() {
		shlLogin = new Shell();
		shlLogin.setSize(450, 300);
		shlLogin.setText("Login");
		
		String abc = "";
		//Combo combo = new Combo(shlLogin, SWT.READ_ONLY);
		//combo.setBounds(184, 71, 117, 22);
		
		
		/*File newFile = new File("/Users/Shawn/Documents/server1List.txt");
		try(BufferedReader br = new BufferedReader(new FileReader(newFile))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        combo.add(line.split(":")[0]);// process the line.
		    }
		    // line is not visible here.
		}
		//read server1 list from txt file
        catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		
		
		Label lblSelectDistrict = new Label(shlLogin, SWT.NONE);
		lblSelectDistrict.setBounds(90, 75, 97, 14);
		lblSelectDistrict.setText("Select District:");
		
		text = new Text(shlLogin, SWT.BORDER);
		text.setBounds(117, 138, 86, 19);
		
		Label lblUsername = new Label(shlLogin, SWT.NONE);
		lblUsername.setBounds(46, 141, 65, 16);
		lblUsername.setText("Username:");
		
		Button btnRegister = new Button(shlLogin, SWT.NONE);
		btnRegister.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//to do
			}
		});
		btnRegister.setBounds(90, 186, 95, 28);
		btnRegister.setText("Register");
		
		Button btnLogin = new Button(shlLogin, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//to do
				loginHandler();
				
			}
		});
		btnLogin.setBounds(224, 186, 95, 28);
		btnLogin.setText("Login");
		
		Label lblPassword = new Label(shlLogin, SWT.NONE);
		lblPassword.setBounds(216, 141, 70, 22);
		lblPassword.setText("Password:");
		
		text_1 = new Text(shlLogin, SWT.PASSWORD | SWT.BORDER);
		text_1.setBounds(284, 138, 86, 19);

	}
}
