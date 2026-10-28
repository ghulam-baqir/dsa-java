import java.util.*;

/**
 * Bus Ticket Booking System (console based)
 * Companies: Daewoo Express & Faisal Movers
 * Concepts used: OOP, ArrayList, HashMap, arrays, input validation
 *
 * Compile: javac BusTicketBookingSystem.java
 * Run:     java BusTicketBookingSystem
 *
 * NOTE: Fares and timings are SAMPLE data, not real company rates.
 */
public class BusTicketBookingSystem {

    // ===================== Trip =====================
    static class Trip {
        private static final int TOTAL_SEATS = 40; // 10 rows x 4 seats (2 + 2 layout)

        private final int id;
        private final String company;
        private final String from;
        private final String to;
        private final String departure;
        private final String busType;
        private final int fare;
        private final boolean[] booked = new boolean[TOTAL_SEATS + 1]; // index 1..40

        Trip(int id, String company, String from, String to,
             String departure, String busType, int fare) {
            this.id = id;
            this.company = company;
            this.from = from;
            this.to = to;
            this.departure = departure;
            this.busType = busType;
            this.fare = fare;
        }

        int getId() { return id; }
        String getCompany() { return company; }
        String getFrom() { return from; }
        String getTo() { return to; }
        String getDeparture() { return departure; }
        String getBusType() { return busType; }
        int getFare() { return fare; }

        boolean isBooked(int seat) { return booked[seat]; }
        void book(int seat) { booked[seat] = true; }
        void release(int seat) { booked[seat] = false; }

        int availableSeats() {
            int count = 0;
            for (int i = 1; i <= TOTAL_SEATS; i++) {
                if (!booked[i]) count++;
            }
            return count;
        }

        void showSeatMap() {
            System.out.println("\n        --- SEAT MAP (X = booked) ---");
            System.out.println("          [DRIVER]");
            for (int row = 0; row < TOTAL_SEATS / 4; row++) {
                int s = row * 4 + 1;
                System.out.printf("   %s %s     %s %s%n",
                        seatLabel(s), seatLabel(s + 1),
                        seatLabel(s + 2), seatLabel(s + 3));
            }
            System.out.println();
        }

        private String seatLabel(int seat) {
            return booked[seat] ? "[ X]" : String.format("[%2d]", seat);
        }

        @Override
        public String toString() {
            return String.format("%-3d %-15s %-10s -> %-10s %-9s %-10s Rs.%-6d %d seats left",
                    id, company, from, to, departure, busType, fare, availableSeats());
        }
    }

    // ===================== Ticket =====================
    static class Ticket {
        private final String ticketId;
        private final String passengerName;
        private final String phone;
        private final Trip trip;
        private final List<Integer> seats;
        private final int totalFare;

        Ticket(String ticketId, String passengerName, String phone,
               Trip trip, List<Integer> seats) {
            this.ticketId = ticketId;
            this.passengerName = passengerName;
            this.phone = phone;
            this.trip = trip;
            this.seats = new ArrayList<>(seats);
            this.totalFare = trip.getFare() * seats.size();
        }

        String getTicketId() { return ticketId; }
        Trip getTrip() { return trip; }
        List<Integer> getSeats() { return seats; }
        int getTotalFare() { return totalFare; }

        void print() {
            System.out.println("\n==========================================");
            System.out.println("   " + trip.getCompany().toUpperCase() + " - E-TICKET");
            System.out.println("==========================================");
            System.out.println(" Ticket ID  : " + ticketId);
            System.out.println(" Passenger  : " + passengerName);
            System.out.println(" Phone      : " + phone);
            System.out.println(" Route      : " + trip.getFrom() + " -> " + trip.getTo());
            System.out.println(" Departure  : " + trip.getDeparture());
            System.out.println(" Bus Type   : " + trip.getBusType());
            System.out.println(" Seat(s)    : " + seats);
            System.out.println(" Total Fare : Rs. " + totalFare);
            System.out.println("==========================================");
        }
    }

    // ===================== Booking System =====================
    static class BookingSystem {
        private static final int MAX_SEATS_PER_BOOKING = 4;
        private static final double CANCELLATION_FEE = 0.10; // 10%

        private final List<Trip> trips = new ArrayList<>();
        private final Map<String, Ticket> tickets = new HashMap<>();
        private int ticketCounter = 1000;
        private final Scanner sc = new Scanner(System.in);

        BookingSystem() {
            loadSampleTrips();
        }

        private void loadSampleTrips() {
            // Daewoo Express
            trips.add(new Trip(1, "Daewoo Express", "Karachi", "Lahore", "06:00 PM", "Business", 6500));
            trips.add(new Trip(2, "Daewoo Express", "Karachi", "Sukkur", "08:00 PM", "Executive", 3200));
            trips.add(new Trip(3, "Daewoo Express", "Lahore", "Islamabad", "09:00 AM", "Executive", 2200));
            trips.add(new Trip(4, "Daewoo Express", "Lahore", "Multan", "02:00 PM", "Standard", 1800));
            // Faisal Movers
            trips.add(new Trip(5, "Faisal Movers", "Karachi", "Lahore", "05:30 PM", "Executive", 5800));
            trips.add(new Trip(6, "Faisal Movers", "Karachi", "Sukkur", "09:30 PM", "Standard", 2800));
            trips.add(new Trip(7, "Faisal Movers", "Sukkur", "Lahore", "07:00 PM", "Executive", 3600));
            trips.add(new Trip(8, "Faisal Movers", "Islamabad", "Peshawar", "11:00 AM", "Standard", 1500));

            // Pre-book some random seats so the bus looks realistic
            Random rand = new Random(42);
            for (Trip t : trips) {
                int pre = 5 + rand.nextInt(12);
                for (int i = 0; i < pre; i++) {
                    t.book(1 + rand.nextInt(40));
                }
            }
        }

        // ---------- input helpers ----------
        private int readInt(String prompt, int min, int max) {
            while (true) {
                System.out.print(prompt);
                String line = sc.nextLine().trim();
                try {
                    int v = Integer.parseInt(line);
                    if (v >= min && v <= max) return v;
                } catch (NumberFormatException ignored) { }
                System.out.println("  Invalid input. Enter a number between " + min + " and " + max + ".");
            }
        }

        private String readNonEmpty(String prompt) {
            while (true) {
                System.out.print(prompt);
                String s = sc.nextLine().trim();
                if (!s.isEmpty()) return s;
                System.out.println("  This field cannot be empty.");
            }
        }

        private String readPhone(String prompt) {
            while (true) {
                String p = readNonEmpty(prompt);
                if (p.matches("03\\d{9}")) return p; // e.g. 03001234567
                System.out.println("  Enter a valid mobile number like 03001234567.");
            }
        }

        private Trip findTrip(int id) {
            for (Trip t : trips) {
                if (t.getId() == id) return t;
            }
            return null;
        }

        // ---------- features ----------
        void showTrips(List<Trip> list) {
            if (list.isEmpty()) {
                System.out.println("\nNo trips found.");
                return;
            }
            System.out.println("\nID  Company         From       -> To         Time      Type       Fare");
            System.out.println("-------------------------------------------------------------------------------");
            for (Trip t : list) System.out.println(t);
        }

        void searchTrips() {
            System.out.print("From city (Enter for all): ");
            String from = sc.nextLine().trim();
            System.out.print("To city   (Enter for all): ");
            String to = sc.nextLine().trim();

            List<Trip> result = new ArrayList<>();
            for (Trip t : trips) {
                boolean f = from.isEmpty() || t.getFrom().equalsIgnoreCase(from);
                boolean d = to.isEmpty() || t.getTo().equalsIgnoreCase(to);
                if (f && d) result.add(t);
            }
            showTrips(result);
        }

        void bookTicket() {
            showTrips(trips);
            int tripId = readInt("\nEnter Trip ID to book (0 to go back): ", 0, trips.size());
            if (tripId == 0) return;

            Trip trip = findTrip(tripId);
            if (trip == null) {
                System.out.println("Trip not found.");
                return;
            }
            if (trip.availableSeats() == 0) {
                System.out.println("Sorry, this bus is full.");
                return;
            }

            trip.showSeatMap();
            int maxAllowed = Math.min(MAX_SEATS_PER_BOOKING, trip.availableSeats());
            int count = readInt("How many seats? (1-" + maxAllowed + "): ", 1, maxAllowed);

            List<Integer> chosen = new ArrayList<>();
            while (chosen.size() < count) {
                int seat = readInt("Choose seat #" + (chosen.size() + 1) + " (1-40): ", 1, 40);
                if (trip.isBooked(seat) || chosen.contains(seat)) {
                    System.out.println("  Seat " + seat + " is not available. Pick another.");
                } else {
                    chosen.add(seat);
                }
            }

            String name = readNonEmpty("Passenger name: ");
            String phone = readPhone("Mobile number: ");

            int total = trip.getFare() * count;
            System.out.println("\nSeats " + chosen + " | Total fare: Rs. " + total);
            System.out.print("Confirm booking? (y/n): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("Booking cancelled.");
                return;
            }

            for (int s : chosen) trip.book(s);

            String prefix = trip.getCompany().startsWith("Daewoo") ? "DW" : "FM";
            String ticketId = prefix + (++ticketCounter);
            Ticket ticket = new Ticket(ticketId, name, phone, trip, chosen);
            tickets.put(ticketId, ticket);

            System.out.println("\nBooking successful!");
            ticket.print();
        }

        void viewTicket() {
            String id = readNonEmpty("Enter Ticket ID (e.g. DW1001): ").toUpperCase();
            Ticket t = tickets.get(id);
            if (t == null) System.out.println("No ticket found with ID " + id);
            else t.print();
        }

        void cancelTicket() {
            String id = readNonEmpty("Enter Ticket ID to cancel: ").toUpperCase();
            Ticket t = tickets.get(id);
            if (t == null) {
                System.out.println("No ticket found with ID " + id);
                return;
            }
            int fee = (int) (t.getTotalFare() * CANCELLATION_FEE);
            int refund = t.getTotalFare() - fee;

            System.out.println("Cancellation fee (10%): Rs. " + fee);
            System.out.println("Refund amount         : Rs. " + refund);
            System.out.print("Confirm cancellation? (y/n): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("Cancellation aborted.");
                return;
            }

            for (int s : t.getSeats()) t.getTrip().release(s);
            tickets.remove(id);
            System.out.println("Ticket " + id + " cancelled. Seats released.");
        }

        void viewAllBookings() {
            if (tickets.isEmpty()) {
                System.out.println("\nNo bookings made in this session yet.");
                return;
            }
            for (Ticket t : tickets.values()) t.print();
        }

        // ---------- menu ----------
        void run() {
            System.out.println("==========================================");
            System.out.println("   BUS TICKET BOOKING SYSTEM");
            System.out.println("   Daewoo Express  |  Faisal Movers");
            System.out.println("==========================================");

            while (true) {
                System.out.println("\n1. View all trips");
                System.out.println("2. Search trips by route");
                System.out.println("3. Book ticket");
                System.out.println("4. View ticket");
                System.out.println("5. Cancel ticket");
                System.out.println("6. View all my bookings");
                System.out.println("0. Exit");
                int choice = readInt("Choose option: ", 0, 6);

                switch (choice) {
                    case 1: showTrips(trips); break;
                    case 2: searchTrips(); break;
                    case 3: bookTicket(); break;
                    case 4: viewTicket(); break;
                    case 5: cancelTicket(); break;
                    case 6: viewAllBookings(); break;
                    case 0:
                        System.out.println("Thank you for travelling with us. Safar bakhair!");
                        return;
                }
            }
        }
    }

    public static void main(String[] args) {
        new BookingSystem().run();
    }
}