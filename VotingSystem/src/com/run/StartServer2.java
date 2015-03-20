package com.run;
import com.server1.Server1;
import com.server2.Server2;

public class StartServer2{

	public static void main(String args[]){
		Server2 server2 = new Server2(8080, "localhost");
		Thread thread = new Thread(server2);
		thread.start();
	}
}