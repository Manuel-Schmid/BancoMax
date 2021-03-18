package Application.Data;

import Application.Utility.ConsoleColors;
import Application.Utility.Salutation;

import java.sql.*;

public class Database {

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
            Class.forName("com.mysql.cj.jdbc.Driver");
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
        } finally {
            try {
                if (stmt != null || conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // INSERTS

    // SELECTS

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
