
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javassist.compiler.ast.ASTree;
import antlr.collections.impl.Vector;

import com.functions.Transmission;
import com.object.Candidate;
import com.object.User;

public class testframework {

	private static ArrayList<User> users;
	private static ArrayList<Candidate> candidates;
	private static ArrayList<String> voted;
	private static int port = 8088;
	//private static Vector voted;
	
//	private String ufPath;
//	private String cfPath;
	private String opPath;

	private File useFile;
	private File candFile;
	private File outputFile;
	
	static InetAddress host;
	DatagramSocket aSocket;
	Transmission tran;
	
	BufferedWriter out;
	ExecutorService executor;
	ExecutorService executor2;
	ExecutorService executor3;

	public testframework(String cfPath, String ufPath, String opPath) {
//		this.cfPath = cfPath;
//		this.ufPath = ufPath;
		useFile = new File(ufPath);
		candFile = new File(cfPath);
		this.opPath = opPath;
		
		//outputFile = new File(opPath+"//VotingSystemTest.txt");
		
		//voted = new ArrayList<String>();
		try{
			aSocket = new DatagramSocket();
			tran = new Transmission(aSocket);
			host = InetAddress.getByName("127.0.0.1");
			//host = InetAddress.getByName("go.joyclick.org");	
			//out = new BufferedWriter(new FileWriter("//Users//xianchizou//Dropbox//3303//testframework//votingsys_m1//VotingSystemTest.txt"));
			//out = new BufferedWriter(new FileWriter(outputFile));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		users = new ArrayList<User>();
		candidates = new ArrayList<Candidate>();
		
	}

	public void run(String chosenCase) {
		readCandData();
		readUserData();		
		//voted = new Vector(users.size());
		//voted = new ArrayList<String>();
		try{
			if(chosenCase.equals("VR1") || chosenCase.equals("VR2") || chosenCase.equals("VR3")){	//the voters registration cases
				//registerVoter(users);
				outputFile = new File(opPath + "//VoterRegisterTest_"+chosenCase+".txt");
				out = new BufferedWriter(new FileWriter(outputFile));
				executor = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					executor.submit(new Process("RegisterVoter", out,users.get(i)));
				}
				
				executor.shutdown();
				System.out.println("All voters are registered!");
				executor.awaitTermination(5, TimeUnit.SECONDS);
				
				out.close();
				return;
			}
			if(chosenCase.equals("CR1") || chosenCase.equals("CR2") || chosenCase.equals("CR3")){	//the candidate registration cases
//				registerCandidate(candidates);
				outputFile = new File(opPath + "//CandidateRegisterTest_"+chosenCase+".txt");
				out = new BufferedWriter(new FileWriter(outputFile));
				
				executor = Executors.newFixedThreadPool(candidates.size());
				for(int i=0; i<candidates.size(); i++){
					executor.submit(new Process("RegisterCandidate", out,candidates.get(i)));
				}
				
				executor.shutdown();
				System.out.println("All candidates are registered!");
				executor.awaitTermination(5, TimeUnit.SECONDS);
				
				out.close();
				return;
			}
			if(chosenCase.equals("V1")){		//the voting cases 
//				registerCandidate(candidates);
//				registerVoter(users);
//				voterLogin(users);
//				voting(users, candidates);				
//				if(voted.size()==users.size()){
//					//voterLogout(users);
//					out.close();
//				}
//				voterLogout(users);
				outputFile = new File(opPath + "//VotingTest_"+chosenCase+".txt");
				out = new BufferedWriter(new FileWriter(outputFile));
				
				executor = Executors.newFixedThreadPool(candidates.size());
				for(int i=0; i<candidates.size(); i++){
					executor.submit(new Process("RegisterCandidate", out,candidates.get(i)));
				}
				
				executor.shutdown();
				System.out.println("All candidates are registered!");
				executor.awaitTermination(5, TimeUnit.SECONDS);
				
				executor2 = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					int k = (int) (Math.random()*candidates.size());
					executor2.submit(new Process("Voting", out,users.get(i), candidates.get(k)));
				}
				
				executor2.shutdown();
				System.out.println("All voters are voted!");
				executor2.awaitTermination(5, TimeUnit.SECONDS);
				
				out.close();
				return;
			}
			
			if( chosenCase.equals("V2")){
				outputFile = new File(opPath + "//VotingTest_"+chosenCase+".txt");
				out = new BufferedWriter(new FileWriter(outputFile));
				
				executor = Executors.newFixedThreadPool(candidates.size());
				for(int i=0; i<candidates.size(); i++){
					executor.submit(new Process("RegisterCandidate", out,candidates.get(i)));
				}
				
				executor.shutdown();
				System.out.println("All candidates are registered!");
				executor.awaitTermination(20, TimeUnit.SECONDS);
				
				executor2 = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					int k = (int) (Math.random()*candidates.size());
					executor2.submit(new Process("Voting", out,users.get(i), candidates.get(k)));
				}
				
				executor2.shutdown();
				executor2.awaitTermination(20, TimeUnit.SECONDS);
				
				executor3 = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					int k = (int) (Math.random()*candidates.size());
					executor3.submit(new Process("Voting2", out,users.get(i), candidates.get(k)));
				}
				
				executor3.shutdown();
				executor3.awaitTermination(20, TimeUnit.SECONDS);
				
				out.close();
				return;
			}
			
			if(chosenCase.equals("LI1")){
//				registerVoter(users);
//				voterLogin(users);
				outputFile = new File(opPath + "//LoginTest_"+chosenCase+".txt");
				out = new BufferedWriter(new FileWriter(outputFile));
				
				executor = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					executor.submit(new Process("VoterLogin", out,users.get(i)));
				}
				
				executor.shutdown();
				System.out.println("All voters are loged in!");
				executor.awaitTermination(20, TimeUnit.SECONDS);
				
				out.close();
				return;
			}
			if(chosenCase.equals("LI3")){
				outputFile = new File(opPath + "//LoginTest_"+chosenCase+".txt");
				out = new BufferedWriter(new FileWriter(outputFile));
				
				executor = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					executor.submit(new Process("VoterLogin", out,users.get(i)));
				}
				
				executor.shutdown();
				System.out.println("All voters are loged in!");
				executor.awaitTermination(10, TimeUnit.SECONDS);
				
				executor2 = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					executor2.submit(new Process("VoterLogin3", out,users.get(i)));
				}
				executor2.shutdown();
				executor2.awaitTermination(10, TimeUnit.SECONDS);
				
				out.close();
				return;
			}
			
			if(chosenCase.equals("LI4")){
				outputFile = new File(opPath + "//LoginTest_"+chosenCase+".txt");
				out = new BufferedWriter(new FileWriter(outputFile));
				
				executor = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					executor.submit(new Process("VoterLogin4", out,users.get(i)));
				}
				
				executor.shutdown();
				executor.awaitTermination(5, TimeUnit.SECONDS);
			}
			
			if(chosenCase.equals("LI2")||chosenCase.equals("LI5")){
				outputFile = new File(opPath + "//LoginTest_"+chosenCase+".txt");
				out = new BufferedWriter(new FileWriter(outputFile));
				
				executor = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					executor.submit(new Process("VoterLogin2", out,users.get(i)));
				}
				
				executor.shutdown();
				executor.awaitTermination(5, TimeUnit.SECONDS);
			}
			
			if(chosenCase.equals("LO1")){
//				registerVoter(users);
//				voterLogin(users);
//				voterLogout(users);
				outputFile = new File(opPath + "//LogoutTest_"+chosenCase+".txt");
				out = new BufferedWriter(new FileWriter(outputFile));
				
				executor = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					executor.submit(new Process("VoterLogout", out,users.get(i)));
				}
				
				executor.shutdown();
				System.out.println("All voters are loged out!");
				executor.awaitTermination(5, TimeUnit.SECONDS);
				
				
				out.close();
				return;
			}
			
			if(chosenCase.equals("LO2")){
				outputFile = new File(opPath + "//LogoutTest_"+chosenCase+".txt");
				out = new BufferedWriter(new FileWriter(outputFile));
				
				executor = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					executor.submit(new Process("VoterLogout", out,users.get(i)));
				}
				
				executor.shutdown();
				System.out.println("All voters are loged out!");
				executor.awaitTermination(10, TimeUnit.SECONDS);
				
				executor2 = Executors.newFixedThreadPool(users.size());
				for(int i=0; i<users.size(); i++){
					executor2.submit(new Process("VoterLogout2", out,users.get(i)));
				}
				
				executor2.shutdown();
				executor2.awaitTermination(10, TimeUnit.SECONDS);
				
				out.close();
				return;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void readCandData() {
		String data = "";
		BufferedReader cr;
		Candidate tempCa;
		try {
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(candFile));
			cr = new BufferedReader(reader);
			while((data =cr.readLine())!=null){
				//System.out.println(data);
				tempCa = new Candidate();
				String[] temp = data.split(":");
				tempCa.setUserName(temp[0]);
				tempCa.setLastName(temp[1]);
				tempCa.setFirstName(temp[2]);
				tempCa.setAddress(temp[3]);
				candidates.add(tempCa);
			}
			//System.out.println(candidates.size());

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
			//br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		for(int i=0; i<users.size();i++){
//			System.out.println(users.get(i).toString());
//		}
	}
	
	public class Process implements Runnable {
		String chosenCase;
		BufferedWriter out;
		User voter;
		Candidate cand;
		
		public Process(String chosenCase, BufferedWriter out, User voter){
			this.chosenCase = chosenCase;
			this.out = out;
			this.voter = voter;
			cand = null;
		}
		
		public Process(String chosenCase, BufferedWriter out, Candidate cand){
			this.chosenCase = chosenCase;
			this.out = out;
			voter = null;
			this.cand = cand;
		}
		
		public Process(String chosenCase, BufferedWriter out, User voter,Candidate cand){
			this.chosenCase = chosenCase;
			this.out = out;
			this.voter = voter;
			this.cand = cand;
		}
		
		public void run() {
			synchronized (out) {
			if(chosenCase.equals("RegisterVoter")){
				registerVoter_S(voter);
				return;
			}
			if(chosenCase.equals("RegisterCandidate")){
				registerCandidate_S(cand);
				return;
			}
			if(chosenCase.equals("VoterLogin")){
				registerVoter_S(voter);
				voterLogin_S(voter);
				return;
			}
			if(chosenCase.equals("VoterLogin2")){
				registerVoter_S(voter);
				User fakeVoter = voter;
				fakeVoter.setUserName(voter.getUserName()+"a");
				voterLogin_S(fakeVoter);
				return;
			}
			
			if(chosenCase.equals("VoterLogin3")){
				voterLogin_S(voter);
				return;
			}
			if(chosenCase.equals("VoterLogin4")){
				registerVoter_S(voter);
				User fakeVoter = voter;
				fakeVoter.setPassword(voter.getPassword()+"a");
				voterLogin_S(fakeVoter);
				return;
			}
			if(chosenCase.equals("Voting")){
				registerVoter_S(voter);
				voterLogin_S(voter);
				voting_S(voter,cand);
				voterLogout_S(voter);
				return;
			}
			if(chosenCase.equals("Voting2")){
				voterLogin_S(voter);
				voting_S(voter, cand);
				voterLogout_S(voter);
				return;
			}
			if(chosenCase.equals("VoterLogout")){
				registerVoter_S(voter);
				voterLogin_S(voter);
				voterLogout_S(voter);
				return;
			}
			if(chosenCase.equals("VoterLogout2")){
				voterLogout_S(voter);
				return;
			}
			}
			
		}
		
		public void voting_S(User voter, Candidate cand){
			try{
				DatagramSocket soc = new DatagramSocket();
				Transmission tra = new Transmission(soc);
				String data = "2:"+voter.getUserName()+":"+cand.getUserName();
				String output = tra.sendData(data, port, host);
				out.write(output+"\r\n");
				System.out.println(output);
				out.flush();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public void registerVoter_S(User voter) {
			try{
			DatagramSocket soc = new DatagramSocket();
			Transmission tra = new Transmission(soc);
			String data = "1:1:"+ voter.getUserName()+":"+voter.getLastName()+":"+voter.getFirstName()+":"+voter.getAddress()+":"+voter.getPassword();
			String output = tra.sendData(data, port, host);
			out.write(output+"\r\n");
			System.out.println(output);
			out.flush();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public void registerCandidate_S(Candidate cand){
			try{
			DatagramSocket soc = new DatagramSocket();
			Transmission tra = new Transmission(soc);
			String data = "1:2:"+cand.getUserName()+":"+cand.getLastName()+":"+cand.getFirstName()+":"+cand.getAddress();
			String output = tra.sendData(data, port, host);
			out.write(output+"\r\n");
			System.out.println(output);
			out.flush();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		public void voterLogout_S(User voter){
			try {
				DatagramSocket soc = new DatagramSocket();
				Transmission tra = new Transmission(soc);
				String data = "5:"+voter.getUserName();
				String output = tra.sendData(data, port, host);
				out.write(output+"\r\n");
				System.out.println(output);
				out.flush();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public void voterLogin_S(User voter){
			try{
				DatagramSocket soc = new DatagramSocket();
				Transmission tra = new Transmission(soc);
				String data = "3:"+voter.getUserName()+":"+voter.getPassword();
				String output = tra.sendData(data, port, host);
				String [] temp = output.split(":");
				if(temp[0].equals("2")){
					String message = "2:("+voter.getUserName()+") has successfully loged in"+"\r\n";
					out.write(message);
				}
				if(temp[0].equals("1")){
					out.write(output+"\r\n");
				}
				System.out.println(output);
				out.flush();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}

