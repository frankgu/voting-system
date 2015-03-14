package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;

import java.util.ArrayList;

public class ClientConfig {
    public class serverInfo{
    	public String ip;
    	public int port;
    	public String name;
    	
    	public serverInfo(){
    		ip = "";
    		port = 0;
    		name = "";
    	}
    	
    	public serverInfo(String n, String i, int p){
    		ip = i;
    		port = p;
    		name = n;
    	}
    }
    
	protected Shell shlClientConfiguration;
    public String serverIP, serverPort, serverName;
    //public static final int MAX_DISTRICT_SERVER = 10;
    public ArrayList<serverInfo> serverList = new ArrayList<serverInfo>();
    public String ip;
	public int port;
	public String name;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ClientConfig window = new ClientConfig();
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
		shlClientConfiguration.open();
		shlClientConfiguration.layout();
		while (!shlClientConfiguration.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlClientConfiguration = new Shell();
		shlClientConfiguration.setSize(450, 300);
		shlClientConfiguration.setText("Client Configuration");
		
		final Combo combo = new Combo(shlClientConfiguration, SWT.READ_ONLY);
		combo.setBounds(183, 97, 131, 22);
		
		URL servers;
		try {
			servers = new URL("https://raw.githubusercontent.com/frankgu/voting-system/master/VotingSystem/server1List.txt?token=AFlT_PaUfHr2oF_dqPREZCc4GsfyYNS4ks5VDa41wA%3D%3D");
			BufferedReader in;
			in = new BufferedReader(
					new InputStreamReader(servers.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null){
				combo.add(inputLine.split(":")[0]);
				serverInfo srv = new serverInfo(inputLine.split(":")[0], inputLine.split(":")[1], Integer.parseInt(inputLine.split(":")[2]));
				serverList.add(srv);
				//serverList.get(num).ip = inputLine.split(":")[1];
				//System.out.println(inputLine.split(":")[2]);
			}
			
			in.close();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*combo.addSelectionListener(new SelectionListener() {
		      public void widgetSelected(SelectionEvent e) {
		        System.out.println(combo.getText());
		      }

		      public void widgetDefaultSelected(SelectionEvent e) {
		        System.out.println(combo.getText());
		      }
		    });*/
		
		Label label = new Label(shlClientConfiguration, SWT.NONE);
		label.setText("Select District:");
		label.setBounds(87, 100, 97, 14);
		
		Button btnOk = new Button(shlClientConfiguration, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if( combo.getText().length() == 0){
					int style = SWT.ICON_ERROR;
					MessageBox noInfo = new MessageBox(shlClientConfiguration, style);
					noInfo.setMessage("Please select a district!");
					shlClientConfiguration.setEnabled(false);
					noInfo.open();
					shlClientConfiguration.setEnabled(true);
				}else{
					int n = 0;
					while(serverList.get(n) != null){
						if( combo.getText().equals(serverList.get(n).name) ){
							name = serverList.get(n).name;
							ip   = serverList.get(n).ip;
							port = serverList.get(n).port;
							break;
						}
						n++;
					}
					System.out.println("ServerName: "+ name +"\nServer IP: " + ip +"\nServer Port: "+port);
					shlClientConfiguration.dispose();
				}	
			}
		});
		btnOk.setBounds(162, 161, 95, 28);
		btnOk.setText("OK");
		
		shlClientConfiguration.addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event) {
		        int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		        MessageBox messageBox = new MessageBox(shlClientConfiguration, style);
		        messageBox.setText("Exit");
		        messageBox.setMessage("Are You Sure to Exit?");
		        event.doit = messageBox.open() == SWT.YES;
		      }
		    });

	}
}
