package com.testframework;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

public class testframework_launch {

	protected Shell shell;
	private Text text;
	private Text text_1;
	private Button button;
	private Button btnTest;
	
	private String selectedDir1="";
	private String selectedDir2="";
	private String selectedDir3="";
	
	private Text text_2;
	private Button button_1;
	private Label lblOutputFile;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			testframework_launch window = new testframework_launch();
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
		shell.setSize(482, 338);
		shell.setText("TestFrameWork");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(60, 46, 290, 19);
		
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(60, 123, 290, 19);
		
		Button btnBrowse = new Button(shell, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedDir1 = "";
				FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
		        fileDialog.setFilterPath(selectedDir1);
		        fileDialog.setFilterExtensions(new String[]{"*.txt"});
		        fileDialog.setFilterNames(new String[]{ ""});
		        String dir = fileDialog.open();
		        if(dir != null) {
		          selectedDir1 = dir;
		        }
		        text.setText(selectedDir1);
			}
		});
		btnBrowse.setBounds(356, 42, 75, 28);
		btnBrowse.setText("browse");
		
		button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedDir2 = "";
				FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
		        fileDialog.setFilterPath(selectedDir2);
		        fileDialog.setFilterExtensions(new String[]{"*.txt"});
		        fileDialog.setFilterNames(new String[]{ ""});
		        String dir = fileDialog.open();
		        if(dir != null) {
		          selectedDir2 = dir;
		        }
		        text_1.setText(selectedDir2);
			}
		});
		button.setText("browse");
		button.setBounds(356, 119, 75, 28);
		
		Label lblCandidatesFile = new Label(shell, SWT.NONE);
		lblCandidatesFile.setBounds(52, 21, 113, 14);
		lblCandidatesFile.setText("Candidates' File:");
		
		Label lblVotersFile = new Label(shell, SWT.NONE);
		lblVotersFile.setBounds(60, 103, 75, 14);
		lblVotersFile.setText("Voters' File:");
		
		btnTest = new Button(shell, SWT.NONE);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(selectedDir1.compareTo("")!=1 && selectedDir2.compareTo("")!=1){
//					testframework tf = new testframework(selectedDir1, selectedDir2,selectedDir3);
//					System.out.println(selectedDir1);
//					System.out.println(selectedDir2);
//					tf.run();
				
					testcases_window tw = new testcases_window();
					tw.open(selectedDir1,selectedDir2,selectedDir3);
				}
			}
		});
		btnTest.setBounds(145, 255, 159, 28);
		btnTest.setText("Choose Test Case");
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(60, 193, 290, 19);
		
		button_1 = new Button(shell, SWT.NONE);
		button_1.setText("browse");
		button_1.setBounds(356, 189, 75, 28);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedDir3 = "";
				DirectoryDialog directoryDialog = new DirectoryDialog(shell);
		        
		        directoryDialog.setFilterPath(selectedDir3);
		        directoryDialog.setMessage("Please select a directory and click OK");
		        
		        String dir = directoryDialog.open();
		        if(dir != null) {
		          selectedDir3 = dir;
		        }
		        text_2.setText(selectedDir3);
			}
		});
		
		lblOutputFile = new Label(shell, SWT.NONE);
		lblOutputFile.setBounds(60, 173, 83, 14);
		lblOutputFile.setText("Output File:");
	}
}
