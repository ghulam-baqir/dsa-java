import java.util.Scanner;

/* Each node is one booked ticket */
class Ticket {
    int id;
    String customer;
    String movie;
    int seat;
    int price;
    Ticket next;

    Ticket(int id, String customer, String movie, int seat, int price) {
        this.id = id;
        this.customer = customer;
        this.movie = movie;
        this.seat = seat;
        this.price = price;
        this.next = null;
    }

    @Override
    public String toString() {
        return String.format("ID: %-3d | %-15s | %-20s | Seat: %-2d | Rs. %d",
                id, customer, movie, seat, price);
    }
}

/* Singly linked list that stores the bookings */
class TicketList {
    private Ticket head;
    private int nextId = 1;
    static final int MAX_SEATS = 50;
    static final int TICKET_PRICE = 800;

    boolean isSeatTaken(String movie, int seat) {
        Ticket cur = head;
        while (cur != null) {
            if (cur.movie.equalsIgnoreCase(movie) && cur.seat == seat)
                return true;
            cur = cur.next;
        }
        return false;
    }

    /* Insert at end */
    void bookTicket(String customer, String movie, int seat) {
        if (seat < 1 || seat > MAX_SEATS) {
            System.out.println("Invalid seat number.");
            return;
        }
        if (isSeatTaken(movie, seat)) {
            System.out.println("Seat " + seat + " is already booked for \"" + movie + "\".");
            return;
        }

        Ticket newNode = new Ticket(nextId++, customer, movie, seat, TICKET_PRICE);
        if (head == null) {
            head = newNode;
        } else {
            Ticket cur = head;
            while (cur.next != null)
                cur = cur.next;
            cur.next = newNode;
        }
        System.out.println("Ticket booked! Ticket ID: " + newNode.id);
    }

    /* Delete a node by ticket ID */
    void cancelTicket(int id) {
        Ticket cur = head, prev = null;
        while (cur != null && cur.id != id) {
            prev = cur;
            cur = cur.next;
        }
        if (cur == null) {
            System.out.println("Ticket " + id + " not found.");
            return;
        }
        if (prev == null)
            head = cur.next;       // deleting the first node
        else
            prev.next = cur.next;

        System.out.println("Ticket " + id + " cancelled.");
    }

    void searchTicket(int id) {
        Ticket cur = head;
        while (cur != null) {
            if (cur.id == id) {
                System.out.println(cur);
                return;
            }
            cur = cur.next;
        }
        System.out.println("Ticket " + id + " not found.");
    }

    void displayTickets() {
        if (head == null) {
            System.out.println("No bookings yet.");
            return;
        }
        System.out.println("\n--- All Bookings ---");
        Ticket cur = head;
        while (cur != null) {
            System.out.println(cur);
            cur = cur.next;
        }
    }
}

public class CinemaTicketSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TicketList list = new TicketList();
        int choice;

        do {
            System.out.println("\n===== Cinema Ticket System =====");
            System.out.println("1. Book ticket");
            System.out.println("2. Cancel ticket");
            System.out.println("3. Search ticket");
            System.out.println("4. Display all bookings");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            choice = Integer.parseInt(sc.nextLine().trim());

            switch (choice) {
                case 1:
                    System.out.print("Customer name: ");
                    String name = sc.nextLine();
                    System.out.print("Movie name: ");
                    String movie = sc.nextLine();
                    System.out.print("Seat number (1-" + TicketList.MAX_SEATS + "): ");
                    int seat = Integer.parseInt(sc.nextLine().trim());
                    list.bookTicket(name, movie, seat);
                    break;
                case 2:
                    System.out.print("Enter ticket ID to cancel: ");
                    list.cancelTicket(Integer.parseInt(sc.nextLine().trim()));
                    break;
                case 3:
                    System.out.print("Enter ticket ID to search: ");
                    list.searchTicket(Integer.parseInt(sc.nextLine().trim()));
                    break;
                case 4:
                    list.displayTickets();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        sc.close();
    }
}