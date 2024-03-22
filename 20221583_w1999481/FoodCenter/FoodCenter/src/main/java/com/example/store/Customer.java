package com.example.store;

public class Customer {

	private String firstName;
	private String secondName;
	private int burgersRequired;

	public String getFirstName() {
		return firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public String getFullName() {
		return firstName + " " + secondName;
	}

	public int getBurgersRequired() {
		return burgersRequired;
	}

	public Customer(String firstName, String secondName, int burgersRequired) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.burgersRequired = burgersRequired;
	}
}
