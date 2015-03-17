
package com.testframework;

import java.io.File;  
import java.io.IOException;
import java.io.InputStreamReader;  
import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.FileInputStream;  
import java.io.FileWriter; 
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

import antlr.collections.impl.Vector;

import com.functions.Transmission;
import com.object.Candidate;
import com.object.User;

public class testframework {

	private static ArrayList<User> users;
	private static ArrayList<Candidate> candidates;
	//private static ArrayList<String> voted;
	private static Vector voted;
	
	private String ufPath;
	private String cfPath;
	private String opPath;

	private File useFile;
	private File candFile;
	private File outputFile;
	
	static InetAddress host;
	DatagramSocket aSocket;
	Transmission tran;
	
	BufferedWriter out;

	public testframework(String cfPath, String ufPath, String opPath) {
		this.ufPath = ufPath;
		this.cfPath = cfPath;
		useFile = new File(ufPath);
		candFile = new File(cfPath);
		
		outputFile = new File(opPath+"//VotingSystemTest.txt");
		
		//voted = new ArrayList<String>();
		try{
			aSocket = new DatagramSocket();
			tran = new Transmission(aSocket);
			//host = InetAddress.getByName("127.0.0.1");
			host = InetAddress.getByName("go.joyclick.org");	
			//out = new BufferedWriter(new FileWriter("//Users//xianchizou//Dropbox//3303//testframework//votingsys_m1//VotingSystemTest.txt"));
			out = new BufferedWriter(new FileWriter(outputFile));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		users = new ArrayList<User>();
		candidates = new ArrayList<Candidate>();
		
	}

	public void run(String chosenCase) {
		readUserData();
		readCandData();
		voted = new Vector(users.size());
		try{
			if(chosenCase.equals("VR1") || chosenCase.equals("VR2") || chosenCase.equals("VR3")){	//the voters registration cases
				System.out.print(123);
				registerVoter(users);
				out.close();
				return;
			}
			if(chosenCase.equals("CR1") || chosenCase.equals("CR2") || chosenCase.equals("CR3")){	//the candidate registration cases
				registerCandidate(candidates);
				out.close();
				return;
			}
			if(chosenCase.equals("V1") || chosenCase.equals("V1")){		//the voting cases 
				registerCandidate(candidates);
				registerVoter(users);
				voterLogin(users);
				voting(users, candidates);
				voterLogout(users);
				if(voted.size()==users.size()){
					out.close();
				}
				return;
			}
			if(chosenCase.equals("LI1") || chosenCase.equals("LI2")||chosenCase.equals("LI3")||chosenCase.equals("LI4")||chosenCase.equals("LI5")){
				registerCandidate(candidates);
				registerVoter(users);
				voterLogin(users);
				out.close();
				return;
			}
			if(chosenCase.equals("LO1")||chosenCase.equals("LO2")){
				registerCandidate(candidates);
				registerVoter(users);
				voterLogin(users);
				voterLogout(users);
				out.close();
				return;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void readCandData() {
		String data = "";
		BufferedReader br;
		Candidate tempC;
		try {
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(candFile));
			br = new BufferedReader(reader);
			while((data=br.readLine())!=null){
				tempC = new Candidate();
				String[] temp = data.split(":");
				tempC.setUserName(temp[0]);
				tempC.setLastName(temp[1]);
				tempC.setFirstName(temp[2]);
				tempC.setAddress(temp[3]);
				candidates.add(tempC);
			}
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
//		for(int i=0; i<candidates.size();i++){
//			System.out.println(candidates.toString());
//		}
	}

	protected void readUserData() {
		String data = "";
		BufferedReader br;
		User tempU;
		try {
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(useFile));
			br = new BufferedReader(reader);
			while((data=br.readLine())!=null){
				tempU = new User();
				String[] temp = data.split(":");
				tempU.setUserName(temp[0]);
				tempU.setLastName(temp[1]);
				tempU.setFirstName(temp[2]);
				tempU.setAddress(temp[3]);
				tempU.setPassword(temp[4]);
				users.add(tempU);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
//		for(int i=0; i<users.size();i++){
//			System.out.println(users.get(i).toString());
//		}
	}
	
	public void registerVoter(ArrayList<User> users) throws IOException{
		for(int j=0; j<users.size();j++){	//register users
			User temp = users.get(j);
			String data = "1:1:"+temp.getUserName()+":"+temp.getLastName()+":"+temp.getFirstName()+":"+temp.getAddress()+":"+temp.getPassword();
			String output = tran.sendData(data, 8088, host)+"\r\n";
			out.write(output);
			System.out.print(output);
			out.flush();
		}
	}
	
	public void registerCandidate(ArrayList<Candidate> candidates) throws IOException {
		for(int i=0; i<candidates.size();i++){	//register candidate
			Candidate temp = candidates.get(i);
			String data = "1:2:"+temp.getUserName()+":"+temp.getLastName()+":"+temp.getFirstName()+":"+temp.getAddress();
			out.write(tran.sendData(data, 8088, host)+"\r\n");
			out.flush();
		}
	}
	
	public void voterLogin(ArrayList<User> users) throws IOException{
		for(int i=0; i<users.size(); i++){
			User temp = users.get(i);
			String data = "3:"+temp.getUserName()+":"+temp.getPassword();
			String output = tran.sendData(data, 8088, host)+"\r\n";
			out.write(output);
			out.flush();
		}
	}
	
	public void voterLogout(ArrayList<User> users) throws IOException{
		for(int i=0; i<users.size(); i++){
			User temp = users.get(i);
			String data = "5:"+temp.getUserName();
			String output = tran.sendData(data, 8088, host)+"\r\n";
			System.out.println(output);
			out.write(output);
			out.flush();
		}
	}
	
	public void voting(ArrayList<User> users, ArrayList<Candidate> candidates){
		for(int k=0; k<users.size();k++){	//voting
			User temp = users.get(k);
			int random = (int)Math.random()*candidates.size();
			String data = "2:"+temp.getUserName()+":"+candidates.get(random).getUserName();
			//out.write(tran.sendData(data, 8088, host)+"\r\n");
			new Thread(new testVoting(tran,out,temp,candidates.get(random).getUserName())).start();
		}
	}
	
	public static class testVoting implements Runnable {
		//String voterName;
		User voter;
		String candName;
		String message;
		Transmission tran;
		BufferedWriter out;
		
		public testVoting(Transmission t, BufferedWriter o, User voter, String cn){
			this.voter = voter;
			candName=cn;
			tran =t;
			out =o;
		}
		

		@Override
		public void run() {
			String data = "2:"+voter.getUserName()+":"+candName;
			try{
				message = tran.sendData(data, 8088, host);
				out.write(message+"\r\n");
				voted.setElementAt(this.candName, users.indexOf(voter));
				out.flush();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}

