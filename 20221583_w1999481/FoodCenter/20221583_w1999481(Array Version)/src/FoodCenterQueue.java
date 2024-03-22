import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class FoodCenterQueue {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);     // Create a Scanner object to read user input
        int maxBurgers = 50;
        int maxCustomers = 10;
        String[][] queue = {{"X", "X"}, {"X", "X", "X"}, {"X", "X", "X", "X", "X"}};    // Create a queue to hold customers
        int[] queueSizes = {2, 3, 5};      // Define queue sizes for each queue line
        int[] burgerStock = {maxBurgers};  // Initialize the burger stock


        // Print the menu options
        System.out.println("*********************");
        System.out.println("      Cashiers       ");
        System.out.println("*********************");
        System.out.println("Menu Options:");
        System.out.println("100 or VFQ: View all Queues.");
        System.out.println("101 or VEQ: View all Empty Queues.");
        System.out.println("102 or ACQ: Add customer to a Queue.");
        System.out.println("103 or RCQ: Remove a customer from a Queue.");
        System.out.println("104 or PCQ: Remove a served customer.");
        System.out.println("105 or VCS: View Customers Sorted in alphabetical order.");
        System.out.println("106 or SPD: Store Program Data into file.");
        System.out.println("107 or LPD: Load Program Data from file.");
        System.out.println("108 or STK: View Remaining burgers Stock.");
        System.out.println("109 or AFS: Add burgers to Stock.");
        System.out.println("999 or EXT: Exit the Program.");
        System.out.println();

        boolean exitProgram = false;

        while (!exitProgram) {
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            // Switch statement to handle user menu options
            switch (choice) {
                case "100", "VFQ" -> viewAllQueues(queue);
                case "101", "VEQ" -> viewEmptyQueues(queue);
                case "102", "ACQ" -> addCustomerToQueue(queue, queueSizes, burgerStock);
                case "103", "RCQ" -> removeCustomerFromQueue(queue, scanner, burgerStock);
                case "104", "PCQ" -> removeServedCustomer(queue, burgerStock);
                case "105", "VCS" -> viewCustomersSorted(queue);
                case "106", "SPD" -> storeProgramDataToFile();
                case "107", "LPD" -> loadProgramDataFromFile();
                case "108", "STK" -> viewRemainingBurgerStock(burgerStock);
                case "109", "AFS" -> addBurgersToStock(burgerStock, scanner);
                case "999", "EXT" -> {
                    exitProgram = true;
                    System.out.println("Exiting the program.");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void viewAllQueues(String[][] queue) {
        System.out.println("******************");
        System.out.println("     Cashiers     ");
        System.out.println("******************");
        for (int i = 0; i < queue[2].length; i++) {  //Checking rows
            if (i < 2) {
                if (queue[0][i].equals("X")) {  //Checking 1st column
                    System.out.print("X");
                } else {
                    System.out.print("O");
                }
            }
            if (i < 3) {
                if (queue[1][i].equals("X")) {  //Checking 2nd column
                    System.out.print("\tX");
                } else {
                    System.out.print("\tO");
                }
            }
            if (i < 5) {
                if (i == 3 || i == 4) {
                    System.out.print("\t");
                }
                if (queue[2][i].equals("X")) {
                    System.out.print("\tX");
                } else {
                    System.out.print("\tO");
                }
            }
            System.out.println();
        }
    }

    public static void viewEmptyQueues(String[][] queue) {
        System.out.println("Empty Queues:");
        for (int i = 0; i < queue.length; i++) {
            if (isEmptyQueue(queue[i])) {
                System.out.println("Queue " + (i + 1));
            }
        }
    }

    public static boolean isEmptyQueue(String[] queueLine) {
        for (String customer : queueLine) {
            if (!customer.equals("X")) {
                return false;
            }
        }
        return true;
    }

    public static void addCustomerToQueue(String[][] queue, int[] queueSizes, int[] burgerStock) {
        // Add a customer to a specific queue
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter queue number (1, 2, or 3): ");
        int queueNumber = scanner.nextInt();
        scanner.nextLine(); // Consume the remaining newline character
        boolean customerAdded = true;
        int queueIndex = queueNumber - 1;
        if (queueIndex >= 0 && queueIndex < queue.length && !isFullQueue(queue[queueIndex], queueSizes[queueIndex])) {
            for (int i = 0; i < queue[queueIndex].length; i++) {
                if (queue[queueIndex][i].equals("X")) {
                    queue[queueIndex][i] = customerName;
                    customerAdded = false;
                    updateBurgerStock(burgerStock, -5);
                    System.out.println("Customer " + customerName + " added to queue " + queueNumber + ".");
                    checkBurgerStock(burgerStock);
                    break;
                }
            }
        }

        if (customerAdded) {
            System.out.println("Unable to add customer to queue " + queueNumber + ".");
        }

    }

    public static boolean isFullQueue(String[] queueLine, int queueSize) {
        int count = 0;
        for (String customer : queueLine) {
            if (!customer.equals("X")) {
                count++;
            }
        }
        return count >= queueSize;
    }

    public static void updateBurgerStock(int[] burgerStock, int quantity) {
        burgerStock[0] += quantity;
    }

    public static void checkBurgerStock(int[] burgerStock) {
        if (burgerStock[0] <= 10) {
            System.out.println("Warning: Low burger stock! Remaining stock: " + burgerStock[0]);
        }
    }

    public static void removeCustomerFromQueue(String[][] queue, Scanner scanner, int[] burgerStock) {
        System.out.print("Enter queue number (1, 2, or 3): ");
        int queueNumber = scanner.nextInt();
        scanner.nextLine();

        int queueIndex = queueNumber - 1;
        if (queueIndex >= 0 && queueIndex < queue.length && !isEmptyQueue(queue[queueIndex])) {
            System.out.print("Enter customer position (1 to " + queue[queueIndex].length + "): ");
            int customerPosition = scanner.nextInt();
            scanner.nextLine();

            int customerIndex = customerPosition - 1;
            if (customerIndex >= 0 && customerIndex < queue[queueIndex].length && !queue[queueIndex][customerIndex].equals("X")) {

                String removedCustomer = queue[queueIndex][customerIndex];
                queue[queueIndex][customerIndex] = "X";
                //int[] burgerStock = new int[0];
                updateBurgerStock(burgerStock, 5);
                System.out.println("Customer " + removedCustomer + " removed from queue " + queueNumber + ".");
                return;
            }
        }

        System.out.println("Unable to remove customer from queue " + queueNumber + ".");
    }

    public static void removeServedCustomer(String[][] queue, int[] burgerStock) {
        for (String[] queueLine : queue) {
            for (int i = 0; i < queueLine.length; i++) {
                if (!queueLine[i].equals("X")) {
                    String servedCustomer = queueLine[i];
                    queueLine[i] = "X";
                    updateBurgerStock(burgerStock, 5);
                    System.out.println("Customer " + servedCustomer + " served and removed.");
                    return;
                }
            }
        }

        System.out.println("No customers to serve.");
    }

    public static void viewCustomersSorted(String[][] queue) {
        String[] customers = collectCustomers(queue);
        sortCustomers(customers);

        System.out.println("Customers Sorted in alphabetical order:");
        for (String customer : customers) {
            System.out.println(customer);
        }
    }

    public static String[] collectCustomers(String[][] queue) {
        int count = 0;
        for (String[] queueLine : queue) {
            for (String customer : queueLine) {
                if (!customer.equals("X")) {
                    count++;
                }
            }
        }

        String[] customers = new String[count];
        int index = 0;
        for (String[] queueLine : queue) {
            for (String customer : queueLine) {
                if (!customer.equals("X")) {
                    customers[index] = customer;
                    index++;
                }
            }
        }

        return customers;
    }

    public static void sortCustomers(String[] customers) {
        for (int i = 0; i < customers.length - 1; i++) {
            for (int j = i + 1; j < customers.length; j++) {
                if (compareNames(customers[i], customers[j]) > 0) {
                    String temp = customers[i];
                    customers[i] = customers[j];
                    customers[j] = temp;
                }
            }
        }
    }

    public static int compareNames(String name1, String name2) {
        String lowercaseName1 = name1.toLowerCase();
        String lowercaseName2 = name2.toLowerCase();
        return lowercaseName1.compareTo(lowercaseName2);
    }

    public static void storeProgramDataToFile() {
        // Code to store program data to a file
        try{
            File file = new File("cashier.txt");
            boolean file_created = file.createNewFile();
            if (file_created){System.out.println("File created: " + file.getName());
            }
            else{
                System.out.println("Error while creating file: " + file.getName());
            }

        }
        catch(IOException e){e.printStackTrace();}
    }

    public static void loadProgramDataFromFile() {
        // Code to load program data from a file
        try {File file = new File("cashier.txt");
            Scanner file_reader = new Scanner(file);
            while (file_reader.hasNextLine()) {String text = file_reader.nextLine();
                System.out.println(text);
            }
            file_reader.close();
            System.out.println("Program data stored successfully.");
        }

        catch (IOException e) {
            System.out.println("Error while reading a file.");
            e.printStackTrace();}
    }


    public static void viewRemainingBurgerStock(int[] burgerStock) {
        System.out.println("Remaining burger stock: " + burgerStock[0]);
    }

    public static void addBurgersToStock(int[] burgerStock, Scanner scanner) {
        System.out.print("Enter quantity of burgers to add: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        updateBurgerStock(burgerStock, quantity);
        System.out.println(quantity + " burgers added to stock. Total stock: " + burgerStock[0]);
    }
}
