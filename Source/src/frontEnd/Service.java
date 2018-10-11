package frontEnd;

public class Service {
	// ----- Class variables ----- /
	private String service_number;
	private String service_date;
	private String service_name;
	private String number_of_tickets;
	
	// ----- Constructors ----- //
	Service() {
		service_number = "#####";
		service_date = "MMDDYY";
		service_name = "blank";
		number_of_tickets = "0";
	}
	
	Service(String number, String date, String name) {
		service_number = number;
		service_date = date;
		service_name = name;
		number_of_tickets = "0";
	}	
	
	// ----- Accessors ----- //
	public String getServiceNumber() {
		return service_number;		
	}
	
	public String getServiceDate() {
		return service_date;		
	}
	
	public String getServiceName() {
		return service_name;		
	}
	
	public String getNumberOfTickets() {
		return number_of_tickets;		
	}
	
	// ----- Mutators ----- //
	public void setServiceNumber(String number) {
		service_number = number;
	}
	
	public void setServiceDate(String date) {
		service_date = date;
	}
	
	public void setServiceName(String name) {
		service_name = name;
	}
	
	public void setNumberOfTickets(String number) {
		number_of_tickets = number;
	}
	
}
