//UDPClient1.java: A simple UDP client program.
package com.client;


public class Client {

	public String ip;//server ip
	public String name;//server name
	public int    port;//server port

	// client ctr
	public Client() {
		ip   = "0.0.0.0";
		name = "null";
		port = 0;
	}

	public void run() {
		
		//config window
		ClientConfig config = new ClientConfig();
		try {
			config.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		name = config.name;
		ip   = config.ip;
		port = config.port;
		
		System.out.println("ServerName: "+ name +"\nServer IP: " + ip +"\nServer Port: "+port);
		
		//while(true){
			//login window
			ClientLogin login = new ClientLogin(name, name, ip, port);
			try {
				login.open();
			} catch (Exception e) {
				e.printStackTrace();
			}

			//voting window
			try {
				if(login.usrInfo.split(":")[3].equals("1")){// check if voted
					ClientVote voting = new ClientVote(name, ip, port, login.usr, login.pwd, 
							login.usrInfo.split(":")[0], 
							login.usrInfo.split(":")[1], 
							login.usrInfo.split(":")[2]);
					voting.open();
				}else{
					ClientVote voting = new ClientVote(name, ip, port, login.usr, login.pwd, 
							login.usrInfo.split(":")[0], 
							login.usrInfo.split(":")[1], 
							login.usrInfo.split(":")[2]);
					voting.open();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		//}
		
	}

}

