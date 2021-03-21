package Application.Data;

import Application.Main;
import Application.Utility.ConsoleColors;
import Application.Utility.Salutation;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    // static String jdbcURL = "jdbc:mariadb://83.77.103.210:3306/bancomax";
    static String jdbcURL = "jdbc:mysql://127.0.0.1:3306/bancomax";
    // 127.0.0.1:3306
    // http://83.77.103.210:3306/bancomax
    // http://83.77.103.210/phpmyadmin:3306/bancomax
    // db_ip:3306/dbName
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

    private void example() {
        try {
            stmt = conn.createStatement();

            String query = "";

            stmt.executeUpdate(query);
            System.out.println("XXX successful");
        } catch (Exception e) {
            e.printStackTrace();
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
            System.out.println("INSERT INTO 'card' successful");
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
            System.out.println("INSERT INTO 'admin' successful");
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

    public static void viewTableUser() {
        String query = "SELECT * FROM bancomax.user;";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) { // Iterates through the whole ResultSet line by line
                int userID = rs.getInt("userID");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                Salutation salutation = Salutation.valueOf(rs.getString("salutation"));
                System.out.println(userID + ", " + firstName + ", " + lastName + ", " + salutation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /*
    // ---
    public static ArrayList<String> getExample() throws Exception {
        try {
            Connection con = getConnection();
            PreparedStatement selected = con.prepareStatement("SELECT firstName, PIN, lastName FROM user WHERE PIN = '523452129'");

            ResultSet selectedData = selected.executeQuery();

            ArrayList<String> list = new ArrayList<String>();
            while(selectedData.next()) { // Runs as long as there is another result on the next line
                System.out.print(selectedData.getString("PIN")+ " ");
                System.out.print(selectedData.getString("firstName")+ " ");
                System.out.println(selectedData.getString("lastName"));

                list.add(selectedData.getString("lastName"));
            }
            System.out.println("All records selected");
            return list;

        } catch (Exception e) {
            System.out.println(e);
            return null; // in case an error happens
        }
        finally {
            System.out.println("Select complete");
        }
    }
    */

}
