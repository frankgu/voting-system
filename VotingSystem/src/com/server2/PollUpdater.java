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
	//the list of votes and get method. Populated by getCandidatePolls
	private List<Candidate> votes;
	private ArrayList <Candidate> winner;
	
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
				updateCandidateList();
				panel.setData(votes);
				
				updateWinner();
				panel.setWinner(winner);
				
				//update the size of the frame based on the number of candidates
				setSize(votes.size()*100 + 125, 500);
				

				panel.repaint();
				Thread.sleep(1000);
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
		votes = candidates;
		
		session.getTransaction().commit();
		session.close();

	}
	
	private void updateWinner(){
		List<Candidate> sortedVotes = new ArrayList<Candidate>(votes);
		Collections.sort(sortedVotes,new PollComparator());
		winner = new ArrayList<Candidate>();
		winner.add(sortedVotes.get(0));
		for (int i=1; i<sortedVotes.size(); i++){
			if (sortedVotes.get(i).getPolls()==winner.get(0).getPolls())
				winner.add(sortedVotes.get(i));
		}
	}
}
