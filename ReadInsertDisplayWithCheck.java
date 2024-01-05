import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class ReadInsertDisplayWithCheck {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\Asus\\Documents\\InvoiceFormat.txt";
        String jdbcURL = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            insertDataFromFile(connection, filePath);
            displayTableContents(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertDataFromFile(Connection connection, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isInvoiceSection = false;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Invoice_id")) {
                    isInvoiceSection = true;
                    continue;
                }
                if (isInvoiceSection) {
                    String[] data = line.split(" – "); // Replace with proper delimiter
                    if (data.length == 17) {
                        int invoiceId = Integer.parseInt(data[0].trim());
                        if (!isInvoiceIdExists(connection, invoiceId)) {
                            insertIntoDatabase(connection, data);
                        } else {
                            System.out.println("Skipping insertion. Invoice_id already exists: " + invoiceId);
                        }
                    } else {
                        System.out.println("Data format mismatch in line: " + line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayTableContents(Connection connection) {
        try {
            String query = "SELECT * FROM file_content";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("file_content Table Data:");
            System.out.println("+------------+--------------+-----------------+-----------------+-----------------+--------+--------------+---------------+------------------+-----------------+---------+------------+------------+--------+------------+-------------+--------+");

            while (resultSet.next()) {
                System.out.printf("| %-11s | %-12s | %-15s | %-15s | %-15s | %-6s | %-12s | %-13s | %-16s | %-15s | %-7s | %-10s | %-10s | %-8s | %-11s | %-12s | %-6s |\n",
                        resultSet.getString("Invoice_id"), resultSet.getString("Invoice_number"),
                        resultSet.getString("Invoice_Date"), resultSet.getString("Shipment_Date"),
                        resultSet.getString("Order_Date"), resultSet.getString("Order_No"),
                        resultSet.getString("Delivery_No"), resultSet.getString("ClientName"),
                        resultSet.getString("ClientAddress"), resultSet.getString("DeliveryAddress"),
                        resultSet.getString("RemitTo"), resultSet.getString("ItemNumber"),
                        resultSet.getString("Quantity"), resultSet.getString("UnitPrice"),
                        resultSet.getString("Price"), resultSet.getString("Totalinvoice"),
                        resultSet.getString("Status"));
            }

            System.out.println("+------------+--------------+-----------------+-----------------+-----------------+--------+--------------+---------------+------------------+-----------------+---------+------------+------------+--------+------------+-------------+--------+");

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean isInvoiceIdExists(Connection connection, int invoiceId) {
        String query = "SELECT Invoice_id FROM file_content WHERE Invoice_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, invoiceId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void insertIntoDatabase(Connection connection, String[] data) {
        try {
            String sql = "INSERT INTO file_content (Invoice_id, Invoice_number, Invoice_Date, Shipment_Date, " +
                    "Order_Date, Order_No, Delivery_No, ClientName, ClientAddress, DeliveryAddress, RemitTo, " +
                    "ItemNumber, Quantity, UnitPrice, Price, Totalinvoice, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(data[0].trim()));
            statement.setInt(2, Integer.parseInt(data[1].trim()));
            statement.setString(3, data[2].trim());
            statement.setString(4, data[3].trim());
            statement.setString(5, data[4].trim());
            statement.setInt(6, Integer.parseInt(data[5].trim()));
            statement.setInt(7, Integer.parseInt(data[6].trim()));
            statement.setString(8, data[7].trim());
            statement.setString(9, data[8].trim());
            statement.setString(10, data[9].trim());
            statement.setString(11, data[10].trim());
            statement.setInt(12, Integer.parseInt(data[11].trim()));
            statement.setInt(13, Integer.parseInt(data[12].trim()));
            statement.setDouble(14, Double.parseDouble(data[13].trim()));
            statement.setDouble(15, Double.parseDouble(data[14].trim()));
            statement.setDouble(16, Double.parseDouble(data[15].trim()));
            statement.setString(17, data[16].trim());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new row has been inserted!");
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
