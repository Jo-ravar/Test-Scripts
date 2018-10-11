package com.miniorange.saml.models;

public class UserAttributes {

	public String fname;
	public String lname;
	public String email; // This should be enum allowing certain types of value
	public String username;
	public String userLastName;
	public String userEmail;
	public String grpAttrName;
	public String adminGrp;
	public String userGrp;

	public UserAttributes(String fname, String lname, String email, String username, String userLastName,
			String userEmail, String grpAttrName, String adminGrp, String userGrp) {
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.username = username;
		this.userLastName = userLastName;
		this.userEmail = userEmail;
		this.grpAttrName = grpAttrName;
		this.adminGrp = adminGrp;
		this.userGrp = userGrp;
	}

}
