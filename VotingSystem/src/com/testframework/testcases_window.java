package com.testframework;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

public class testcases_window {
	
//	static String l1;
//
//	static String l2;
//
//	static String l3;

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			testcases_window window = new testcases_window();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
//		this.l1 = l1;
//		this.l2 = l2;
//		this.l3 = l3;
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(530, 409);
		shell.setText("TestCases");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//testframework tf = new testframework(l1, l2, l3);
				//tf.run("VR1");
				testframework_launch tfl = new testframework_launch();
				tfl.open("VR1");
			}
		});
		btnNewButton.setBounds(37, 36, 90, 28);
		btnNewButton.setText("Register");
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("VR2");
				testframework_launch tfl = new testframework_launch();
				tfl.open("VR2");
			}
		});
		button.setBounds(155, 36, 138, 28);
		button.setText("DuplicateVoters");
		
		Button btnLargenumbervoters = new Button(shell, SWT.NONE);
		btnLargenumbervoters.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("VR3");
				testframework_launch tfl = new testframework_launch();
				tfl.open("VR3");
			}
		});
		btnLargenumbervoters.setBounds(315, 36, 170, 28);
		btnLargenumbervoters.setText("LargeNumberVoters");
		
		Label lblRegister = new Label(shell, SWT.NONE);
		lblRegister.setBounds(10, 10, 90, 20);
		lblRegister.setText("VoterRegister:");
		
		Button btnRegister = new Button(shell, SWT.NONE);
		btnRegister.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("CR1");
				testframework_launch tfl = new testframework_launch();
				tfl.open("CR1");
			}
		});
		btnRegister.setBounds(37, 109, 90, 28);
		btnRegister.setText("Register");
		
		Label lblCandidateregister = new Label(shell, SWT.NONE);
		lblCandidateregister.setBounds(10, 80, 131, 23);
		lblCandidateregister.setText("CandidateRegister:");
		
		Button btnDuplicatecandidates = new Button(shell, SWT.NONE);
		btnDuplicatecandidates.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("CR2");
				testframework_launch tfl = new testframework_launch();
				tfl.open("CR2");
			}
		});
		btnDuplicatecandidates.setBounds(133, 109, 172, 28);
		btnDuplicatecandidates.setText("DuplicateCandidates");
		
		Button btnLargenumbercandidates = new Button(shell, SWT.NONE);
		btnLargenumbercandidates.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("CR3");
				testframework_launch tfl = new testframework_launch();
				tfl.open("CR3");
			}
		});
		btnLargenumbercandidates.setBounds(312, 109, 187, 28);
		btnLargenumbercandidates.setText("LargeNumberCandidates");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("V1");
				testframework_launch tfl = new testframework_launch();
				tfl.open("V1");
			}
		});
		btnNewButton_1.setBounds(37, 163, 94, 28);
		btnNewButton_1.setText("Vote");
		
		Button btnAlreadyvoted = new Button(shell, SWT.NONE);
		btnAlreadyvoted.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("V2");
				testframework_launch tfl = new testframework_launch();
				tfl.open("V2");
			}
		});
		btnAlreadyvoted.setBounds(149, 163, 125, 28);
		btnAlreadyvoted.setText("AlreadyVoted");
		
		Label lblVoting = new Label(shell, SWT.NONE);
		lblVoting.setBounds(10, 143, 59, 28);
		lblVoting.setText("Voting:");
		
		Button btnActivatedlimit = new Button(shell, SWT.NONE);
		btnActivatedlimit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("LI1");
				testframework_launch tfl = new testframework_launch();
				tfl.open("LI1");
			}
		});
		btnActivatedlimit.setBounds(37, 220, 108, 28);
		btnActivatedlimit.setText("ActivatedLimit");
		
		Label lblLogin = new Label(shell, SWT.NONE);
		lblLogin.setBounds(10, 197, 59, 28);
		lblLogin.setText("Login:");
		
		Button btnNewButton_2 = new Button(shell, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("LI2");
				testframework_launch tfl = new testframework_launch();
				tfl.open("LI2");
			}
		});
		btnNewButton_2.setBounds(149, 220, 175, 28);
		btnNewButton_2.setText("VoterOutofDistrict");
		
		Button btnAlreadylogedin = new Button(shell, SWT.NONE);
		btnAlreadylogedin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("LI3");
				testframework_launch tfl = new testframework_launch();
				tfl.open("LI3");
			}
		});
		btnAlreadylogedin.setBounds(354, 220, 131, 28);
		btnAlreadylogedin.setText("AlreadyLogedin");
		
		Button btnInvalidpassword = new Button(shell, SWT.NONE);
		btnInvalidpassword.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("LI4");
				testframework_launch tfl = new testframework_launch();
				tfl.open("LI4");
			}
		});
		btnInvalidpassword.setBounds(37, 265, 115, 28);
		btnInvalidpassword.setText("InvalidPassword");
		
		Button btnCannotfindusername = new Button(shell, SWT.NONE);
		btnCannotfindusername.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("LI5");
				testframework_launch tfl = new testframework_launch();
				tfl.open("LI5");
			}
		});
		btnCannotfindusername.setBounds(173, 265, 170, 28);
		btnCannotfindusername.setText("CannotFindUserName");
		
		Label lblLogout = new Label(shell, SWT.NONE);
		lblLogout.setBounds(10, 310, 59, 20);
		lblLogout.setText("Logout:");
		
		Button btnLogout = new Button(shell, SWT.NONE);
		btnLogout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("LO1");
				testframework_launch tfl = new testframework_launch();
				tfl.open("LO1");
			}
		});
		btnLogout.setBounds(37, 335, 94, 28);
		btnLogout.setText("Logout");
		
		Button btnAlreadylogout = new Button(shell, SWT.NONE);
		btnAlreadylogout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				testframework tf = new testframework(l1, l2, l3);
//				tf.run("LO2");
				testframework_launch tfl = new testframework_launch();
				tfl.open("LO2");
			}
		});
		btnAlreadylogout.setBounds(152, 335, 111, 28);
		btnAlreadylogout.setText("AlreadyLogout");

	}
}
