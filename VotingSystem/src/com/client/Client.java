//UDPClient1.java: A simple UDP client program.
package com.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.functions.Transmission;
import com.object.User;

public class Client {

	private User user = null; 		 // the user who login to the client
	private DatagramSocket aSocket = null;
	private Transmission tran = null;		
	
	
	/*
	 * 		validate the host and port before send a message
			 InetAddress aHost = InetAddress.getByName(host);
			int serverPort = Integer.valueOf(port).intValue();
	 */
	
	public Client(){

		try {

			// -----initialize the transmission function and the socket for the client
			aSocket = new DatagramSocket();
			tran = new Transmission(aSocket);
			
			// -----test
			String data = new String("1:1:gdf1992803");
			InetAddress host = InetAddress.getByName("localhost");
			String replyMessage = tran.sendData(data, 8080, host);
			int flag = Integer.parseInt(replyMessage.split(":")[0]);
			if(flag == 1){
				
				//error
				
			} else if(flag == 2){
				
				//success
				System.out.print("success");
			
			}
			// -----test end
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * Step 1: configuration
		 */
		// -----client should pop up a dialog to let the customer to fulfill the
		// server port (have several option, for different districts)

		// -----client should grap the candidate information from the server
		// belong to the specific district

		/*
		 * Step 2: voting
		 */
		// -----after the configuration, voter/candidates can register, voter
		// should have unique userName, candidates should have unique userName
		// and the user information should be sent to the server 1 to store the
		// information.

		// -----voter can vote for a candidates and this information will be
		// sent
		// to the server 1

		/* 
		 * Step 3: logout
		 */
		//user logout the system
		
		/*
		 * requirement
		 */
		// -----the client can show the status of the server

		// -----request form : "[flag]:[value]"
		// [flag] = 1 , [value] = [flag2]:[userName] (regist account)
		// [flag] = 2 , [value] = [userName]:[candidateName] (voting)
		// [flag] = 3 , [value] = [userName] (login)
		// [flag] = 4 , [value] = null (get candidate list)
		// [flag] = 5 , [value] = [userName] (logout the server1)

		// -----reply form : "[flag]:[value]"
		// [flag] = 1 , [value] = string (error message)
		// [flag] = 2 , [value] = success
		// [flag] = 3 , [value] = [candidate name]:[candidate name]:...
		
		// for the lab
		// sendDataToServer("hello world", "60009", "134.117.28.81");

	}

	

}