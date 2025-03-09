import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringListWriter {

  // define a method that takes a list of strings and a file name as input, and saves the list of strings to the file
  public static void writeStringListToFile(List<String> strings, String fileName) {
    // try-with-resources block to automatically close the writer when done
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
      for (String string : strings) {
        // write each string to the file, followed by a newline character
        writer.write(string);
        writer.newLine();
      }
    }
    // handle any exceptions that may occur
    catch (IOException e) {
      System.err.println("An error occurred while writing to the file: " + e.getMessage());
    }
  }
  
  public static void main(String[] args) {
    String[] NAMES = {"Alice", "Bob", "Carol", "David", "Emily", "Frank", "Grace", "Henry", "Isabella", "John"};
    List<String> namesList = new ArrayList<>();
    for (String name : NAMES) {
        namesList.add(name);
      }
      writeStringListToFile(namesList, "Name-List.txt");
      System.out.println("DONE");
  }
}
