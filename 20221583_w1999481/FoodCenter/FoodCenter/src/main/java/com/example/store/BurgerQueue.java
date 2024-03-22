package com.example.store;

import java.util.ArrayList;

public class BurgerQueue {
	private final int burgerPrice = 650;
	private ArrayList<Customer> queue;
	public int queueSize;
	public int soldQty;

	public BurgerQueue(int queueSize) {
		this.queue = new ArrayList<>();
		this.queueSize = queueSize;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public int getCurrentCapacity() {
		return queue.size();
	}

	private boolean queueSizeCheacker() {
		return this.queue.size() < this.queueSize;
	}

	public void addCustomer(Customer customer) {
		if (queueSizeCheacker()){
			this.queue.add(customer);
		}
		else {
			System.out.println("The queue is full Please wait");
		}
	}

	public ArrayList<Customer> getCustomers() {
		return queue;
	}

	public Customer getCustomer(int customerNumber) {
		try {
			return this.queue.get(customerNumber);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}

	}

	public void serveCustomer() {
		this.queue.remove(0);
		System.out.println("Customer has been served.");
	}

	public boolean isQueueEmpty() {
		return this.queue.isEmpty();
	}

	public boolean isQueueFull() {
		return queue.size() == queueSize;
	}

	public void removeCoustomer(int index) {
		this.queue.remove(index);
	}

	public int getIncome() {
		int total = 0;


			total += soldQty * burgerPrice;
		
		return total;
	}
}
