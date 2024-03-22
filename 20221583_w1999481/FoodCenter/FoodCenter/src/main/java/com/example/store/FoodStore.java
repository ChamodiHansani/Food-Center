package com.example.store;


import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class FoodStore {

	private static boolean run = true;
	private static int burgers = 50;
	private static BurgerQueue cashier1 = new BurgerQueue(2);
	private static BurgerQueue cashier2 = new BurgerQueue(3);
	private static BurgerQueue cashier3 = new BurgerQueue(5);

	private static WaitingQueue waitingQueue = new WaitingQueue();



	public static void main(String[] args) throws IOException {

		while (run) {
			if (burgers <= 10) {
				System.out.println("*************************************");
				System.out.println("Burger stocks are low please restock.");
				System.out.println("*************************************");
			}

			userChoise();
		}

	}

	public static void printInitialMessage() {
		System.out.println();
		System.out.println("Welcome to Food Center Queue Management System!");
		System.out.println("Menu Options:");
		System.out.println("100 or VFQ: View all Queues.");
		System.out.println("101 or VEQ: View all Empty Queues.");
		System.out.println("102 or ACQ: Add customer to a Queue.");
		System.out.println("103 or RCQ: Remove a customer from a Queue. (From a specific location)");
		System.out.println("104 or PCQ: Remove a served customer.");
		System.out.println("105 or VCS: View Customers Sorted in alphabetical order.");
		System.out.println("106 or SPD: Store Program Data into file.");
		System.out.println("107 or LPD: Load Program Data from file.");
		System.out.println("108 or STK: View Remaining burgers Stock.");
		System.out.println("109 or AFS: Add burgers to Stock.");
		System.out.println("110 or IFQ: Income of each cashier.");
		System.out.println("999 or EXT: Exit the Program.");
	}

	private static void userChoise() throws IOException {
		
		Scanner scanner = new Scanner(System.in);
		String input;
		printInitialMessage();
		System.out.print("Enter an option: ");
		input = scanner.nextLine();

		try {
			input = input.toUpperCase();
			userInput(input);

		} catch (Exception e) {
			userInput(input);
		}

	}

	private static void userInput(String input) throws IOException {
		
		switch (input) {
		case "VFQ":
		case "100":
			viewAllQueues();
			break;
		case "VEQ":
		case "101":
			viewEmptyQueues();
			break;
		case "ACQ":
		case "102":
			addCustomerToCashier();
			break;
		case "RCQ":
		case "103":
			removeCustomerFromCashier();
			break;
		case "PCQ":
		case "104":
			removeServedCustomer();
			break;
		case "VCS":
		case "105":
			getSortedCustomers();
			break;
		case "SPD":
		case "106":
			saveToFile();
			break;
		case "LPD":
		case "107":
			readFromFile();
			break;
		case "STK":
		case "108":
			System.out.println("Remaining number of burgers: " + burgers);
			break;
		case "AFS":
		case "109":
			burgers = 50;
			System.out.println("Stock refilled");
			break;
		case "IFQ":
		case "110":
			printIncome();
			break;
		case "EXT":
		case "999":
			run = false;
			break;
		default:
			System.out.println("Please enter a valid input....");
			break;
		}
	}

	private static void viewAllQueues() {

		System.out.println("*****************");
		System.out.println("    Cashiers     ");
		System.out.println("*****************");

		for (int i = 0; i < 5; i++) {

			System.out.print("     ");
			if (cashier1.getCustomer(i) != null && i < 2) {
				System.out.print("O ");
			} else if (i < 2) {
				System.out.print("X ");
			} else {
				System.out.print("  ");
			}

			if (cashier2.getCustomer(i) != null && i < 3) {
				System.out.print("O ");
			} else if (i < 3) {
				System.out.print("X ");
			} else {
				System.out.print("  ");
			}

			if (cashier3.getCustomer(i) != null) {
				System.out.print("O ");
			} else {
				System.out.print("X ");
			}
			System.out.println();

		}
	}

	private static void viewEmptyQueues() {
		System.out.println("*****************");
		System.out.println("    Cashiers     ");
		System.out.println("*****************");

		List<BurgerQueue> queueList = new ArrayList<>();
		int emptyQueueQTY =0;
		queueList.add(cashier1);
		queueList.add(cashier2);
		queueList.add(cashier3);
		

		for (int i = 0; i < queueList.size(); i++) {
			if (queueList.get(i).isQueueEmpty()) {
				System.out.println("Queue " + (i + 1));
				emptyQueueQTY+=1;
			}
		}
		if(emptyQueueQTY<1){
			System.out.println("No Empty Queues");
		}

		System.out.println();

	}

	private static void addCustomerToCashier() {
		var fname = stringInput("Please Enter your first name: ");
		String sname = stringInput("Please Enter your second name: ");
		int burgers = intInput("Please Enter number of burgers: ", 1, 10);
		Customer customer = new Customer(fname, sname, burgers);
		WaitingQueue waitingQueue = new WaitingQueue();

		BurgerQueue smallestQueue = null;
		if (!cashier1.isQueueFull()) {
			smallestQueue = cashier1;
		}
		if (!cashier2.isQueueFull()
				&& (smallestQueue == null || cashier2.getCurrentCapacity() < smallestQueue.getCurrentCapacity())) {
			smallestQueue = cashier2;
		}
		if (!cashier3.isQueueFull()
				&& (smallestQueue == null || cashier3.getCurrentCapacity() < smallestQueue.getCurrentCapacity())) {
			smallestQueue = cashier3;
		}

		if (smallestQueue != null) {
			smallestQueue.addCustomer(customer);
			System.out.println("Customer added to Queue "
					+ (cashier1 == smallestQueue ? "1" : (cashier2 == smallestQueue ? "2" : "3")));
		} else {
			boolean addToWaitingQueue = false;
			while (!addToWaitingQueue) {
				String answer = stringInput(
						"We're sorry, all the queues are full. Do you wish to be added to the waiting queue? (Y/N): ");
				if (answer.equalsIgnoreCase("Y")) {
					addToWaitingQueue = true;
					waitingQueue.add(customer);
					System.out.println("Customer added to the Waiting Queue");
				} else if (answer.equalsIgnoreCase("N")) {
					addToWaitingQueue = true;
					// Handle the case where the customer does not want to be added to the waiting
					// queue
				} else {
					System.out.println("Invalid input. Please enter 'Y' or 'N'.");
				}
			}
		}
	}

	private static void removeServedCustomer() {
		int cashierNumber = intInput("Enter the cashier number (1, 2, or 3): ", 1, 3);

		if (cashierNumber == 1 && !cashier1.isQueueEmpty()) {
			burgers = burgers - cashier1.getCustomers().get(0).getBurgersRequired();
			cashier1.soldQty+=cashier1.getCustomers().get(0).getBurgersRequired();
			cashier1.serveCustomer();
			cashier1.addCustomer(waitingQueue.get());
		} else if (cashierNumber == 2 && !cashier2.isQueueEmpty()) {
			burgers = burgers - cashier2.getCustomers().get(0).getBurgersRequired();
			cashier2.soldQty+=cashier2.getCustomers().get(0).getBurgersRequired();
			cashier2.serveCustomer();
			cashier2.addCustomer(waitingQueue.get());
		} else if (cashierNumber == 3 && !cashier3.isQueueEmpty()) {
			burgers = burgers - cashier3.getCustomers().get(0).getBurgersRequired();
			cashier3.soldQty+=cashier3.getCustomers().get(0).getBurgersRequired();
			cashier3.serveCustomer();
			cashier3.addCustomer(waitingQueue.get());
		} else{
			System.out.println("No Customers to Serve at Cashier "+ cashierNumber);
		}
	}

	private static void removeCustomerFromCashier() {
		int cashierNumber = intInput("Enter the cashier number (1, 2, or 3): ", 1, 3);
		int customerNumber;

		if(cashier1.isQueueEmpty() && cashier2.isQueueEmpty() && cashier3.isQueueEmpty()){
			System.out.println("No Customers to remove at Cashier "+ cashierNumber);
			return;
		}

		if (cashierNumber == 1 && !cashier1.isQueueEmpty()) {
			customerNumber = intInput("Enter the customer number (1, 2): ", 1, 2);
			cashier1.removeCoustomer(customerNumber - 1);
			cashier1.addCustomer(waitingQueue.get());
		} else if (cashierNumber == 2 && !cashier2.isQueueEmpty()) {
			customerNumber = intInput("Enter the customer number (1, 2,3): ", 1, 3);
			cashier2.removeCoustomer(customerNumber - 1);
			cashier2.addCustomer(waitingQueue.get());
		} else if (cashierNumber == 3 && !cashier3.isQueueEmpty()) {
			customerNumber = intInput("Enter the customer number (1, 2,3,4,5): ", 1, 5);
			cashier3.removeCoustomer(customerNumber - 1);
			cashier3.addCustomer(waitingQueue.get());
		}else {
			System.out.println("No Customers to Serve at Cashier "+ cashierNumber);
		}
	}

	private static void getSortedCustomers() {
		ArrayList<String> sortedList = new ArrayList<>();

		for (Customer customer : cashier1.getCustomers()) {
			sortedList.add(customer.getFullName());
		}
		for (Customer customer : cashier2.getCustomers()) {
			sortedList.add(customer.getFullName());
		}
		for (Customer customer : cashier3.getCustomers()) {
			sortedList.add(customer.getFullName());
		}

		int n = sortedList.size();
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (sortedList.get(j).compareTo(sortedList.get(j + 1)) > 0) {
					String temp = sortedList.get(j);
					sortedList.set(j, sortedList.get(j + 1));
					sortedList.set(j + 1, temp);
				}
			}
		}

		for (String name : sortedList) {
			if (name != null) {
				System.out.println(name);
			}
		}
	}

	private static int intInput(String text, int min, int max) {
		Scanner scanner = new Scanner(System.in);
		int input;

		while (true) {
			System.out.print(text);

			try {
				input = scanner.nextInt();
				if (min <= input && input <= max) {
					return input;
				} else {
					System.out.println("Please enter a number between " + min + " and " + max);
				}
			} catch (Exception e) {
				System.out.println("Please enter a valid number.");
				scanner.nextLine(); // Clear the invalid input from the scanner
			}
		}
	}

	private static String stringInput(String text) {
		Scanner scanner = new Scanner(System.in);
		System.out.print(text);
		while (true) {
			try {
				String input = scanner.nextLine();
				String pattern = "^[a-zA-Z]+$";
				if (Pattern.matches(pattern, input)) {
					return input;
				} else {
					System.out.print("Please enter a valid name: ");
				}
			} catch (Exception e) {
				continue;
			}
		}
	}

	private static void saveToFile() {
		try {
			FileWriter writer = new FileWriter("program_data.txt");

			// Write the data to the file
			writer.write("Bugers: " + burgers + "\n");

			// Write the customers in each cashier's queue
			writer.write("Cashier 1 Queue:\n");
			for (Customer customer : cashier1.getCustomers()) {
				writer.write(customer.getFullName() + " - Burgers: " + customer.getBurgersRequired() + "\n");
			}
			writer.write("\n");

			writer.write("Cashier 2 Queue:\n");
			for (Customer customer : cashier2.getCustomers()) {
				writer.write(customer.getFullName() + " - Burgers: " + customer.getBurgersRequired() + "\n");
			}
			writer.write("\n");

			writer.write("Cashier 3 Queue:\n");
			for (Customer customer : cashier3.getCustomers()) {
				writer.write(customer.getFullName() + " - Burgers: " + customer.getBurgersRequired() + "\n");
			}
			writer.write("\n");

			// Write the customers in the waiting queue
			// Write the customers in the waiting queue
			writer.write("Waiting Queue:\n");
			ArrayList<Customer> waitingCustomers = waitingQueue.getCustomers();
			for (Customer customer : waitingCustomers) {
				writer.write(customer.getFullName() + " - Burgers: " + customer.getBurgersRequired() + "\n");
			}
			writer.write("\n");

			writer.close();
			System.out.println("Program data stored successfully.");

		} catch (IOException e) {
			System.out.println("Error storing program data: " + e.getMessage());
		}
	}

	private static void readFromFile() {
		try {
			File file = new File("program_data.txt");
			Scanner scanner = new Scanner(file);

			// Read and set the value of bugers
			String line = scanner.nextLine();
			String[] burgerLine = line.split(":");
			int bugers = Integer.parseInt(burgerLine[1].trim());

			// Read and populate the customers in each cashier's queue
			for (int i = 0; i < 3; i++) {
				line = scanner.nextLine(); // Skip the cashier queue header
				while (scanner.hasNextLine()) {
					line = scanner.nextLine().trim();
					if (line.isEmpty()) {
						break; // End of the current cashier's queue
					}
					String[] customerInfo = line.split("-");
					String fullName = customerInfo[0].trim();
					String fname = fullName.split(" ")[0];
					String sname = fullName.split(" ")[1];
					int burgersRequired = Integer.parseInt(customerInfo[1].split(":")[1].trim());
					Customer customer = new Customer(fname, sname, burgersRequired);
					// Add the customer to the corresponding cashier's queue based on the loop
					// counter i
					switch (i) {
					case 0:
						cashier1.addCustomer(customer);
						break;
					case 1:
						cashier2.addCustomer(customer);
						break;
					case 2:
						cashier3.addCustomer(customer);
						break;
					}
				}
			}

			// Read and populate the customers in the waiting queue
			line = scanner.nextLine(); // Skip the waiting queue header
			while (scanner.hasNextLine()) {
				line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					break; // End of the waiting queue
				}
				String[] customerInfo = line.split("-");
				String fullName = customerInfo[0].trim();
				String fname = fullName.split(" ")[0];
				String sname = fullName.split(" ")[1];
				int burgersRequired = Integer.parseInt(customerInfo[1].split(":")[1].trim());
				Customer customer = new Customer(fname, sname, burgersRequired);
				waitingQueue.add(customer);
			}

			scanner.close();
			System.out.println("Program data read successfully.");

		} catch (FileNotFoundException e) {
			System.out.println("Program data file not found.");
		}
	}

	private static void printIncome() {
		int cashier1Income = cashier1.getIncome();
		int cashier2Income = cashier2.getIncome();
		int cashier3Income = cashier3.getIncome();
		int total = cashier1Income + cashier2Income + cashier3Income;
		System.out.println("Income of cashier 1: " + cashier1Income);
		System.out.println("Income of cashier 2: " + cashier2Income);
		System.out.println("Income of cashier 3: " + cashier3Income);
		System.out.println();
		System.out.println("Total income: " + total);
	}


	}


