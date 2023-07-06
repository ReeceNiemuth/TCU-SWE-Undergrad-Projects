import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//Project done with Matthew Simmons 
public class Lab4Server {

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(25413);

			while (true) {
				Socket socket = serverSocket.accept();

				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				String requestString = reader.readLine();
				String[] requestParts = requestString.split("\\s+");

				String locomotiveType = requestParts[0];
				String seatType = requestParts[1];

				int numberOfAdults = Integer.parseInt(requestParts[2]);
				int numberOfChildrens = Integer.parseInt(requestParts[3]);

				//System.out.println(locomotiveType + " :" + seatType + " :" + numberOfAdults + " :" + numberOfChildrens);

				// create the request object from the input data
				TravelRequest request = new TravelRequest(seatType, locomotiveType, numberOfAdults, numberOfChildrens);

				Lab5ServerThread server = new Lab5ServerThread(socket, request);
				server.start();
			}
		} catch (IOException e) {
			e.printStackTrace();

		}

	}
}

class Lab5ServerThread extends Thread {

	private Socket socket;
	private TravelRequest request;

	// declaring costs based on seat types and locomotion

	// CABOOSE
	private final double CABOOSE_DIESEL = 850.0;
	private final double CABOOSE_STEAM = 950.0;

	// PRESIDENTIAL
	private final double PRESIDENTIAL_DIESEL_ADULT = 77.0;
	private final double PRESIDENTIAL_STEAM_ADULT = 87.0;

	private final double PRESIDENTIAL_DIESEL_CHILD = 57.0;
	private final double PRESIDENTIAL_STEAM_CHILD = 67.0;

	// FIRST CLASS
	private final double FIRST_CLASS_DIESEL_ADULT = 57.0;
	private final double FIRST_CLASS_STEAM_ADULT = 67.0;

	private final double FIRST_CLASS_DIESEL_CHILD = 37.0;
	private final double FIRST_CLASS_STEAM_CHILD = 47.0;

	// OPEN AIR
	private final double OPEN_AIR_DIESEL_ADULT = 27.0;
	private final double OPEN_AIR_STEAM_ADULT = 37.0;

	private final double OPEN_AIR_DIESEL_CHILD = 22.0;
	private final double OPEN_AIR_STEAM_CHILD = 32.0;

	public Lab5ServerThread(Socket socket, TravelRequest request) {
		this.socket = socket;
		this.request = request;
	}

	public void run() {
		try {

			// calculate the cost based on the request
			double cost = calculateCost(request);

			// send the response to the client
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println(cost);

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private double calculateCost(TravelRequest request) {
		double cost = 0.0;

		String seatType = request.getSeatType().toUpperCase();

		switch (seatType) {

		case "CABOOSE":
			if (request.getLocomotiveType().equalsIgnoreCase("DIESEL")) {

				cost += CABOOSE_DIESEL;

			} else if (request.getLocomotiveType().equalsIgnoreCase("STEAM")) {

				cost += CABOOSE_STEAM;

			}
			break;
		case "PRESIDENTIAL":

			if (request.getLocomotiveType().equalsIgnoreCase("DIESEL")) {

				double adultsCost = (PRESIDENTIAL_DIESEL_ADULT * request.getNumberOfAdults());

				double childrensCost = (PRESIDENTIAL_DIESEL_CHILD * request.getNumberOfChildrens());

				cost += (adultsCost + childrensCost);

			} else if (request.getLocomotiveType().equalsIgnoreCase("STEAM")) {

				double adultsCost = (PRESIDENTIAL_STEAM_ADULT * request.getNumberOfAdults());

				double childrensCost = (PRESIDENTIAL_STEAM_CHILD * request.getNumberOfChildrens());

				cost += (adultsCost + childrensCost);

			}
			break;
		case "FIRSTCLASS":
			if (request.getLocomotiveType().equalsIgnoreCase("DIESEL")) {

				double adultsCost = (FIRST_CLASS_DIESEL_ADULT * request.getNumberOfAdults());

				double childrensCost = (FIRST_CLASS_DIESEL_CHILD * request.getNumberOfChildrens());

				cost += (adultsCost + childrensCost);

			} else if (request.getLocomotiveType().equalsIgnoreCase("STEAM")) {

				double adultsCost = (FIRST_CLASS_STEAM_ADULT * request.getNumberOfAdults());

				double childrensCost = (FIRST_CLASS_STEAM_CHILD * request.getNumberOfChildrens());

				cost += (adultsCost + childrensCost);

			}
			break;
		case "OPENAIR":
			if (request.getLocomotiveType().equalsIgnoreCase("DIESEL")) {

				double adultsCost = (OPEN_AIR_DIESEL_ADULT * request.getNumberOfAdults());

				double childrensCost = (OPEN_AIR_DIESEL_CHILD * request.getNumberOfChildrens());

				cost += (adultsCost + childrensCost);

			} else if (request.getLocomotiveType().equalsIgnoreCase("STEAM")) {

				double adultsCost = (OPEN_AIR_STEAM_ADULT * request.getNumberOfAdults());

				double childrensCost = (OPEN_AIR_STEAM_CHILD * request.getNumberOfChildrens());

				cost += (adultsCost + childrensCost);

			}
			break;
		}
		return cost;
	}

}

class TravelRequest {
	private String seatType;
	private String locomotiveType;
	private int numberOfAdults;
	private int numberOfChildrens;

	public TravelRequest(String seatType, String locomotiveTpye, int numberOfAdults, int numberOfChildrens) {
		this.seatType = seatType;
		this.locomotiveType = locomotiveTpye;
		this.numberOfAdults = numberOfAdults;
		this.numberOfChildrens = numberOfChildrens;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getLocomotiveType() {
		return locomotiveType;
	}

	public void setLocomotiveType(String locomotiveType) {
		this.locomotiveType = locomotiveType;
	}

	public int getNumberOfAdults() {
		return numberOfAdults;
	}

	public void setNumberOfAdults(int numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	public int getNumberOfChildrens() {
		return numberOfChildrens;
	}

	public void setNumberOfChildrens(int numberOfChildrens) {
		this.numberOfChildrens = numberOfChildrens;
	}

}

class TravelResponse {
	private double cost;

	public TravelResponse(double cost) {
		this.cost = cost;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

}
