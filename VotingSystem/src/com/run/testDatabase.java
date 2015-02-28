package com.run;

import org.hibernate.JDBCException;
import org.hibernate.Session;

import com.object.User;
import com.object.Voter;
import com.functions.*;

public class testDatabase {

	public static void main(String[] arg) {

		//Client client = new Client();
		// hibernate test
		User user = new Voter("gdf1992803", "dongfeng","gu","ottawa","k1g4a7");
		Voter voter = (Voter)user;
		
		try{
			
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(voter);
			
			voter = (Voter)session.get(Voter.class, "gdf1992803");
			if(voter.getCandidateName() == null){

				System.out.println("hello");
				
			}
			
			session.getTransaction().commit();
			session.close();
			
		} catch(JDBCException e){
			
			System.out.print("false");
			
		}
		
		
	}
}
