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
	protected void createContents() {
		shlLogin = new Shell();
		shlLogin.setSize(450, 300);
		shlLogin.setText("Login");
		
		Combo combo = new Combo(shlLogin, SWT.NONE);
		combo.setBounds(50, 48, 117, 22);
		
		Label lblSelectDistrict = new Label(shlLogin, SWT.NONE);
		lblSelectDistrict.setBounds(50, 28, 97, 14);
		lblSelectDistrict.setText("Select District:");
		
		List list = new List(shlLogin, SWT.BORDER);
		list.setBounds(86, 101, 117, 83);
		
		Label lblCandidates = new Label(shlLogin, SWT.NONE);
		lblCandidates.setText("Candidates:");
		lblCandidates.setBounds(85, 81, 97, 14);
		
		Composite composite = new Composite(shlLogin, SWT.NONE);
		composite.setBounds(256, 101, 110, 83);
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setBounds(10, 5, 60, 14);
		lblName.setText("name");
		
		Label lblInfo = new Label(composite, SWT.NONE);
		lblInfo.setBounds(10, 25, 60, 14);
		lblInfo.setText("info1");
		
		Label lblInfo_1 = new Label(composite, SWT.NONE);
		lblInfo_1.setBounds(10, 42, 60, 14);
		lblInfo_1.setText("info2");
		
		Label lblCandidateInfo = new Label(shlLogin, SWT.NONE);
		lblCandidateInfo.setText("Candidate Info");
		lblCandidateInfo.setBounds(256, 81, 97, 14);
		
		text = new Text(shlLogin, SWT.BORDER);
		text.setBounds(191, 205, 86, 19);
		
		Label lblUsername = new Label(shlLogin, SWT.NONE);
		lblUsername.setBounds(128, 208, 65, 16);
		lblUsername.setText("Username:");
		
		Button btnRegister = new Button(shlLogin, SWT.NONE);
		btnRegister.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//to do
			}
		});
		btnRegister.setBounds(138, 230, 95, 28);
		btnRegister.setText("Register");
		
		Button btnLogin = new Button(shlLogin, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//to do
				System.out.println("test 1");
			}
		});
		btnLogin.setBounds(242, 230, 95, 28);
		btnLogin.setText("Login");

	}
}
