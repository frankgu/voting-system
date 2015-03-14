package com.run;

import com.server1.Server1;

public class StartServer1_CMD {

	public static void main(String[] arg){
		if(!arg[0].isEmpty() && !arg[1].isEmpty() && !arg[2].isEmpty()){
			Server1 server1 = new Server1(arg[0], Integer.parseInt(arg[1]), arg[2]);
			//addline name:ip:port to git remote server1List.txt
			Thread thread = new Thread(server1);
			thread.start();
			//on termination, search name:ip:port on remote list, delete line
		}else{
			System.out.println("Invalid Inputs!\njava -jar name port ip");
		}
		
		
	}
}
