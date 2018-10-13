package FrontEnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TSF{

	File log = new File("TSF.txt");
	PrintWriter pw;
	
	public TSF(){
		try {
			this.pw = new PrintWriter(log);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	void logTransaction(String serviceCode, String serviceNumber, String ticketsOrCapacity,String serviceNumber2, String serviceName, String serviceDate) {

		if (ticketsOrCapacity == null) {
			ticketsOrCapacity = "0";
		}

		if (serviceNumber2 == null) {
			serviceNumber2 = "00000";
		}

		if (serviceName == null) {
			serviceName = "****";
		}

		if (serviceDate == null) {
			serviceDate = "****";
		}

		String transaction = serviceCode + " " + serviceNumber + " " + ticketsOrCapacity + " " + serviceNumber2 + " "
				+ serviceName + " " + serviceDate;

		pw.write(transaction+"\n");
		System.out.println("Logged: "+ transaction);
	}
	
	public void printTSF() {
		pw.write("EOS");
	}
	public void closeLog() {
		pw.close();
	}

}
