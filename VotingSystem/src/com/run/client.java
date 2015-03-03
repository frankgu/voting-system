package com.run;

import org.hibernate.Session;

import com.object.User;
import com.object.Voter;
import com.functions.*;
import com.client.ClientGUI;
import com.client.ClientLogin;

public class client {

	public static void main(String[] arg) {

		//Client client = new Client()
		
		
		//client login panel
		try {
			ClientLogin window = new ClientLogin();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//client main panel
		try {
			ClientGUI window = new ClientGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		User user = new Voter("gdf1992803", "dongfeng","gu","ottawa","k1g4a7");
		Voter voter = (Voter)user;
		voter.setCandidateName("hello");
		
		// hibernate test
		Session session = HibernateUtilServer2.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(voter);
		session.getTransaction().commit();
		session.close();
		
	}
}
