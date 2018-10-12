package frontEnd;

import java.util.HashSet;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 * 
 * @author 
 * SHOULD BE USING JAVADOC COMMENTS FOR FILE
 */
public class frontEndSystem {
	// ----- Class Variables ----- //
	private static HashSet<String> validServices = new HashSet<String>();
	private static boolean inPlannerMode = false;
	private static Scanner scan = new Scanner(System.in);

	// ----- Main Execution of Front End System ----- //
	public static void main(String[] args) {
		// When system starts, automatically ask to login
		login();
		
		//After a successful login, load the valid services
		frontEndSystem.loadValidServiceFile("./bin/frontEnd/ValidServices.txt");
		
		String service_selection = "";
		while (true) {
			// Get input for service here
			service_selection = scan.nextLine();

			// Process input for service here
			switch (service_selection) {
			case "logout":
				logout();
			case "createService":
				createService();
				break;
			case "deleteService":
				deleteService();
				break;
			case "sellTicket":
				sellTicket();
				break;
			case "cancelTicket":
				cancelTicket();
				break;
			case "changeTicket":
				changeTicket();
				break;
			default:
				System.out.println("Error! Invalid transaction type please try again...");
			}
		}
	}

	// ----- Methods ----- //
	/**
	 * Waits for the user to enter login, and then determines the mode
	 * of operation for the session (planner or agent mode). Sets the inPlannerMode
	 * class boolean variable accordingly
	 */
	private static void login() {
		// Get input and check if correct for login
		System.out.println("Type 'login' to access the system...");
		String input = scan.nextLine();
		while (!input.equals("login")) {
			System.out.println("Must login to use system! Please try again...");
			input = scan.nextLine();
		}

		// Get input to check for agent / planner mode
		System.out.println("What mode would you like to use?");
		input = scan.nextLine();
		while (!input.equals("agent") && !input.equals("planner")) {
			System.out.println("Invalid mode! Please try again...");
			input = scan.nextLine();
		}

		//Set flag for the mode which was selected
		if(input.equals("planner")){ 
			inPlannerMode = true; 
		}

		System.out.println("Logging in... Success!");
		printMenu();
	}

	/**
	 * Terminates the front end program with an exit status of 0
	 */
	private static void logout() {
		System.out.println("Logging out... system will shut down...");
		System.exit(0);
	}

	/**
	 * Gathers the service number, capacity, name and date and logs transaction 
	 * in the transaction summary file.
	 * 
	 * If the service number already exists returns to main without logging 
	 * a transaction
	 */
	private static void createService() {
		String serviceCode = "CRE";
		String serviceNumber = getServiceNumber();
		
		if (serviceNumber!=null&&!validServices.contains(serviceNumber)) {
			String serviceCapacity = getServiceCapacity();
			String serviceName = getServiceName();
			String serviceDate = getServiceDate();
			
			logTransaction(serviceCode, serviceNumber, serviceCapacity,null,serviceName, serviceDate);
			System.out.println("Service successfully created!");
		} else {		
			System.out.println("That service number already exists!");
		}
		System.out.println("Returning to main menu...");
	}

	/**
	 * 
	 */
	private static void deleteService() {
		String serviceCode = "DEL";
		String serviceNumber = getServiceNumber();
		
		if (validServices.contains(serviceNumber)) {
			// Remove service from service list
			validServices.remove(serviceNumber);
			
			String serviceName = getServiceName();
			System.out.println(serviceName);
			logTransaction(serviceCode, serviceNumber, null, null, serviceName, null);

			System.out.println("Deleted service number " + serviceNumber + ": " + serviceName);
		} else {
			System.out.println("Error! Did not find a matching service!");	
		}	
		
		System.out.println("Returning to main menu...");
	}

	/**
	 * 
	 */
	private static void sellTicket() {
		String serviceNumber = "";
		String numTickets = "";

		System.out.println("Please enter the service number");
		serviceNumber = scan.nextLine();

		// Ensure service was found and sell tickets it if it was found
		if (validServices.contains(serviceNumber)) {
			System.out.println("Please enter the number of tickets to sell");
			numTickets = scan.nextLine();
			
			String transaction = "SEL" + " " + serviceNumber + " " + numTickets + " " + "00000" + " " + "*****" + " "
					+ "*****";
			logInTransactionSummaryFile(transaction);
		
			System.out.println("Sold " + numTickets + "tickets for service " + serviceNumber);
		} else {
			System.out.println("Error! Did not find a matching service!");
		}

		System.out.println("Returning to main menu...");
	}

	/**
	 * 
	 */
	private static void cancelTicket() {
		System.out.println("Please enter the service number");
		String serviceNumber = scan.nextLine();

		// Ensure service was found and cancel tickets it if it was found
		if (validServices.contains(serviceNumber)) {
			System.out.println("Please enter the number of tickets to cancel");
			String numTickets = scan.nextLine();

			//Log transaction in transaction summary file
			String transaction = "CAN" + " " + serviceNumber + " " + numTickets + " " + "00000" + " " + "******" + " "
					+ "*****";
			logInTransactionSummaryFile(transaction);

			System.out.println("Cancelled " + numTickets + "tickets for service " + serviceNumber);
		} else {
			System.out.println("Error! Did not find a matching service!");
		}

		System.out.println("Returning to main menu...");
	}
	

	// Change a ticket
	/**
	 * 
	 */
	private static void changeTicket() {
		System.out.println("Please enter the current service number");
		String serviceNumber = scan.nextLine();

		// Find service to change tickets for
		if(validServices.contains(serviceNumber)) {
			System.out.println("Please enter the new service number");
			String serviceNumber2 = scan.nextLine();
			if(validServices.contains(serviceNumber2)) {
				System.out.println("Please enter the number of tickets to change");
				String numTickets = scan.nextLine();

				//Log transaction in transaction summary file
				String transaction = "CHG" + " " + serviceNumber + " " + numTickets + " " + serviceNumber2 + " " + "****" + " "
						+ "*****";
				logInTransactionSummaryFile(transaction);
				System.out.println("Changed " + numTickets + "tickets for service " + serviceNumber + "to service" + serviceNumber2);
			}else {
				System.out.println("Error! Did not find a matching service!");
			}
		}else {
			System.out.println("Error! Did not find a matching service!");
		}

		System.out.println("Returning to main menu...");
	}
	
	/**
	 * 
	 * @param filePath
	 */

	// Load valid services list
	private static void loadValidServiceFile(String filePath) {
		String serviceNumber;
		BufferedReader fileReader;
		
		try {
			fileReader = new BufferedReader(new FileReader(filePath));
			serviceNumber = fileReader.readLine();
			
			while(!serviceNumber.equals("00000")) {
				//For each entry add new service
				Service service = new Service();
				service.setServiceNumber(serviceNumber);
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

	private static void logInTransactionSummaryFile(String transaction) {
		System.out.println(transaction);	
	}
	// Print the menu for the user
	private static void printMenu() {
		System.out.println("----- MAIN MENU -----");
		System.out.println("Type any of the following commands to continue...");
		System.out.println("logout");
		System.out.println("createService");
		System.out.println("deleteService");
		System.out.println("sellTicket");
		System.out.println("cancelTicket");
		System.out.println("changeTicket");
	}

	private static String getServiceNumber() {
		System.out.println("Please enter the service number");
		return scan.nextLine();
	}
	
	private static String getServiceName() {
		System.out.println("Please enter the service name");
		return scan.nextLine().toLowerCase();
	}
	
	private static String getServiceDate() {
		System.out.println("Please enter the service date");
		return scan.nextLine();
	}
	
	private static String getServiceCapacity() {
		System.out.println("Please enter the service capacity");
		return scan.nextLine();
	}
	
	private static String getNumberOfTickets() {
		System.out.println("Please enter the number number");
		return scan.nextLine();
	}

	private static void logTransaction(String code, String serviceNumber, String ticketsOrCapacity, String serviceNumber2, String serviceName, String serviceDate) {
		
		if(ticketsOrCapacity == null) {
			ticketsOrCapacity = "0";
		}
		
		if(serviceNumber2 == null) {
			serviceNumber2 = "00000";
		}
		
		if(serviceName == null) {
			System.out.println(serviceName);
			serviceName = "****";
		}
		
		if(serviceDate == null) {
			serviceDate = "****";
		}
		
		String transaction = code + " " + serviceNumber + " " + ticketsOrCapacity + " " + serviceNumber2 + " " + serviceName
				+ " " + serviceDate;
		
		logInTransactionSummaryFile(transaction);
	}
}
