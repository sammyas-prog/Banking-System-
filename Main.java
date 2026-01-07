import java.awt.*;
import javax.swing.*;

public class Main extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private Bank bank = new Bank();

    private JTextField loginUserField;
    private JPasswordField loginPassField;

    public Main() {
        setTitle("SAR BANK");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createMainMenuPanel(), "MainMenu");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        JLabel titleLabel = new JLabel("S.A.R BANK");
        titleLabel.setFont(new Font("Bodoni MT", Font.BOLD, 20));

        loginUserField = new JTextField(15);
        loginPassField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(loginUserField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(loginPassField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(loginButton, gbc);
        gbc.gridx = 1;
        panel.add(createAccountButton, gbc);

        loginButton.addActionListener(e -> {
            if (bank.login(loginUserField.getText().trim(),
                    new String(loginPassField.getPassword()))) {
                loginUserField.setText("");
                loginPassField.setText("");
                cardLayout.show(mainPanel, "MainMenu");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        createAccountButton.addActionListener(e -> createUserAccount());

        return panel;
    }

    private void createUserAccount() {
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JPasswordField passConfirmField = new JPasswordField(15);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("New Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(passConfirmField);

        if (JOptionPane.showConfirmDialog(this, panel,
                "Create New User Account",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            String passwordConfirm = new String(passConfirmField.getPassword());

            if (username.isEmpty() || password.isEmpty()
                    || !password.equals(passwordConfirm)) {
                JOptionPane.showMessageDialog(this,
                        "Invalid input or passwords do not match!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (bank.createUserAccount(username, password)) {
                // Automatically create a bank account with auto-generated ID
                String accountId = bank.addAccount(username, 0.0); // Holder name = username, initial deposit = 0
                JOptionPane.showMessageDialog(this,
                        "User account created successfully!\nYour bank account ID is: " + accountId);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username already exists!",
                        "Error",
                        
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createMainMenuPanel() { 
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("S.A.R BANK - MAIN MENU",
                SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel);

        JButton[] buttons = {
                new JButton("Deposit To Account"),
                new JButton("Withdraw From Account"),
                new JButton("Show Account Transactions"),
                new JButton("Logout"),
                new JButton("Exit")
        };

        for (JButton btn : buttons) {
            btn.setPreferredSize(new Dimension(200, 30));
            panel.add(btn);
        }

        buttons[0].addActionListener(e -> depositToAccount());
        buttons[1].addActionListener(e -> withdrawFromAccount());
        buttons[2].addActionListener(e -> showAccountTransactions());
        buttons[3].addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                cardLayout.show(mainPanel, "Login");
            }
        });
        buttons[4].addActionListener(e -> System.exit(0));

        return panel;
    }

    private void depositToAccount() {
        String accNum = JOptionPane.showInputDialog(this,
                "Enter Account Number:");
        if (accNum == null || (accNum = accNum.trim()).isEmpty())
            return;

        String amountStr = JOptionPane.showInputDialog(this,
                "Enter amount to deposit:");
        if (amountStr == null || (amountStr = amountStr.trim()).isEmpty())
            return;

        try {
            double amount = Double.parseDouble(amountStr);
            double newBalance = bank.depositToAccount(accNum, amount);
            if (newBalance == -1) {
                JOptionPane.showMessageDialog(this,
                        "Account not found or invalid amount!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Deposit successful! New Balance: $" + newBalance);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void withdrawFromAccount() {
        String accNum = JOptionPane.showInputDialog(this,
                "Enter Account Number:");
        if (accNum == null || (accNum = accNum.trim()).isEmpty())
            return;

        String amountStr = JOptionPane.showInputDialog(this,
                "Enter amount to withdraw:");
        if (amountStr == null || (amountStr = amountStr.trim()).isEmpty())
            return;

        try {
            double amount = Double.parseDouble(amountStr);
            double newBalance = bank.withdrawFromAccount(accNum, amount);
            if (newBalance == -2) {
                JOptionPane.showMessageDialog(this,
                        "Account not found!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (newBalance == -1) {
                JOptionPane.showMessageDialog(this,
                        "Insufficient balance!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Withdrawal successful! New Balance: $" + newBalance);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAccountTransactions() {
        String accNum = JOptionPane.showInputDialog(this,
                "Enter Account Number:");
        if (accNum == null || (accNum = accNum.trim()).isEmpty())
            return;

        String history = bank.getTransactionHistory(accNum);
        if (history == null) {
            JOptionPane.showMessageDialog(this,
                    "Account not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JTextArea textArea = new JTextArea(history);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "Transaction History for Account " + accNum,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new Main().setVisible(true));
    }
}