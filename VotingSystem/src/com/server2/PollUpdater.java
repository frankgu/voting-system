package com.server2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import org.hibernate.Session;

import com.functions.HibernateUtil;
import com.object.Candidate;

public class PollUpdater extends JFrame implements Runnable{
	Server2GUIPanel panel;
	//the list of candidateList and get method. Populated by getCandidatePolls
	private List<Candidate> candidateList;
	private ArrayList <Candidate> winner;
	private ArrayList <String> districts;
	
	public PollUpdater(){

		// initial the hibernate factory
		HibernateUtil.getSessionFactory();
				
		//frame properties(visible, size, closeoperation)
		setVisible(true);
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//gui panel
		panel = new Server2GUIPanel();
		add(panel);
		panel.setVisible(true);
	}
	
	
	public void run(){
		while(true){
			try{
				//retrieve the list of candidates from the database and update the bar graph based on which district is selected
				updateCandidateList();
				List<Candidate> temp = new ArrayList<Candidate>();
				if (panel.getSelectedDistrict()!="All Districts"){
					for(int i = 0; i<candidateList.size(); i++){
						if (candidateList.get(i).getDistrictName().equals(panel.getSelectedDistrict())){
							temp.add(candidateList.get(i));
						}
					}
				}else temp = candidateList;
				panel.setData(temp);
				
				updateWinner();
				panel.setWinner(winner);
				
				updateDistricts();
				panel.setDistricts(districts);
				//update the size of the frame based on the number of candidates
				setSize(temp.size()*100 + 125, 500);
				
				panel.repaint();
				Thread.sleep(100);
			}
			catch(Exception e){
				
			}
		}
	}
	
	private void updateCandidateList() {
		// -----get the candidate list
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		//retrieve all the candidate data from the database and then store them as the candidate object
		//into the list(below)
		
		List<Candidate> candidates = session.createCriteria(Candidate.class)
				.list();
		candidateList = candidates;
		
		session.getTransaction().commit();
		session.close();

	}
	
	private void updateWinner(){
		List<Candidate> sortedVotes = new ArrayList<Candidate>(candidateList);
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
