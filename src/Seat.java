public class Seat {

	public static final String AVAILABLE = "AVAILABLE";
	public static final String HELD = "HELD";
	public static final String RESERVED = "RESERVED";
	public static final int MAX_HOLD_TIME = 30000;
	
	private Theatre t;
	private long holdTime = 0;
	private String heldBy = "";
	private String state;
	
	public Seat(Theatre t, String state) {
		this.state = state;
		this.t = t;
	}                                                                                                                 

	public Seat(Theatre t) {
		this(t, AVAILABLE);
	}

	public String getState() {
		if(state == Seat.HELD) {
			if(System.currentTimeMillis() - holdTime > MAX_HOLD_TIME && holdTime > 0) {
				state = AVAILABLE;
				t.notifyAvailableSeat();
				heldBy = "";
			}
		}
		return state;
	}

	public void hold(String email) {
		state = Seat.HELD;
		holdTime = System.currentTimeMillis();
		heldBy = email;
	}
	
	public boolean isHeldBy(String email) {
		return heldBy == email && getState() == "HELD";
	}
	
	public boolean reserve(String requesterEmail) {
		if(getState() == Seat.HELD && isHeldBy(requesterEmail)) {
			state = Seat.RESERVED;
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "Seat = {state: " +state+"}\t";
	}
}