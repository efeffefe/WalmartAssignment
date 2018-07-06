import java.awt.Dimension;
import java.io.*;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Scanner;

public class Theatre {

	private ArrayList<SeatHold> seatHolds;
	private Seat[][] seating;
	private int seatHoldCount = 0;
	private int seatsAvailable;
	private LinkedList<String> stage;
	
	public Theatre(File f) {
		Scanner sc = null;
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException for file: " + f.getName());
		}
		
		seatHolds = new ArrayList<SeatHold>();
		LinkedList<Dimension> seats = new LinkedList<Dimension>();
		stage = new LinkedList<String>();
		int row = 0;
		int maxColumns = 0;
		while(sc.hasNextLine()) {
			String rowData = sc.nextLine();
			if(rowData.contains("s")) {
				maxColumns = Math.max(maxColumns, rowData.length());
				for(int col = 0; col < rowData.length(); col++) {
					if(rowData.charAt(col) == 's') {
						Dimension seatLocation = new Dimension(row, col);
						seats.add(seatLocation);
					}
				}
				row++;
			}else {
				stage.add(rowData);
			}
		}
		seatsAvailable = seats.size();
		seating = new Seat[row][maxColumns];
		for(Dimension seatLocation : seats) {
			seating[seatLocation.width][seatLocation.height] = new Seat(this);
		}
	}

	public boolean isAvailable(int row, int col) {
		if(seating[row][col] != null) {
			return seating[row][col].getState() == Seat.AVAILABLE;
		}
		return false;
	}
	
	private void holdSeat(int row, int col, String email) {
		if(isAvailable(row, col)) {
			seating[row][col].hold(email);
			seatsAvailable--;
		}
	}
	
	public int numSeatsAvailable() {
		return seatsAvailable;
	}
	
	public void notifyAvailableSeat() {
		seatsAvailable++;
	}
	
	public SeatHold getSeatHold(int seatHoldID) {
		if(seatHolds.size() > seatHoldID) {
			return seatHolds.get(seatHoldID); 
		}
		System.err.println(seatHoldID + " is not a valid SeatHold ID");
		return null;
	}
	
	public String reserveSeats(int seatHoldID, String email) {
		SeatHold hold = getSeatHold(seatHoldID);
		if(hold == null) {
			return "";
		}
		if(!hold.getEmail().equals(email)) {
			return email+" cannot reserve these seats because the SeatHold with ID "+ seatHoldID +" was held by " + hold.getEmail() +".";
		}
		hold.reserve();
		return "Seat Reservation Confirmation: \"" + hold.toString()+"\"";
		
	}
	
	public SeatHold findSeats(int numSeats, String email) {
		// Check if there are enough available seats to fulfill the request:
		int seatsAvailable = numSeatsAvailable();
		if(numSeats <= 0) {
			System.err.println("Invalid number request. Number of seats must be positive.");
			return null;
		}
		if(seatsAvailable < numSeats) {
			System.err.println("Cannot hold "+numSeats+" seats; There are only "+seatsAvailable + " available.");
			return null;
		}
		
		// Search for numSeats consecutive seats in the theater to hold:
		SeatHold seatHold = new SeatHold(email, seatHoldCount);
		seatHold.setID(seatHoldCount++);
		if(numSeats < seating[0].length) {
			System.out.println(".");
			for(int row = 0; row < seating.length; row++) {
				boolean consecutive = true;
				for(int col = 0; col < seating[0].length - numSeats; col++) {
					for(int k = col; k < col+numSeats; k++) {
						if(!isAvailable(row, k)) {
							consecutive = false;
							break;
						}
					}	
					if(consecutive) {
						for(int k = col; k < numSeats+col; k++) {
							holdSeat(row, k, email);
							seatHold.addSeat(seating[row][k], new Dimension(row, k));
						}
						seatHolds.add(seatHold);
						return seatHold;
					}
					consecutive = true;
				}
			}
		}
		
		//If the hold request exceeds the amount of seats in each row then hold seats in order as available:
		for(int i = 0; i < seating.length && numSeats > 0; i++) {
			for(int j = 0; j < seating[0].length && numSeats > 0; j++) {
				if(isAvailable(i, j)) {
					holdSeat(i, j, email);
					seatHold.addSeat(seating[i][j], new Dimension(i, j));
					numSeats--;
				}	
			}
		}
		seatHolds.add(seatHold);
		return seatHold;
	}
		
	
	public void print() {
		for(String s : stage) {
			System.out.println(s);
		}
		for(int i = 0; i < seating.length; i++) {
			for(int j = 0; j < seating[i].length; j++) {
				if(seating[i][j] instanceof Seat) {
					if(seating[i][j].getState() == Seat.HELD) {
						System.out.print("S");
					}else if(seating[i][j].getState() == Seat.AVAILABLE) {
						System.out.print("s");
					}
					else if(seating[i][j].getState() == Seat.RESERVED) {
						System.out.print("x");
					}
				}else {
					System.out.print("-");
				}
			}
			System.out.println();
		}
		System.out.println("\n\n");
	}
	
	public void printLegend() {
		System.out.println("Legend:\t\'s\' => Available\n\t\'S\' => Held\n\t\'x\' => Reserved\n\t'-' => stage/no seats\n\n");
	}
}
