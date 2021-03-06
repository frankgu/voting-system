package com.server2;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import org.hibernate.Session;

import com.functions.HibernateUtil;
import com.functions.Transmission;
import com.object.Candidate;

public class SpectatorClient extends JFrame implements Runnable{
	SpectatorClientGUIPanel panel;
	//the list of candidateList and get method. Populated by getCandidatePolls
	private List<Candidate> candidateList;
	private List<Candidate> candidateListDistrict;
	private ArrayList <Candidate> winner;
	private ArrayList <String> districts;
	
	private int port;
	private String host;
	
	public SpectatorClient(int port, String host){
		//frame properties(visible, size, closeoperation)
		setVisible(true);
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		//gui panel
		panel = new SpectatorClientGUIPanel();
		add(panel);
		panel.setVisible(true);
	
		// initial the port and the host
		this.port = port;
		this.host = host;
	}
	
	public void run(){
		while(true){
				updateCandidateList();
				panel.setData(candidateListDistrict);
				
				updateWinner();
				panel.setWinner(winner);
				
				updateDistricts();
				panel.setDistricts(districts);
				//update the size of the frame based on the number of candidates
				setSize(candidateListDistrict.size()*100 + 125, 500);
				
				panel.repaint();
				try{
					Thread.sleep(1000);
				}
				catch(Exception e){}
			}
	}
	
	private void updateCandidateList() {
		// -----get the candidate list
		try{
			DatagramSocket aSocket = new DatagramSocket();
			Transmission tran = new Transmission(aSocket);
			InetAddress ahost = InetAddress.getByName(host);
			String response = tran.sendData("1:", port, ahost);
			//System.out.println(response);
			String [] candidateStringArray = response.split("::");
			candidateList = new ArrayList<Candidate>();
			for (int i = 0; i<candidateStringArray.length; i++){
				String [] candidate = candidateStringArray[i].split(":");
				Candidate cand = new Candidate(candidate[0], candidate[1], candidate[2], Integer.parseInt(candidate[3]));
				candidateList.add(cand);
			}
			
			candidateListDistrict = new ArrayList<Candidate>();
			if (panel.getSelectedDistrict()!="All Districts"){
				for(int i = 0; i<candidateList.size(); i++){
					if (candidateList.get(i).getDistrictName().equals(panel.getSelectedDistrict())){
						candidateListDistrict.add(candidateList.get(i));
					}
				}
			}else candidateListDistrict = candidateList;
		}catch(Exception e){}
	}
	
	private void updateWinner(){
		List<Candidate> sortedVotes = new ArrayList<Candidate>(candidateListDistrict);
		Collections.sort(sortedVotes,new PollComparator());
		winner = new ArrayList<Candidate>();
		if (!sortedVotes.isEmpty()){
			winner.add(sortedVotes.get(0));
			for (int i=1; i<sortedVotes.size(); i++){
				if (sortedVotes.get(i).getPolls()==winner.get(0).getPolls())
					winner.add(sortedVotes.get(i));
			}
		}
	}
	
	private void updateDistricts(){
		districts = new ArrayList<String>();
		if (!candidateList.isEmpty()){
			districts.add(candidateList.get(0).getDistrictName());
			for(int i = 1; i<candidateList.size(); i++){
				if (!districts.contains(candidateList.get(i).getDistrictName()))
					districts.add(candidateList.get(i).getDistrictName());
			}
		}
	}
}
