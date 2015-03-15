package com.run;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.server1.Server1;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Server1Starter_GUI {

	protected Shell  shlServerStarter;
	public    String name;
	public    String ip;
	public    int    port;
	private Text text;
	private Text text_1;
	private Text text_2;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Server1Starter_GUI window = new Server1Starter_GUI();
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
		shlServerStarter.open();
		shlServerStarter.layout();
		while (!shlServerStarter.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlServerStarter = new Shell(SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
		shlServerStarter.setSize(450, 300);
		shlServerStarter.setText("Server1 Starter");
		
		Label lblDistrictName = new Label(shlServerStarter, SWT.NONE);
		lblDistrictName.setAlignment(SWT.RIGHT);
		lblDistrictName.setBounds(91, 80, 94, 19);
		lblDistrictName.setText("District Name:");
		
		text = new Text(shlServerStarter, SWT.BORDER);
		text.setBounds(191, 77, 101, 19);
		
		Label lblIp = new Label(shlServerStarter, SWT.NONE);
		lblIp.setText("Ip:");
		lblIp.setAlignment(SWT.RIGHT);
		lblIp.setBounds(91, 118, 94, 14);
		
		Label lblPort = new Label(shlServerStarter, SWT.NONE);
		lblPort.setText("Port:");
		lblPort.setAlignment(SWT.RIGHT);
		lblPort.setBounds(91, 157, 94, 16);
		
		text_1 = new Text(shlServerStarter, SWT.BORDER);
		text_1.setBounds(191, 115, 101, 19);
		
		text_2 = new Text(shlServerStarter, SWT.BORDER| SWT.V_SCROLL);
		text_2.setBounds(191, 154, 101, 19);
		
		Button btnStart = new Button(shlServerStarter, SWT.NONE);
		btnStart.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(!text.getText().isEmpty() && !text_1.getText().isEmpty() && !text_2.getText().isEmpty()){
					Server1 server1 = new Server1(text.getText(), Integer.parseInt(text_2.getText()), text_1.getText());
					Thread thread = new Thread(server1);
					thread.start();
					shlServerStarter.dispose();
				}else{
					int style = SWT.ICON_ERROR;
					MessageBox noInfo = new MessageBox(shlServerStarter, style);
					noInfo.setMessage("Invalid Inouts!");
					shlServerStarter.setEnabled(false);
					noInfo.open();
					shlServerStarter.setEnabled(true);
				}
			}
		});
		btnStart.setBounds(191, 209, 95, 28);
		btnStart.setText("Start");
		text_2.addListener(SWT.Verify, new Listener() {
		      public void handleEvent(Event e) {
		        String string = e.text;
		        char[] chars = new char[string.length()];
		        string.getChars(0, chars.length, chars, 0);
		        for (int i = 0; i < chars.length; i++) {
		          if (!('0' <= chars[i] && chars[i] <= '9')) {
		            e.doit = false;
		            return;
		          }
		        }
		      }
		    });

	}
}
