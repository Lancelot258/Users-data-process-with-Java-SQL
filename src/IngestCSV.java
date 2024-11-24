import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

public class IngestCSV {

    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:/Users/path/yourdatabase.db";
        String csvFilePath = "/Users/path/customers-100.csv";

        // SQL statement to create the table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS testdb ("
                + "customer_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "customer_unique_id TEXT, "
                + "first_name TEXT, "
                + "last_name TEXT NOT NULL, "
                + "company TEXT, "
                + "city TEXT, "
                + "country TEXT, "
                + "phone1 TEXT, "
                + "phone2 TEXT, "
                + "email TEXT UNIQUE, "
                + "subscription_date TEXT, "
                + "website TEXT"
                + ");";

        // SQL statement to insert data with IGNORE to skip duplicate emails
        String insertSQL = "INSERT OR IGNORE INTO testdb (customer_unique_id, first_name, last_name, company, city, country, phone1, phone2, email, subscription_date, website) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            // Create the table if it does not exist
            stmt.execute(createTableSQL);
            System.out.println("Table testdb created successfully.");

            // Prepare to insert data
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                // Read the CSV file
                String line;
                boolean isHeader = true;
                String[] headers = null;
                while ((line = br.readLine()) != null) {
                    // Skip header and verify header information
                    if (isHeader) {
                        headers = line.split(",");
                        if (headers.length < 12) {
                            System.err.println("Invalid header information: " + line);
                            return;
                        }
                        isHeader = false;
                        continue;
                    }

                    // Split CSV data using a custom method to correctly handle commas within quotes
                    String[] data = splitCSV(line);
                    if (data.length < 12) { // Ensure at least 12 columns
                        System.err.println("Invalid data: " + line);
                        continue;
                    }

                    // Extract relevant columns
                    String customerUniqueId = data[1].trim();
                    String firstName = data[2].trim();
                    String lastName = data[3].trim();
                    String company = data[4].trim();
                    String city = data[5].trim();
                    String country = data[6].trim();
                    String phone1 = data[7].trim();
                    String phone2 = data[8].trim();
                    String email = data[9].trim();
                    String subscriptionDate = data[10].trim();
                    String website = data[11].trim();

                    // Print debug info to check if the comma is retained
                    System.out.println("Company: " + company);

                    // Set parameters and add to batch
                    pstmt.setString(1, customerUniqueId);
                    pstmt.setString(2, firstName);
                    pstmt.setString(3, lastName);
                    pstmt.setString(4, company);
                    pstmt.setString(5, city);
                    pstmt.setString(6, country);
                    pstmt.setString(7, phone1);
                    pstmt.setString(8, phone2);
                    pstmt.setString(9, email);
                    pstmt.setString(10, subscriptionDate);
                    pstmt.setString(11, website);
                    pstmt.addBatch();
                }

                // Execute batch insert
                pstmt.executeBatch();
                System.out.println("Data imported successfully!");
            }

        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String[] splitCSV(String line) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\"[^\"]*\"|[^,]+)(,|$)");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String value = matcher.group(1).trim();
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            result.add(value);
        }
        return result.toArray(new String[0]);
    }
}
