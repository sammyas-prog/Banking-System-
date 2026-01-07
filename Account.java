import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Account {
    private static final double INTEREST_RATE = 0.05; // 5% interest rate

    private static class Transaction {
        private String type;
        private double amount;
        private String dateTime;

        public Transaction(String type, double amount) {
            this.type = type;
            this.amount = amount;
            
            this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        @Override
        public String toString() {
            return dateTime + " - " + type + ": $" + amount;
        }
    }

    private String accountNumber;
    private String holderName;
    private double balance;
    private ArrayList<Transaction> transactionHistory = new ArrayList<>();

    public Account(String accountNumber, String holderName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = initialDeposit;
        transactionHistory.add(new Transaction("Initial Deposit", initialDeposit));
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }

    public String getTransactionHistory() {
        StringBuilder sb = new StringBuilder();
        for (Transaction t : transactionHistory) {
            sb.append(t.toString()).append("\n");
        }
        return sb.toString();
    }

    public void deposit(double amount) {
        balance += amount;
        // Add interest on deposit
        double interest = amount * INTEREST_RATE;
        balance += interest;
        transactionHistory.add(new Transaction("Deposit", amount));
    }

    public boolean withdraw(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        transactionHistory.add(new Transaction("Withdraw", amount));
        return true;
    }
}