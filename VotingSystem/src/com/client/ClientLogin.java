package com.client;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;


//login panel
public class ClientLogin {

	protected Shell shlLogin;

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
	protected void createContents() {
		shlLogin = new Shell();
		shlLogin.setSize(450, 300);
		shlLogin.setText("Login");
		
		Combo combo = new Combo(shlLogin, SWT.NONE);
		combo.setBounds(50, 48, 117, 22);
		
		Label lblSelectDistrict = new Label(shlLogin, SWT.NONE);
		lblSelectDistrict.setBounds(50, 28, 97, 14);
		lblSelectDistrict.setText("Select District:");

	}

}
