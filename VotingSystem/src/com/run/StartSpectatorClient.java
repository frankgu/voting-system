package com.run;
import com.server2.SpectatorClient;

public class StartSpectatorClient {
	public static void main(String args[]){
		SpectatorClient spectatorClient = new SpectatorClient();
		Thread thread = new Thread(spectatorClient);
		thread.start();
	}
}
