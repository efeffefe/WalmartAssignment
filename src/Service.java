import java.io.File;
import java.util.Scanner;

public class Service implements TicketService{
	
	private Theatre t;
	
    public static void main(String[] args) {
    	StringBuilder fileName = new StringBuilder(new File("").getAbsolutePath() +  "/input/");
    	if(args.length == 1) {
    		fileName.append(args[0]);
    		System.out.println("***=>"+fileName);
    	}else {
    		fileName.append("default.txt");
    		System.out.println("***=>"+fileName);
    		System.err.println("Using default.txt as the seating file.");
    	}	
     	File f = new File(fileName.toString());
    	Theatre t = new Theatre(f);
    	runCLI(t);
    }
    
    public Service(Theatre t) {
    	this.t = t;
    }

	@Override
	public int numSeatsAvailable() {
		return t.numSeatsAvailable();
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		return t.findSeats(numSeats, customerEmail);
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		return t.reserveSeats(seatHoldId, customerEmail);
	}
	
	public static void runCLI(Theatre t) {
    	System.out.println("CLI commands: \n\t'hold <num_seats> <customer_email>\'");
    	System.out.println("\t\t=>finds and hold <num_seats> seats for customer <customer_email>");
    	System.out.println("\t\t=>Hold expires after " + Seat.MAX_HOLD_TIME / 1000 +" seconds. returns SeatHoldID");
    	System.out.println("\t\t=>example: hold 10 email@example.com");
    	System.out.println("\t'reserve <SeatHold_ID> <customer_email>\'");
    	System.out.println("\t\t=>reserves seat hold for <customer_email>");
    	System.out.println("\t\t=>returns Confirmation of reservation\n");
    	System.out.println("\t'available'");
    	System.out.println("\t\t=>returns number of available seats");
    	t.printLegend();
    	
    	Service s = new Service(t);
    	String input = "";
    	Scanner sc = new Scanner(System.in);
    	do {
    		t.print();
    		System.out.print("Enter command: ");
    		input = sc.nextLine();
    		if(input.startsWith("hold")) {
    			String[] split = input.split(" ");
    			if(split.length != 3) {
    				System.err.println("'hold' takes 2 parameters. Format: 'hold <number_of_seats> <customer_email>'");
    				continue;
    			}
    			int num_seats = Integer.parseInt(split[1]);
    			String customer_email = split[2];
    			System.out.println("Output: ");
    			SeatHold sh = s.findAndHoldSeats(num_seats, customer_email);
    			System.out.println(sh);
    		}
    		else if(input.startsWith("reserve")) {
    			String[] split = input.split(" ");
    			if(split.length != 3) {
    				System.err.println("Invalid command call. Format: 'reserve <SeatHoldID> <customer_email>'");
    				continue;
    			}
    			int sh_ID = Integer.parseInt(split[1]);
    			String customer_email = split[2];
    			String confirmation = s.reserveSeats(sh_ID, customer_email);
    			System.out.println("Output: \n"+confirmation);
    		}
    		else if(input.equals("available")) {
    			System.out.println("Output: \n" + s.numSeatsAvailable() +" seats available.");
    		}
    		else if(!(input.equals("quit") || input.equals("q"))) {
    			System.err.println("Unknown command: " + input);
    		}
    		System.out.println("\n");   
    	}while(!(input.equals("quit") || input.equals("q")));
    	sc.close();
	}
}
