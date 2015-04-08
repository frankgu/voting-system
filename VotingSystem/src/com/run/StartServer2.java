package com.run;
import com.server2.Server2;

public class StartServer2{

	public static void main(String args[]){
		Server2 server2 = new Server2(9090, "127.0.0.1");
		Thread thread = new Thread(server2);
		thread.start();
	}
}