package com.run;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.functions.Transmission;

public class testServer1Function {

	public static void main(String[] arg) {

		// -----request form : "[flag]:[value]"
		// ---[flag] = 1 , [value] =
		// [flag2]:[userName]:[lastName]:[firstName]:[address]:[password]
		// (regist account,
		// flag2 = 1 is voter, flag2 = 2 is candidate)
		// ---[flag] = 2 , [value] = [userName]:[candidateUserName] (voting)
		// ---[flag] = 3 , [value] = [userName]:[password] (login)
		// ---[flag] = 4 , [value] = null (get candidate list)
		// ---[flag] = 5 , [value] = [userName] (logout the server1)
		// ---[flag] = 6 , [value] = [userName] (check the voter
		// vote state)

		// -----reply form : "[flag]:[value]"
		// ---[flag] = 1 , [value] = string (error message)
		// ---[flag] = 2 , [value] = success
		// ---[flag] = 3 , [value] = [candidate name]:[candidate name]:...
		// (candidate name consist of [userName]:[FirstName]:[LastName])
		// ---[flag] = 4 , [value] =
		// [flag2]:[candidateFirstName]:[candidateLastName] (check the voter
		// vote state)
		// [flag2] = 1 (voter hasn't vote) , [flag2] = 2 (voter already vote and
		// return the candidate name)
		/*
		 * test the server1 functionality
		 */
		try {

			DatagramSocket aSocket = new DatagramSocket();
			Transmission tran = new Transmission(aSocket);

			// -----test
			InetAddress host = InetAddress.getByName("localhost");

			System.out.println(tran.sendData(
					"1:2:candidate:dongfeng:gu:1591 riverside:hyfgdf", 8080,
					host));
			System.out.println(tran.sendData(
					"1:2:candidate2:dongfeng:gu:1591 riverside:hyfgdf", 8080,
					host));
			System.out.println(tran.sendData(
					"1:1:voter:tom:liu:1591 riverside:hyfgdf", 8080, host));

			System.out.println(tran.sendData("2:voter:candidate", 8080, host));

			System.out.println(tran.sendData("3:voter:hyfgf", 8080, host));

			System.out.println(tran.sendData("4:", 8080, host));

			System.out.println(tran.sendData("5:voter", 8080, host));
			
			System.out.println(tran.sendData("6:voter", 8080, host));
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (UnknownHostException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}
}
