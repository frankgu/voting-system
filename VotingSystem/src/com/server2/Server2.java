// udpServer.java: A simple UDP server program.
package com.server2;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.hibernate.Session;

import com.functions.HibernateUtil;
import com.object.Candidate;

public class Server2 extends JFrame implements Runnable{
	Server2GUIPanel panel;
	//the list of votes and get method. Populated by getCandidatePolls
	private List<Candidate> votes;
	private ArrayList <Candidate> winner;
	
	public Server2() {
		// initial the hibernate factory
		HibernateUtil.getSessionFactory();
		
		//frame properties(visible, size, closeoperation)
		//pack();
		setVisible(true);
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//gui panel
		panel = new Server2GUIPanel();
		add(panel);
		panel.setVisible(true);
	}

	@Override
	public void run() {
		while(true){
			try{
				getCandidatePolls();
				panel.setData(votes);
				
				getWinner();
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
	
	private void getCandidatePolls() {
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
	
	private void getWinner(){
		List<Candidate> sortedVotes = new ArrayList(votes);
		votes.sort(new PollComparator());
		winner = new ArrayList<Candidate>();
		winner.add(votes.get(0));
		for (int i=1; i<votes.size(); i++){
			if (votes.get(i).getPolls()==winner.get(0).getPolls())
				winner.add(votes.get(i));
		}
	}
}
