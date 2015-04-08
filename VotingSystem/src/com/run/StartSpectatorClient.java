package com.run;
import com.server2.SpectatorClient;

public class StartSpectatorClient {
	public static void main(String args[]){
		
		int port = Integer.parseInt(args[0]);
		String host = args[1];
		
		SpectatorClient spectatorClient = new SpectatorClient(port, host);
		Thread thread = new Thread(spectatorClient);
		thread.start();
	}
}
