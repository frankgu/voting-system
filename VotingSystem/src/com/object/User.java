package com.object;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

	@Id
	@Column(name = "USER_NAME")
	private String userName;
	@Column(name = "LAST_NAME")
	private String LastName;
	@Column(name = "FIRST_NAME")
	private String FirstName;
	@Column(name = "DISTRICT_NAME")
	private String districtName;
	@Column(name = "ADDRESS")
	private String address;

	//------default constructor
	public User(){}
	
	public User(String userName, String lastName, String firstName,
			String districtName, String address) {
		super();
		this.userName = userName;
		LastName = lastName;
		FirstName = firstName;
		this.districtName = districtName;
		this.address = address;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
