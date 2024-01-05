import java.io.*;
import java.util.zip.*;

public class ReadDocFile {
    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\Asus\\Documents\\InvoiceFormat.docx";
        String outputFilePath = "C:\\Users\\Asus\\Documents\\InvoiceFormat.txt";

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             ZipInputStream zis = new ZipInputStream(fis);
             FileWriter writer = new FileWriter(outputFilePath);
             BufferedWriter bw = new BufferedWriter(writer)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("word/document.xml")) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }
                    String content = new String(baos.toByteArray(), "UTF-8");
                    content = content.replaceAll("<[^>]+>", ""); // Remove XML tags
                    bw.write(content);
                    break;
                }
            }

            System.out.println("Content from " + inputFilePath + " written to " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
