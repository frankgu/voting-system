package com.run;

import org.hibernate.Session;

import com.object.User;
import com.object.Voter;
import com.functions.*;

public class client {

	public static void main(String[] arg) {

		//Client client = new Client();
		// hibernate test
		User user = new Voter("gdf1992803", "dongfeng","gu","ottawa","k1g4a7");
		Voter voter = (Voter)user;
		voter.setCandidateName("hello");
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(voter);
		session.getTransaction().commit();
		session.close();
		
	}
}
