package frontEnd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

public class frontEndSystem {
	// ----- Class Variables ----- //
	private static ArrayList<Service> services = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	// private boolean planner_mode = false;

	// ----- Main Execution of Front End System ----- //
	public static void main(String[] args) {
		// When system starts, automatically ask to login
		login();

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
				System.out.println("Error! Incorrect input please try again...");
			}
		}
	}

	// ----- Methods ----- //
	// Login to the system
	private static void login() {
		String input = "";
		System.out.println("Type 'login' to access the system...");

		// Get input and check if correct for login
		input = scan.nextLine();
		while (!input.equals("login")) {
			System.out.println("Incorrect input! Please try again...");
			input = scan.nextLine();
		}

		input = ""; // Reset input

		// Get input to check for agent / planner mode
		System.out.println("What mode would you like to use?");
		input = scan.nextLine();
		while (!input.equals("agent") && !input.equals("planner")) {
			System.out.println("Incorrect input! Please try again...");
			input = scan.nextLine();
		}

		// later, add a check to set planner mode if selected
		// i.e if(input == "planner") { planner_mode = true; }

		System.out.println("Logging in... Success!");
		printMenu();
	}

	// Logout and end system
	private static void logout() {
		System.out.println("Logging out... system will shut down...");
		System.exit(0);
	}

	// Create a service
	private static void createService() {
		String input1 = "";
		String input2 = "";
		String input3 = "";

		System.out.println("Please enter the service number");
		input1 = scan.nextLine();

		System.out.println("Please enter the service date");
		input2 = scan.nextLine();

		System.out.println("Please enter the service name");
		input3 = scan.nextLine();

		// Create service from inputs and add it to the services list
		Service new_service = new Service(input1, input2, input3);
		services.add(new_service);

		System.out.println("Service successfully created!");
		System.out.println("Returning to main menu...");
	}

	// Delete a service
	private static void deleteService() {
		String input1 = "";
		String input2 = "";

		System.out.println("Please enter the service number");
		input1 = scan.nextLine();

		System.out.println("Please enter the service name");
		input2 = scan.nextLine();

		// Find service to delete
		int index = -1;
		for (int i = 0; i < services.size(); i++) {
			if (services.get(i).getServiceNumber().equals(input1) && services.get(i).getServiceName().equals(input2)) {
				index = i;
				break;
			}
		}

		// Ensure service was found and delete it if it was found
		if (index != -1) {
			// Figure out how we tell the backend to remove the service here
			// STUFF

			System.out.println("Deleted service number " + input1 + ": " + input2);
		} else {
			System.out.println("Error! Did not find a matching service!");
		}

		System.out.println("Returning to main menu...");
	}

	// Sell a ticket
	private static void sellTicket() {
		String input1 = "";
		String input2 = "";

		System.out.println("Please enter the service number");
		input1 = scan.nextLine();

		// Find service to sell tickets for
		int index = -1;
		for (int i = 0; i < services.size(); i++) {
			if (services.get(i).getServiceNumber().equals(input1)) {
				index = i;
				break;
			}
		}

		// Ensure service was found and sell tickets it if it was found
		if (index != -1) {
			System.out.println("Please enter the number of tickets to sell");
			input2 = scan.nextLine();

			int number = Integer.parseInt(services.get(index).getNumberOfTickets());
			number += Integer.parseInt(input2);
			services.get(index).setNumberOfTickets(Integer.toString(number));

			System.out.println("Sold " + input2 + "tickets for service " + input1);
		} else {
			System.out.println("Error! Did not find a matching service!");
		}

		System.out.println("Returning to main menu...");
	}

	// Cancel a ticket
	private static void cancelTicket() {
		String input1 = "";
		String input2 = "";

		System.out.println("Please enter the service number");
		input1 = scan.nextLine();

		// Find service to cancel tickets for
		int index = -1;
		for (int i = 0; i < services.size(); i++) {
			if (services.get(i).getServiceNumber().equals(input1)) {
				index = i;
				break;
			}
		}

		// Ensure service was found and cancel tickets it if it was found
		if (index != -1) {
			System.out.println("Please enter the number of tickets to cancel");
			input2 = scan.nextLine();

			int number = Integer.parseInt(services.get(index).getNumberOfTickets());
			number -= Integer.parseInt(input2);
			services.get(index).setNumberOfTickets(Integer.toString(number));

			System.out.println("Cancelled " + input2 + "tickets for service " + input1);
		} else {
			System.out.println("Error! Did not find a matching service!");
		}

		System.out.println("Returning to main menu...");
	}

	// Change a ticket
	private static void changeTicket() {
		String input1 = "";
		String input2 = "";
		String input3 = "";

		System.out.println("Please enter the current service number");
		input1 = scan.nextLine();

		// Find service to change tickets for
		int index1 = -1;
		for (int i = 0; i < services.size(); i++) {
			if (services.get(i).getServiceNumber().equals(input1)) {
				index1 = i;
				break;
			}
		}

		// Ensure service was found and check for new service existance
		if (index1 != -1) {
			System.out.println("Please enter the new service number");
			input2 = scan.nextLine();

			// Find service to change tickets to
			int index2 = -1;
			for (int i = 0; i < services.size(); i++) {
				if (services.get(i).getServiceNumber().equals(input2)) {
					index2 = i;
					break;
				}
			}

			// Ensure new service was found and then change tickets
			if (index2 != -1) {
				System.out.println("Please enter the number of tickets to change");
				input3 = scan.nextLine();

				int number = 0;
				// Reduce tickets on existing service
				number = Integer.parseInt(services.get(index1).getNumberOfTickets());
				number -= Integer.parseInt(input3);
				services.get(index1).setNumberOfTickets(Integer.toString(number));

				// Increase tickets on new service
				number = Integer.parseInt(services.get(index2).getNumberOfTickets());
				number += Integer.parseInt(input3);
				services.get(index2).setNumberOfTickets(Integer.toString(number));

				System.out.println("Changed " + input3 + "tickets for service " + input1 + "to service" + input2);
			}
		} else {
			System.out.println("Error! Did not find a matching service!");
		}

		System.out.println("Returning to main menu...");
	}

	// Load valid services list
	private static void loadFile() {
		// Some code to iterate through newline(?) delineated file
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

}
