import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class User {
    private String userId;
    private String pin;
    private double balance;
    private List<String> transactionHistory;

    public User(String userId, String pin) {
        this.userId = userId;
        this.pin = pin;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public boolean validatePin(String pin) {
        return this.pin.equals(pin);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposited: $" + amount);
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add("Withdrew: $" + amount);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void transfer(User recipient, double amount) {
        if (amount <= balance) {
            balance -= amount;
            recipient.deposit(amount);
            transactionHistory.add("Transferred: $" + amount + " to " + recipient.getUserId());
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
}

class Bank {
    private Map<String, User> users;

    public Bank() {
        users = new HashMap<>();
    }

    public void addUser(String userId, String pin) {
        users.put(userId, new User(userId, pin));
    }

    public User authenticate(String userId, String pin) {
        User user = users.get(userId);
        if (user != null && user.validatePin(pin)) {
            return user;
        }
        return null;
    }
}

class ATM {
    private Bank bank;
    private User currentUser;
    private Scanner scanner;

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the ATM");
        authenticateUser();
        showMenu();
    }

    private void authenticateUser() {
        while (currentUser == null) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();
            currentUser = bank.authenticate(userId, pin);
            if (currentUser == null) {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }

    private void showMenu() {
        int choice = 0;
        while (choice != 5) {
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    showTransactionHistory();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    transfer();
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showTransactionHistory() {
        System.out.println("Transaction History:");
        for (String transaction : currentUser.getTransactionHistory()) {
            System.out.println(transaction);
        }
    }

    private void withdraw() {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        currentUser.withdraw(amount);
    }

    private void deposit() {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        currentUser.deposit(amount);
    }

    private void transfer() {
        System.out.print("Enter recipient User ID: ");
        String recipientId = scanner.next();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        User recipient = bank.authenticate(recipientId, ""); // Simulating retrieval of the user by ID
        if (recipient != null) {
            currentUser.transfer(recipient, amount);
        } else {
            System.out.println("Recipient not found.");
        }
    }
}

public class ATMInterface {
    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.addUser("user1", "1234");
        bank.addUser("user2", "5678");

        ATM atm = new ATM(bank);
        atm.start();
    }
}
