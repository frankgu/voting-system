package com.client;

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

	/**
	 * Launch the application.
	 * @param args
	 */
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
	}
	
	protected void createContents() {
		shlLogin = new Shell();
		shlLogin.setSize(450, 300);
		shlLogin.setText("Login");
		
		Combo combo = new Combo(shlLogin, SWT.NONE);
		combo.setBounds(184, 71, 117, 22);
		
		Label lblSelectDistrict = new Label(shlLogin, SWT.NONE);
		lblSelectDistrict.setBounds(90, 75, 97, 14);
		lblSelectDistrict.setText("Select District:");
		
		text = new Text(shlLogin, SWT.BORDER);
		text.setBounds(184, 138, 86, 19);
		
		Label lblUsername = new Label(shlLogin, SWT.NONE);
		lblUsername.setBounds(113, 141, 65, 16);
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

	}
}
