package com.example.store;

import java.util.ArrayList;


public class WaitingQueue {
	private ArrayList<Customer> queue;

	public WaitingQueue() {
		// Constructor: Initializes an empty queue
		this.queue = new ArrayList<>();
	}
	public boolean isEmpty() {
		// Checks if the queue is empty
		return queue.isEmpty();
	}
	public void add(Customer item) {
		// Adds an item to the end of the queue
		queue.add(item);
	}
	public Customer get() {
		if (isEmpty()) {
			return null;
		}

		// Removes and returns the first item from the queue
		Customer item = queue.remove(0);
		return item;
	}
	public ArrayList<Customer> getCustomers() {
		return this.queue;
	}
}