import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Read {
    public static void main(String[] args) {
        String txtFilePath = "C:\\Users\\Asus\\Documents\\InvoiceFormat.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(txtFilePath))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    // Assuming the header (ABC Enterprises, 123, Anna Salai, Chennai 600001) contains only the invoice number and its details
                    String[] headerParts = line.split("\\s{2,}");
                    if (headerParts.length >= 2) {
                        String invoiceNumber = headerParts[0];
                        String invoiceDetails = headerParts[1];

                        System.out.println("Invoice Number: " + invoiceNumber);
                        System.out.println("Invoice Details: " + invoiceDetails);
                    } else {
                        System.out.println("Invalid header format.");
                    }
                    isHeader = false;
                } else {
                    System.out.println( line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
