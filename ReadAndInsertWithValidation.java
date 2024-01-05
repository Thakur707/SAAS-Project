import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadAndInsertWithValidation {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\Asus\\Documents\\InvoiceFormat.txt";

        String jdbcURL = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
             BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            boolean isInvoiceSection = false;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Invoice_id")) {
                    isInvoiceSection = true;
                    continue;
                }
                if (isInvoiceSection) {
                    String[] data = line.split(" - ");
                    if (data.length == 17) {
                        if (!isInvoiceIdExists(connection, Integer.parseInt(data[0]))) {
                            insertIntoDatabase(connection, data);
                        } else {
                            System.out.println("Skipping insertion. Invoice_id already exists: " + data[0]);
                        }
                    } else {
                        System.out.println("Data format mismatch in line: " + line);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean isInvoiceIdExists(Connection connection, int invoiceId) throws SQLException {
        String query = "SELECT Invoice_id FROM file_content WHERE Invoice_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, invoiceId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Returns true if Invoice_id exists, false otherwise
            }
        }
    }

    private static void insertIntoDatabase(Connection connection, String[] data) {
        try {
            String sql = "INSERT INTO file_content (Invoice_id, Invoice_number, Invoice_Date, Shipment_Date, " +
                    "Order_Date, Order_No, Delivery_No, ClientName, ClientAddress, DeliveryAddress, RemitTo, " +
                    "ItemNumber, Quantity, UnitPrice, Price, Totalinvoice, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(data[0]));
            statement.setInt(2, Integer.parseInt(data[1]));
            statement.setString(3, data[2]);
            statement.setString(4, data[3]);
            statement.setString(5, data[4]);
            statement.setInt(6, Integer.parseInt(data[5]));
            statement.setInt(7, Integer.parseInt(data[6]));
            statement.setString(8, data[7]);
            statement.setString(9, data[8]);
            statement.setString(10, data[9]);
            statement.setString(11, data[10]);
            statement.setInt(12, Integer.parseInt(data[11]));
            statement.setInt(13, Integer.parseInt(data[12]));
            statement.setDouble(14, Double.parseDouble(data[13]));
            statement.setDouble(15, Double.parseDouble(data[14]));
            statement.setDouble(16, Double.parseDouble(data[15]));
            statement.setString(17, data[16]);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new row has been inserted!");
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
