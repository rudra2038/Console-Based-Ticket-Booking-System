import java.util.*;

class User {
    String username;
    String password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Ticket {
    int seatNumber;
    String bookedBy;

    Ticket(int seatNumber) {
        this.seatNumber = seatNumber;
        this.bookedBy = null; // initially seat is empty
    }

    boolean isAvailable() {
        return bookedBy == null;
    }
}

public class TicketBookingSystem {
    static Scanner sc = new Scanner(System.in);
    static List<User> users = new ArrayList<>();
    static List<Ticket> tickets = new ArrayList<>();
    static User currentUser = null;

    public static void main(String[] args) {
        // create 10 seats initially
        for (int i = 1; i <= 10; i++) {
            tickets.add(new Ticket(i));
        }

        while (true) {
            if (currentUser == null) {
                System.out.println("\n--- Welcome to Online Ticket Booking ---");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1: register(); break;
                    case 2: login(); break;
                    case 3: System.exit(0);
                    default: System.out.println("Invalid choice!");
                }
            } else {
                showMenu();
            }
        }
    }

    static void register() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        users.add(new User(username, password));
        System.out.println("✅ Registration successful!");
    }

    static void login() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        for (User u : users) {
            if (u.username.equals(username) && u.password.equals(password)) {
                currentUser = u;
                System.out.println("✅ Login successful! Welcome " + username);
                return;
            }
        }
        System.out.println("❌ Invalid username or password!");
    }

    static void showMenu() {
        System.out.println("\n--- Ticket Booking Menu ---");
        System.out.println("1. View Available Seats");
        System.out.println("2. Book a Ticket");
        System.out.println("3. Cancel a Ticket");
        System.out.println("4. Logout");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1: viewAvailableSeats(); break;
            case 2: bookTicket(); break;
            case 3: cancelTicket(); break;
            case 4: currentUser = null; break;
            default: System.out.println("Invalid choice!");
        }
    }

    static void viewAvailableSeats() {
        System.out.println("\n--- Available Seats ---");
        for (Ticket t : tickets) {
            if (t.isAvailable()) {
                System.out.print("[Seat " + t.seatNumber + " - Available] ");
            } else {
                System.out.print("[Seat " + t.seatNumber + " - Booked by " + t.bookedBy + "] ");
            }
        }
        System.out.println();
    }

    static void bookTicket() {
        viewAvailableSeats();
        System.out.print("\nEnter seat number to book: ");
        int seat = sc.nextInt();

        for (Ticket t : tickets) {
            if (t.seatNumber == seat) {
                if (t.isAvailable()) {
                    t.bookedBy = currentUser.username;
                    System.out.println("🎉 Ticket booked successfully for Seat " + seat);
                } else {
                    System.out.println("❌ Seat already booked!");
                }
                return;
            }
        }
        System.out.println("❌ Invalid seat number!");
    }

    static void cancelTicket() {
        System.out.print("Enter seat number to cancel: ");
        int seat = sc.nextInt();

        for (Ticket t : tickets) {
            if (t.seatNumber == seat && currentUser.username.equals(t.bookedBy)) {
                t.bookedBy = null;
                System.out.println("✅ Ticket cancelled for Seat " + seat);
                return;
            }
        }
        System.out.println("❌ You have not booked this seat or invalid seat number!");
    }
}
