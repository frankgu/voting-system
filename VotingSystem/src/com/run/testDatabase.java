package com.run;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.functions.Transmission;
import com.server1.Server1;

public class testDatabase {

	public static void main(String[] arg) {

		// -----request form : "[flag]:[value]"
		// ---[flag] = 1 , [value] =
		// [flag2]:[userName]:[lastName]:[firstName]:[address] (regist account,
		// flag2 = 1 is voter, flag2 = 2 is candidate)
		// ---[flag] = 2 , [value] = [userName]:[candidateUserName] (voting)
		// ---[flag] = 3 , [value] = [userName] (login)
		// ---[flag] = 4 , [value] = null (get candidate list)
		// ---[flag] = 5 , [value] = [userName] (logout the server1)

		// -----reply form : "[flag]:[value]"
		// ---[flag] = 1 , [value] = string (error message)
		// ---[flag] = 2 , [value] = success
		// ---[flag] = 3 , [value] = [candidate name]:[candidate name]:...
		// (candidate name consist of [userName]:[FirstName]:[LastName])
		
		/*
		 * test the server1 functionality
		 */
		try {
			
			DatagramSocket aSocket = new DatagramSocket();
			Transmission tran = new Transmission(aSocket);

			// -----test
			InetAddress host = InetAddress.getByName("localhost");
			
			
			System.out.println(tran.sendData("1:2:candidate:dongfeng:gu:1591 riverside", 8080, host));
			System.out.println(tran.sendData("1:2:candidate2:dongfeng:gu:1591 riverside", 8080, host));
			System.out.println(tran.sendData("1:1:voter:tom:liu:1591 riverside", 8080, host));
			
			System.out.println(tran.sendData("2:voter:candidate", 8080, host));
			
			System.out.println(tran.sendData("3:voter", 8080, host));
			
			System.out.println(tran.sendData("4:", 8080, host));
		
			System.out.println(tran.sendData("5:voter", 8080, host));
			
			
			
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (UnknownHostException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} 

		/*
		 * test the hibernate operation
		 * 
		 * //Client client = new Client(); // hibernate test User user = new
		 * Voter("gdf1992803", "dongfeng","gu","ottawa","k1g4a7"); Voter voter =
		 * (Voter)user;
		 * 
		 * try{
		 * 
		 * Session session = HibernateUtil.getSessionFactory().openSession();
		 * session.beginTransaction(); session.save(voter);
		 * 
		 * voter = (Voter)session.get(Voter.class, "gdf1992803");
		 * if(voter.getCandidateName() == null){
		 * 
		 * System.out.println("hello");
		 * 
		 * }
		 * 
		 * session.getTransaction().commit(); session.close();
		 * 
		 * } catch(JDBCException e){
		 * 
		 * System.out.print("false");
		 * 
		 * }
		 */

	}
}
