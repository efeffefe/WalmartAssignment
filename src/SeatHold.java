import java.awt.Dimension;
import java.util.HashMap;

public class SeatHold {

	private String email;
	private HashMap<Seat, Dimension> seats;
	private int ID;
	
	public SeatHold(String email, int ID) {
		this.setEmail(email);
		this.seats = new HashMap<Seat, Dimension>();
	}
	
	public void addSeat(Seat s, Dimension location) {
		seats.put(s, location);
	}
	
	public int numSeatHolds() {
		return seats.size();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}
	
	public boolean reserve() {
		for(Seat s : seats.keySet()) {
			boolean successfulReservation = s.reserve(email);
			if(!successfulReservation) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isReserved() {
		for(Seat s : seats.keySet()) {
			if(s.getState() == Seat.RESERVED) {
				return true;
			}
		}
		return false;
	}
	
	
	public String toString() {
		return "SeatHold ID = " + getID()+"\n" +getEmail()+ " has " + seats.size()+" seats "+ (isReserved() ? "reserved" : "held") +" at seat locations: " + seats.values();
	}

}
