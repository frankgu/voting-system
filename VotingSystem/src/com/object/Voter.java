package com.object;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "VOTER")
@PrimaryKeyJoinColumn(name = "USER_ID")
public class Voter extends User {

	public Voter(String userName, String lastName, String firstName,
			String districtName, String address) {
		super(userName, lastName, firstName, districtName, address);
		// TODO Auto-generated constructor stub
	}

	@Column(name = "CANDIDATE_NAME")
	private String candidateName;

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

}
