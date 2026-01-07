import java.util.HashMap;

public class Bank {
    private HashMap<String, String> users = new HashMap<>();
    private HashMap<String, Account> accounts = new HashMap<>();
    private int accountIdCounter = 1000; // Counter for auto-generating unique account IDs

    public Bank() {
        users.put("admin", "admin");
    }

    public boolean login(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public boolean createUserAccount(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty() || users.containsKey(username)) {
            return false;
        }
        users.put(username, password);
        return true;
    }

    // Modified: Now auto-generates account number and returns it. No longer takes accountNumber as input.
    public String addAccount(String holderName, double initialDeposit) {
        String accountNumber = String.valueOf(accountIdCounter++);
        if (holderName == null || holderName.trim().isEmpty() || initialDeposit < 0) {
            return null; // Error case (though unlikely with auto-gen)
        }
        accounts.put(accountNumber, new Account(accountNumber, holderName, initialDeposit));
        return accountNumber;
    }

    public double depositToAccount(String accountNumber, double amount) {
        if (amount <= 0) return -1;
        Account account = accounts.get(accountNumber);
        if (account == null) return -1;
        account.deposit(amount);
        return account.getBalance();
    }

    public double withdrawFromAccount(String accountNumber, double amount) {
        if (amount <= 0) return -1;
        Account account = accounts.get(accountNumber);
        if (account == null) return -2;
        return account.withdraw(amount) ? account.getBalance() : -1;
    }

    public String getTransactionHistory(String accountNumber) {
        Account account = accounts.get(accountNumber);
        return account != null ? account.getTransactionHistory() : null;
    }
}