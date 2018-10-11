package com.miniorange.saml.models;

public class Testcase {
	public String testName;
	// public String testExpectation;
	public String testResut; // This should be enum allowing certain types of value
	public String testFailureReason;

	public Testcase(String name, String result, String reason) {
		this.testName = name;
		// this.testExpectation = expectation;
		this.testResut = result;
		this.testFailureReason = reason;
	}

}
