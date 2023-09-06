import java.util.*;

class Account {
    private int accountNumber;
    private double balance;

    public Account(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void transfer(Account receiver, double amount) {
        if (amount <= balance) {
            balance -= amount;
            receiver.deposit(amount);
            System.out.println("Transfer successful.");
        } else {
            System.out.println("Insufficient balance for transfer.");
        }
    }
}

class BankTransaction {
    private static int transactionCount = 0;
    private int transactionId;
    private Account sender;
    private Account receiver;
    private String transactionType;
    private double amount;
    private Date timestamp;

    public BankTransaction(Account sender, Account receiver, String transactionType, double amount) {
        this.transactionId = ++transactionCount;
        this.sender = sender;
        this.receiver = receiver;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = new Date();
    }

    public String toString() {
        return "Transaction ID: " + transactionId + "\n"
                + "Sender: " + sender.getAccountNumber() + "\n"
                + "Receiver: " + receiver.getAccountNumber() + "\n"
                + "Transaction Type: " + transactionType + "\n"
                + "Amount: " + amount + "\n"
                + "Timestamp: " + timestamp + "\n";
    }
}

class AccountHolder {
    private int userId;
    private int userPin;
    private List<Account> accounts;

    public AccountHolder(int userId, int userPin) {
        this.userId = userId;
        this.userPin = userPin;
        this.accounts = new ArrayList<>();
    }

    public boolean authenticate(int userId, int userPin) {
        return this.userId == userId && this.userPin == userPin;
    }

    public int getUserId() {
        return userId;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
}

class Bank {
    private Map<Integer, AccountHolder> accountHolders;
    private List<BankTransaction> transactions;

    public Bank() {
        accountHolders = new HashMap<>();
        transactions = new ArrayList<>();
    }

    public void addAccountHolder(AccountHolder accountHolder) {
        accountHolders.put(accountHolder.getUserId(), accountHolder);
    }

    public boolean authenticateAccountHolder(int userId, int userPin) {
        AccountHolder accountHolder = accountHolders.get(userId);
        if (accountHolder != null) {
            return accountHolder.authenticate(userId, userPin);
        }
        return false;
    }

    public void createAccount(AccountHolder accountHolder, int accountNumber, double initialBalance) {
        Account account = new Account(accountNumber, initialBalance);
        accountHolder.addAccount(account);
    }

    public void performTransaction(Account sender, Account receiver, String transactionType, double amount) {
        if (sender != null && receiver != null) {
            BankTransaction transaction = new BankTransaction(sender, receiver, transactionType, amount);
            transactions.add(transaction);

            if ("transfer".equalsIgnoreCase(transactionType)) {
                sender.transfer(receiver, amount);
            } else if ("withdraw".equalsIgnoreCase(transactionType)) {
                sender.withdraw(amount);
            } else if ("deposit".equalsIgnoreCase(transactionType)) {
                receiver.deposit(amount);
            }
        }
    }

    public List<BankTransaction> getTransactionHistory() {
        return transactions;
    }
}



public class ATM{
    public static void main(String[] args) {
        Bank bank = new Bank();
        AccountHolder accountHolder = new AccountHolder(7894, 56123);
        bank.addAccountHolder(accountHolder);
        bank.createAccount(accountHolder, 1001, 1000.0);
        bank.createAccount(accountHolder, 1002, 500.0);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter user id: ");
        int userId = scanner.nextInt();
        System.out.print("Enter user pin: ");
        int userPin = scanner.nextInt();

        if (bank.authenticateAccountHolder(userId, userPin)) {
            System.out.println("Authentication successful. Welcome!");

            while (true) {
                System.out.println("\nSelect an option:");
                System.out.println("1. Show balance");
                System.out.println("2. Show transactions history");
                System.out.println("3. Withdraw");
                System.out.println("4. Deposit");
                System.out.println("5. Transfer");
                System.out.println("6. Quit");
                int option = scanner.nextInt();

                switch (option) {
                    case 1:
                        List<Account> accounts = accountHolder.getAccounts();
                        System.out.println("Account balances:");
                        for (Account account : accounts) {
                            System.out.println("Account " + account.getAccountNumber() + ": " + account.getBalance());
                        }
                        break;

                    case 2:
                        List<BankTransaction> transactions = bank.getTransactionHistory();
                        for (BankTransaction transaction : transactions) {
                            System.out.println(transaction);
                        }
                        break;

                    case 3:
                        System.out.print("Enter account number: ");
                        int withdrawAccountNumber = scanner.nextInt();
                        System.out.print("Enter amount to withdraw: ");
                        double withdrawAmount = scanner.nextDouble();
                        Account withdrawAccount = null;
                        for (Account account : accountHolder.getAccounts()) {
                            if (account.getAccountNumber() == withdrawAccountNumber) {
                                withdrawAccount = account;
                                break;
                            }
                        }
                        if (withdrawAccount != null) {
                            bank.performTransaction(withdrawAccount, null, "withdraw", withdrawAmount);
                        } else {
                            System.out.println("Account not found.");
                        }
                        break;

                    case 4:
                        System.out.print("Enter account number: ");
                        int depositAccountNumber = scanner.nextInt();
                        System.out.print("Enter amount to deposit: ");
                        double depositAmount = scanner.nextDouble();
                        Account depositAccount = null;
                        for (Account account : accountHolder.getAccounts()) {
                            if (account.getAccountNumber() == depositAccountNumber) {
                                depositAccount = account;
                                break;
                            }
                        }
                        if (depositAccount != null) {
                            bank.performTransaction(null, depositAccount, "deposit", depositAmount);
                        } else {
                            System.out.println("Account not found.");
                        }
                        break;

                    case 5:
                        System.out.print("Enter sender account number: ");
                        int senderAccountNumber = scanner.nextInt();
                        System.out.print("Enter receiver account number: ");
                        int receiverAccountNumber = scanner.nextInt();
                        System.out.print("Enter amount to transfer: ");
                        double transferAmount = scanner.nextDouble();
                        Account senderAccount = null;
                        Account receiverAccount = null;
                        for (Account account : accountHolder.getAccounts()) {
                            if (account.getAccountNumber() == senderAccountNumber) {
                                senderAccount = account;
                            }
                            if (account.getAccountNumber() == receiverAccountNumber) {
                                receiverAccount = account;
                            }
                        }
                        if (senderAccount != null && receiverAccount != null) {
                            bank.performTransaction(senderAccount, receiverAccount, "transfer", transferAmount);
                        } else {
                            System.out.println("Sender or receiver account not found.");
                        }
                        break;

                    case 6:
                        System.out.println("Thank you for using the ATM.");
                        scanner.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid option.");
                }
            }
        } else {
            System.out.println("Authentication failed. Exiting...");
        }
    }
}
