package FrontEnd;

import java.util.HashSet;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class represents a terminal in the Queen’s Intercity Excursion System
 * (QIES) It takes as input a valid Services list named validService.txt located
 * in the FrontEnd Source Code Project Folder and outputs a transaction summary file named
 * TSF.txt also located in the FronEnd project folder.
 *
 * The program runs in a continuous while loop until no more input from std.IN
 * is detected. A user must log in and specify the mode of operation to be used
 * before the session begins. Privileged transactions are guarded by the boolean
 * flag variable "inPlannerMode". For modularity purposes, each transaction is
 * processed an individual method. To eliminate redundancy, transactions use
 * helper methods to retrieve and validate user input.
 *
 * A TSF class object is used to create the transaction summary file. The method
 * tsf.log(serviceCode, serviceName, ticketsOrCapacity, serviceName2,
 * serviceDate) is used in the transaction methods to write successful
 * transactions to the transaction summary file. The method tsf.endFile() is
 * used in the logout method to add the EOS line at the end of the transaction
 * summary file. Lastly the method tsf.close() is used in both the logout method
 * and the catch clause to close the file writer and ensure there are no
 * resource leaks
 *
 * @author Nathan Lee, Primrose Chareka, Max Karan, Michael Wiederick
 * @version 1.0
 */
public class FrontEndSystem {
	// ----- Class Variables ----- //
	private static HashSet<String> validServices = new HashSet<String>();
	private static boolean inPlannerMode = false;
	private static boolean loggedOut = true;
	private static TSF tsf = new TSF();
	private static Scanner scan = new Scanner(System.in);

	// ----- Main Execution of Front End System ----- //
	public static void main(String[] args) {
		String selectedTransaction = "";

		try {
			while (true) {
				// Ensure user is logged in before attempting another transaction
				if (loggedOut) {
					login();
				} else {
					// Ask user to select a transaction
					System.out.println("Please enter a valid transaction...");
					selectedTransaction = scan.nextLine();

					// Process selected transaction
					switch (selectedTransaction) {
					// Privileged transactions
					case "createService":
						if (inPlannerMode) {
							createService();
						} else {
							System.out.println("This is a privilaged transaction. Please try another transaction...");
						}
						break;
					case "deleteService":
						if (inPlannerMode) {
							deleteService();
						} else {
							System.out.println("This is a privilaged transaction. Please try another transaction...");
						}
						break;
					// General transactions
					case "cancelTicket":
						cancelTicket();
						break;
					case "changeTicket":
						changeTicket();
						break;
					case "sellTicket":
						sellTicket();
						break;
					case "logout":
						logout();
						break;
					}// end switch case
				} // end if
			} // end while
		} catch (Exception e) {
			System.out.println("End of file input");
			tsf.closeLog();
			e.printStackTrace();
		}

	}// End Main

	/**
	 * Logs into the system by setting the class variable loggedIn to true. Sets the
	 * inPlannerMode class boolean true if planner mode is selected or false if
	 * agent is selected.
	 */
	private static void login() {
		// Get input and check if correct for login
		System.out.println("Type 'login' to access the system...");
		String input = scan.nextLine();
		while (!input.equals("login")) {
			System.out.println("Must login to use the system! Please try again...");
			input = scan.nextLine();
		}

		// Get input to check for agent / planner mode
		System.out.println("What mode would you like to use?");
		input = scan.nextLine();
		while (!input.equals("agent") && !input.equals("planner")) {
			System.out.println("Invalid mode! Please try again...");
			input = scan.nextLine();
		}

		// Set flag for the mode which was selected
		if (input.equals("planner")) {
			inPlannerMode = true;
		} else {
			inPlannerMode = false;
		}

		loggedOut = false;
		System.out.println("Logging in using " + input + " mode ... Success!");

		// After a successful login, load the valid services and print menu
		loadValidServiceFile();
		printMenu();
	}

	/**
	 * Logs out of the system by setting the class variable loggedIn to false
	 */
	private static void logout() {
		loggedOut = true;
		tsf.endTSF();
		tsf.closeLog();
		System.out.println("Logging out successfully");
	}

	/**
	 * Creates a new services, and logs a transaction in the TSF however service
	 * cannot be used within same session. Validates user input before logging the
	 * transaction
	 */
	private static void createService() {
		String serviceCode = "CRE";
		String serviceNumber = getServiceNumber();
		if (validServices.contains(serviceNumber)) {
			System.out.println("Error! That service number already exists");
		} else {
			String serviceCapacity = getServiceCapacity();
			String serviceName = getServiceName();
			String serviceDate = getServiceDate();

			tsf.logTransaction(serviceCode, serviceNumber, serviceCapacity, null, serviceName, serviceDate);
			System.out.println("Service successfully created!");
		}
		System.out.println("Returning to main menu...");
	}

	/**
	 * Deletes an existing service making it no longer available for use in this
	 * session and logs the transaction in the TSF. Validates user input before
	 * logging the transaction. If the service number is not listed in the valid
	 * services list, returns to main without logging a transaction
	 */
	private static void deleteService() {
		String serviceCode = "DEL";
		String serviceNumber = getServiceNumber();
		String serviceName = getServiceName();

		if (validServices.contains(serviceNumber)) {
			// Remove service from service list
			validServices.remove(serviceNumber);

			tsf.logTransaction(serviceCode, serviceNumber, null, null, serviceName, null);

			System.out.println("Deleted service number " + serviceNumber + ": " + serviceName);
		} else {
			System.out.println("Error! Did not find a matching service!");
		}

		System.out.println("Returning to main menu...");
	}

	/**
	 * Sells tickets for an existing service and logs the transaction in the TSF.
	 * Validates user input before logging the transaction.If the service number is
	 * not listed in the valid services list, returns to main without logging a
	 * transaction
	 */
	private static void sellTicket() {
		String serviceCode = "SEL";
		String serviceNumber = getServiceNumber();
		String numTickets = getNumberOfTickets();

		// Ensure service was found and sell tickets it if it was found
		if (validServices.contains(serviceNumber)) {
			tsf.logTransaction(serviceCode, serviceNumber, numTickets, null, null, null);
			System.out.println("Sold " + numTickets + " tickets for service " + serviceNumber);
		} else {
			System.out.println("Error! Did not find a matching service!");
		}

		System.out.println("Returning to main menu...");
	}

	/**
	 * Cancels tickets for an existing service and logs the transaction in the TSF.
	 * Assumes user input has been properly validated with regards to formatting. If
	 * the service number is not listed in the valid services list, returns to main
	 * without logging a transaction
	 */
	private static void cancelTicket() {
		String serviceCode = "CAN";
		String serviceNumber = getServiceNumber();
		String numTickets = getNumberOfTickets();

		// Ensure service was found and sell tickets it if it was found
		if (validServices.contains(serviceNumber)) {
			tsf.logTransaction(serviceCode, serviceNumber, numTickets, null, null, null);
			System.out.println("Cancelled " + numTickets + " tickets for service " + serviceNumber);
		} else {
			System.out.println("Error! Did not find a matching service!");
		}

		System.out.println("Returning to main menu...");
	}

	/**
	 * Changes tickets for an existing service to another existing service and logs
	 * the transaction in the TSF. Assumes user input has been properly validated
	 * with regards to formatting. If either of the two service numbers are not
	 * listed in the valid services list, returns to main without logging a
	 * transaction
	 */
	private static void changeTicket() {
		String serviceCode = "CHG";
		String serviceNumber = getServiceNumber();
		String serviceNumber2 = getServiceNumber();
		String numTickets = getNumberOfTickets();

		// Find service to change tickets for
		if (validServices.contains(serviceNumber) && validServices.contains(serviceNumber)) {
			tsf.logTransaction(serviceCode, serviceNumber, numTickets, serviceNumber2, null, null);
		} else {
			System.out.println("Error! Did not find a matching service!");
		}

		System.out.println("Returning to main menu...");
	}

	// ======================= User Input Methods ======================= //
	private static String getServiceNumber() {
		System.out.println("Please enter the service number");
		String serviceNumber = scan.nextLine();
		boolean isValid = false;
		while (!isValid) {
			if (serviceNumber.length() != 5 || serviceNumber.charAt(0) == '0') {
				System.out.println("Invalid Service number. Please enter a 5 digit number with no leading zeros");
			} else {
				return serviceNumber;
			}
			serviceNumber = scan.nextLine();
		}
		return null;
	}

	private static String getServiceCapacity() {
		System.out.println("Please enter the service capacity");
		String serviceCapacity = scan.nextLine();

		boolean isValid = false;
		while (!isValid) {
			if (Integer.valueOf(serviceCapacity) < 0 || Integer.valueOf(serviceCapacity) > 1000) {
				System.out.println("Please enter a number between 0 and 1000 (inclusive)");
				serviceCapacity = scan.nextLine();
			} else {
				return serviceCapacity;
			}
		}
		return null;
	}

	private static String getServiceName() {
		System.out.println("Please enter the service name");
		boolean isValid = false;
		String serviceName = scan.nextLine().toLowerCase();

		while (!isValid) {
			if (serviceName.length() < 3 || serviceName.length() > 40) {
				System.out.println("Invalid string length");
			} else if (serviceName.charAt(0) == ' ' || serviceName.charAt(serviceName.length() - 1) == ' ') {
				System.out.println("Service name cannot start or end with a space");
			} else {
				return serviceName;
			}
			serviceName = scan.nextLine().toLowerCase();
		}
		return null;
	}

	private static String getServiceDate() {
		System.out.println("Please enter the service date");
		String serviceDate = scan.nextLine();

		boolean isValid = false;
		while (!isValid) {
			if (!serviceDate.matches("[0-9]{8}\\.[0-9]{4}")) {
				System.out.println("Invalid Date. Please enter a date in the format YYYYMMDD.hhmm");
				serviceDate = scan.nextLine();
			} else {
				return serviceDate;
			}
		}


		return null;
	}

	private static String getNumberOfTickets() {
		System.out.println("Please enter the number of tickets");
		String numTickets = scan.nextLine();

		boolean isValid = false;
		while (!isValid) {
			if (Integer.valueOf(numTickets) < 0 || Integer.valueOf(numTickets) > 1000) {
				System.out.println("Please enter a number between 0 and 1000 (inclusive)");
				numTickets = scan.nextLine();
			} else {
				return numTickets;
			}
		}
		return null;
	}

	// ======================= Helper Methods ======================= //
	// Print the menu for the user
	private static void printMenu() {
		System.out.println("----- MAIN MENU -----");
		System.out.println("Type any of the following commands to continue...");
		System.out.println("createService");
		System.out.println("deleteService");
		System.out.println("cancelTicket");
		System.out.println("changeTicket");
		System.out.println("sellTicket");
		System.out.println("logout");
	}

	// Load valid services list
	private static void loadValidServiceFile() {
		String serviceNumber;
		String filePath = "../sourceCode/FrontEnd/validServices.txt";
		BufferedReader fileReader;

		try {
			fileReader = new BufferedReader(new FileReader(filePath));
			serviceNumber = fileReader.readLine();

			while (!serviceNumber.equals("00000")) {
				// For each entry add new service
				validServices.add(serviceNumber);
				serviceNumber = fileReader.readLine();
			}
			fileReader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
