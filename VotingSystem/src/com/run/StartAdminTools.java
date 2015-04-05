package com.run;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import swing2swt.layout.FlowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class StartAdminTools {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			StartAdminTools window = new StartAdminTools();
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
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());
		
		Button btnCandReg = new Button(shell, SWT.NONE);
		btnCandReg.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setEnabled(false);
				try {
					StartCandidateRegister window = new StartCandidateRegister();
					window.open();
				} catch (Exception ee) {
					ee.printStackTrace();
				}
				shell.setEnabled(true);
			}
		});
		FormData fd_btnCandReg = new FormData();
		fd_btnCandReg.top = new FormAttachment(0, 61);
		fd_btnCandReg.right = new FormAttachment(100, -113);
		fd_btnCandReg.left = new FormAttachment(0, 109);
		btnCandReg.setLayoutData(fd_btnCandReg);
		btnCandReg.setText("Candidate Register");
		
		Button btnCloseDistrictElection = new Button(shell, SWT.NONE);
		fd_btnCandReg.bottom = new FormAttachment(100, -169);
		btnCloseDistrictElection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setEnabled(false);
				try {
					CloseElectionServer window = new CloseElectionServer();
					window.open();
				} catch (Exception ee) {
					ee.printStackTrace();
				}
				shell.setEnabled(true);
			}
		});
		FormData fd_btnCloseDistrictElection = new FormData();
		fd_btnCloseDistrictElection.top = new FormAttachment(btnCandReg, 19);
		fd_btnCloseDistrictElection.left = new FormAttachment(btnCandReg, 0, SWT.LEFT);
		fd_btnCloseDistrictElection.bottom = new FormAttachment(100, -102);
		fd_btnCloseDistrictElection.right = new FormAttachment(100, -113);
		btnCloseDistrictElection.setLayoutData(fd_btnCloseDistrictElection);
		btnCloseDistrictElection.setText("Close District Election");

	}
}
