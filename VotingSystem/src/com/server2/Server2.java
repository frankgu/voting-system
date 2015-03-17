// udpServer.java: A simple UDP server program.
package com.server2;

import java.util.List;

import javax.swing.JFrame;


import org.hibernate.Session;

import com.functions.HibernateUtil;
import com.object.Candidate;

public class Server2 extends JFrame implements Runnable{
	Server2GUIPanel panel;
	//the list of votes and get method. Populated by getCandidatePolls
	private List<Candidate> votes;
	public List<Candidate> getVotes(){
		return votes;
	}
	
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
}
