package com.object;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "CANDIDATE")
@PrimaryKeyJoinColumn(name = "USER_NAME")
public class Candidate extends User {

	public Candidate() {
	}

	//this constructor used for the spectator client(doesnt need the username, address, password)
	public Candidate(String lastName, String firstName, String districtName, int polls){
		super("n/a", lastName, firstName, districtName, "n/a", "n/a");
		this.polls = polls;
	}
	
	public Candidate(String userName, String lastName, String firstName,
			String districtName, String address, String password) {
		super(userName, lastName, firstName, districtName, address, password);
		polls = 0;
		// TODO Auto-generated constructor stub
	}

	@Column(name = "POLLS_NUMBER")
	private int polls;

	public int getPolls() {
		return polls;
	}

	public void setPolls(int polls) {
		this.polls = polls;
	}

}
