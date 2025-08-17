import java.io.*;
import java.util.*;

class User implements Serializable {
    String username;
    String password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Ticket implements Serializable {
    int seatNumber;
    String bookedBy;

    Ticket(int seatNumber) {
        this.seatNumber = seatNumber;
        this.bookedBy = null;
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

    static final String USER_FILE = "users.txt";
    static final String TICKET_FILE = "tickets.txt";

    public static void main(String[] args) {
        loadData();

        while (true) {
            if (currentUser == null) {
                System.out.println("\n--- 🎟 Welcome to Online Ticket Booking System ---");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1: register(); break;
                    case 2: login(); break;
                    case 3: saveData(); System.exit(0);
                    default: System.out.println("❌ Invalid choice!");
                }
            } else {
                showMenu();
            }
        }
    }

    static void register() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        // check duplicate
        for (User u : users) {
            if (u.username.equals(username)) {
                System.out.println("⚠ Username already exists, choose another!");
                return;
            }
        }

        System.out.print("Enter password: ");
        String password = sc.nextLine();
        users.add(new User(username, password));
        System.out.println("✅ Registration successful!");
        saveData();
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
            default: System.out.println("❌ Invalid choice!");
        }
    }

    static void viewAvailableSeats() {
        System.out.println("\n--- Seat Status ---");
        for (Ticket t : tickets) {
            if (t.isAvailable()) {
                System.out.print("[Seat " + t.seatNumber + " - ✅ Available] ");
            } else {
                System.out.print("[Seat " + t.seatNumber + " - ❌ Booked by " + t.bookedBy + "] ");
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
                    saveData();
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
                saveData();
                return;
            }
        }
        System.out.println("❌ You have not booked this seat or invalid seat number!");
    }

    // ---------------- File Handling ----------------
    @SuppressWarnings("unchecked")
    static void loadData() {
        // Load users
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            users = (List<User>) ois.readObject();
        } catch (Exception e) {
            users = new ArrayList<>();
        }

        // Load tickets
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TICKET_FILE))) {
            tickets = (List<Ticket>) ois.readObject();
        } catch (Exception e) {
            // if file not found then initialize 10 seats
            tickets = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                tickets.add(new Ticket(i));
            }
        }
    }

    static void saveData() {
        // Save users
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save tickets
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TICKET_FILE))) {
            oos.writeObject(tickets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
