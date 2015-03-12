//UDPClient1.java: A simple UDP client program.
package com.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.functions.Transmission;
import com.object.User;

public class Client {

	//private User user = null; // the user who login to the client
	private DatagramSocket aSocket = null;
	private Transmission tran = null;
	
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
		
		//login window
		ClientLogin login = new ClientLogin(name, name, ip, port);
		try {
			login.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

/*
 * validate the host and port before send a message InetAddress aHost =
 * InetAddress.getByName(host); int serverPort =
 * Integer.valueOf(port).intValue();
 */

/*
 * try {
 * 
 * // -----initialize the transmission function and the socket for the // client
 * aSocket = new DatagramSocket(); tran = new Transmission(aSocket);
 * 
 * // -----test String data = new String("1:1:gdf1992803"); InetAddress host =
 * InetAddress.getByName("localhost"); String replyMessage = tran.sendData(data,
 * 8080, host); int flag = Integer.parseInt(replyMessage.split(":")[0]);
 * 
 * if(flag == 1){
 * 
 * System.out.println("flag 1 error");//error
 * 
 * } else if(flag == 2){
 * 
 * //success System.out.println("flag 2 success");
 * 
 * } // -----test end } catch (SocketException e) { // TODO Auto-generated catch
 * block e.printStackTrace(); } catch (UnknownHostException e) { // TODO
 * Auto-generated catch block e.printStackTrace(); }
 */

/*
 * Step 1: configuration
 */
// -----client should pop up a dialog to let the customer to fulfill the
// server port (have several option, for different districts)

// -----client should grab the candidate information from the server
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
// user logout the system

/*
 * requirement
 */
// -----the client can show the status of the server

// for the lab
// sendDataToServer("hello world", "60009", "134.117.28.81");
