package com.run;
import com.server2.Server2;

public class StartServer2{

	public static void main(String args[]){
		Server2 server2;
		if(!args[0].isEmpty()){
			server2 = new Server2(Integer.parseInt(args[0]), "go.joyclick.org");
		} else {
			server2 = new Server2(8080, "go.joyclick.org");
		}
		Thread thread = new Thread(server2);
		thread.start();
	}
}