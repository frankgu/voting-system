package com.run;

import com.server1.Server1;

public class Main {

	public static void main(String[] arg){
		
		Server1 server1 = new Server1("ottawa", 8080, "localhost");
		server1.start();
		
	}
}
