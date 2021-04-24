package Application.Data;

import Application.Main;
import Application.Utility.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Database {

    // static String jdbcURL = "jdbc:mariadb://83.77.103.210:3306/bancomax";
    static String jdbcURL = "jdbc:mysql://127.0.0.1:3306/bancomax";
    // 127.0.0.1:3306
    // http://83.77.103.210:3306/bancomax
    // http://83.77.103.210/phpmyadmin:3306/bancomax
    static String username = "BMadmin";
    static String password = "When83+wet++";

    static Connection conn = null;
    static Statement stmt = null;

    public static void connectToDatabase() throws Exception {
        try {
            // Register JDBC Driver
            Main.class.forName("com.mysql.cj.jdbc.Driver");
            // Main.class.forName("org.mariadb.jdbc.Driver");
            // Open a Connection
            System.out.println("Connecting to  database ...");
            conn  = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println(ConsoleColors.GREEN + "Connection successful!" + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Connection failed" + ConsoleColors.RESET);
            System.out.println(e);
        }
    }

    // INSERTS

    public static void insertCard(String cardNr, String cardtype, byte[] PINhash, byte[] PINsalt, int accountID) {
        try {
            String query = "INSERT INTO `bancomax`.`card` (`cardNr`, `cardtype`, `PINhash`, `PINsalt`, `FK_accountID`) VALUES ('"+cardNr+"', '"+cardtype+"', ?, ?, '"+accountID+"')";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setBytes(1, PINhash);
            pstmt.setBytes(2, PINsalt);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertAdmin(byte[] passwordHash, byte[] passwordSalt) {
        try {
            String query = "INSERT INTO `bancomax`.`admin`(`passwordHash`, `passwordSalt`) VALUES (?,?);";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setBytes(1, passwordHash);
            pstmt.setBytes(2, passwordSalt);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertUser(String firstName, String lastName, Salutation salutation) {
        try {
            String query = "INSERT INTO `bancomax`.`user` (`firstName`, `lastName`, `salutation`) VALUES ('"+firstName+"', '"+lastName+"', '"+salutation+"');";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertAccount(String IBAN, double balance, String bank, int userID) {
        try {
            String query = "INSERT INTO `bancomax`.`account`(`IBAN`,`balanceInCHF`,`bank`,`FK_userID`) VALUES ('"+IBAN+"', '"+balance+"', '"+bank+"', '"+userID+"');";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertTransaction(Operation operation, Currency currency, double amount, int cardID) {
        try {
            String action = "";
            if (operation == Operation.withdraw) {
                action = "Withdrawal";
            } else {
                action = "Deposit";
            }
            Date currentDate = new Date();
            Timestamp tmstmp = new Timestamp(currentDate.getTime());
            String query = "INSERT INTO bancomax.transaction (timestamp, action, currency, amount, FK_cardID) VALUES ('"+tmstmp+"', '"+action+"', '"+currency+"', '"+amount+"', '"+cardID+"');";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // UPDATES

    public static void changeAdminPW(byte[] hash, byte[] salt) {
        try {
            String query = "UPDATE bancomax.admin SET passwordHash = ?, passwordSalt = ? WHERE adminID = 1;";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setBytes(1, hash);
            pstmt.setBytes(2, salt);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changePIN(int cardID, byte[] PINhash, byte[] PINsalt) {
        try {
            String query = "UPDATE `bancomax`.`card` SET `PINhash` = ?, `PINsalt` = ? WHERE `cardID` = '"+cardID+"';";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setBytes(1, PINhash);
            pstmt.setBytes(2, PINsalt);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateBalance(Operation operation, double amountInCHF, int accountID) {
        try {
            double newBalance;
            if (operation == Operation.deposit) { // deposit
                newBalance = getBalance(accountID) + amountInCHF;
            } else { // withdraw
                newBalance = getBalance(accountID) - amountInCHF;
            }
            String query = "UPDATE bancomax.account SET balanceInCHF = '"+newBalance+"' WHERE `accountID` = '"+accountID+"';";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateMoneyStock(Operation operation, int[] banknotes, Currency currency) {
        try {
            int[] stock = getMoneystock(currency);
            for (int i = 0; i < banknotes.length; i++) {
                if (operation == Operation.withdraw) {
                    stock[i] -= banknotes[i];
                } else {
                    stock[i] += banknotes[i];
                }
            }
            String query = "UPDATE bancomax.moneystock SET thousand = ?, twoHundred = ?, hundred = ?, fifty = ?, twenty = ?, ten = ? WHERE currency = '"+currency+"';";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, stock[0]);
            pstmt.setInt(2, stock[1]);
            pstmt.setInt(3, stock[2]);
            pstmt.setInt(4, stock[3]);
            pstmt.setInt(5, stock[4]);
            pstmt.setInt(6, stock[5]);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SELECTS

    public static byte[] getAdminSalt() {
        String query = "SELECT passwordSalt FROM bancomax.admin WHERE adminID = 1;";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getBytes(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getAdminHash() {
        String query = "SELECT passwordHash FROM bancomax.admin WHERE adminID = 1;";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getBytes(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getSalt(String cardNr) {
        String query = "SELECT PINsalt FROM bancomax.card WHERE cardNr = '"+cardNr+"';";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getBytes(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getHash(String cardNr) {
        String query = "SELECT PINhash FROM bancomax.card WHERE cardNr = '"+cardNr+"';";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getBytes(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getUserInfo(String cardNr) {
        String query = "SELECT u.userID, u.firstName, u.lastName, u.salutation FROM bancomax.card as c JOIN account as a ON c.FK_accountID = a.accountID JOIN user as u ON a.FK_userID = u.userID WHERE c.cardNr = '"+cardNr+"';";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) { // Iterates through the whole ResultSet line by line
                int userID = rs.getInt("userID");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String salutation = rs.getString("salutation");
                return new String[] { String.valueOf(userID), firstName, lastName, salutation };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getCardInfo(String cardNr) {
        String query = "SELECT c.cardID, c.cardNr, c.cardType FROM bancomax.card as c WHERE c.cardNr = '"+cardNr+"';";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) { // Iterates through the whole ResultSet line by line
                int cardID = rs.getInt("cardID");
                String cardNumber = rs.getString("cardNr");
                String cardtype = rs.getString("cardtype");
                return new String[] { String.valueOf(cardID), cardNumber, cardtype };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getAccountInfo(String cardNr) {
        String query = "SELECT a.accountID, a.IBAN, a.bank FROM bancomax.card as c JOIN account as a ON c.FK_accountID = a.accountID WHERE c.cardNr = '"+cardNr+"';";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) { // Iterates through the whole ResultSet line by line
                int accountID = rs.getInt("accountID");
                String IBAN = rs.getString("IBAN");
                String bank = rs.getString("bank");
                return new String[] { String.valueOf(accountID), IBAN, bank };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Double getBalance(int accountID) {
        String query = "SELECT a.balanceInCHF FROM bancomax.card as c JOIN account as a ON c.FK_accountID = a.accountID WHERE a.accountID = '"+accountID+"';";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getTransactionCount(int day, int month) {
        String query = "SELECT COUNT(*) FROM transaction WHERE (SELECT EXTRACT(MONTH FROM transaction.timestamp)) = '"+month+"' AND (SELECT EXTRACT(DAY FROM transaction.timestamp)) = '"+day+"';";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<String> getAllCardNrs() {
        String query = "SELECT cardNr FROM bancomax.card;";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<String> cardNrs = new ArrayList<>();
            while (rs.next()) {
                cardNrs.add(rs.getString(1));
            }
            return cardNrs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Integer> getAllAccountIDs() {
        String query = "SELECT accountID FROM bancomax.account;";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<Integer> accountIDs = new ArrayList<>();
            while (rs.next()) {
                accountIDs.add(rs.getInt(1));
            }
            return accountIDs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Integer> getAllUserIDs() {
        String query = "SELECT userID FROM bancomax.user;";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<Integer> userIDs = new ArrayList<>();
            while (rs.next()) {
                userIDs.add(rs.getInt(1));
            }
            return userIDs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int[] getMoneystock(Currency currency) {
        String query = "SELECT thousand, twoHundred, hundred, fifty, twenty, ten FROM bancomax.moneystock WHERE currency = '"+currency+"';";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) { // Iterates through the whole ResultSet line by line
                int thousand = rs.getInt("thousand");
                int twoHundred = rs.getInt("twoHundred");
                int hundred = rs.getInt("hundred");
                int fifty = rs.getInt("fifty");
                int twenty = rs.getInt("twenty");
                int ten = rs.getInt("ten");
                return new int[] { thousand, twoHundred, hundred, fifty, twenty, ten };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkMoneystock(int[] banknotes, String currency) {
        String query = "SELECT thousand, twoHundred, hundred, fifty, twenty, ten FROM bancomax.moneystock WHERE currency = '"+currency+"';";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) { // Iterates through the whole ResultSet line by line
                int thousand = rs.getInt("thousand");
                int twoHundred = rs.getInt("twoHundred");
                int hundred = rs.getInt("hundred");
                int fifty = rs.getInt("fifty");
                int twenty = rs.getInt("twenty");
                int ten = rs.getInt("ten");
                int[] stock = { thousand, twoHundred, hundred, fifty, twenty, ten };
                for (int i = 0; i < banknotes.length; i++) {
                    if (banknotes[i] > stock[i]) {
                        return false;
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getTransactionCurrencyCount(Currency currency) {
        String query = "SELECT COUNT(*) FROM transaction WHERE currency = '"+currency+"';";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTransactionOperationsCount(Currency currency, Operation operation) {
        String op = "";
        if (operation.equals(Operation.withdraw)) {
            op = "Withdrawal";
        } else {
            op = "Deposit";
        }
        String query = "SELECT COUNT(*) FROM transaction WHERE currency = '"+currency+"' AND action = '"+op+"';";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<Transaction> getTransactions(int cardID) {
        String query = "SELECT timestamp, action, currency, amount FROM bancomax.transaction WHERE FK_cardID = '"+cardID+"';";
        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) { // Iterates through the whole ResultSet line by line
                Timestamp timestamp = rs.getTimestamp("timestamp");
                String action = rs.getString("action");
                String currency = rs.getString("currency");
                double amount = rs.getDouble("amount");
                String nAction;
                if (action.equals("Withdrawal")) {
                    nAction = "Bezug";
                } else {
                    nAction = "Einzahlung";
                }
                String tmstmp = Utils.formatTimestamp(timestamp);
                String nAmount = Utils.formatMoney(amount);
                transactions.add(new Transaction(tmstmp, nAction, currency, nAmount));
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
