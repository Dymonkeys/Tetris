import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyCounter {

  // define a method that takes a file name as input and returns a map of words and their frequencies
  public static Map<String, Integer> countWordFrequencies(String fileName) {
    // create a new map to store the words and their frequencies
    Map<String, Integer> wordFrequencies = new HashMap<>();
    // try-with-resources block to automatically close the reader when done
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      // read the file line by line
      String line;
      while ((line = reader.readLine()) != null) {
        // split the line into a list of words
        List<String> words = splitLineIntoWords(line);
        // add each word to the map, incrementing its frequency by 1
        for (String word : words) {
          wordFrequencies.merge(word, 1, Integer::sum);
        }
      }
    }
    // handle any exceptions that may occur
    catch (IOException e) {
      System.err.println("An error occurred while reading the file: " + e.getMessage());
    }
    // return the map of words and their frequencies
    return wordFrequencies;
  }

  // define a helper method that takes a line of text as input and returns a list of words
  private static List<String> splitLineIntoWords(String line) {
    // create a new list of words
    List<String> words = new ArrayList<>();
    // split the line into words using the space character as a delimiter
    String[] tokens = line.split(" ");
    // add each word to the list
    for (String token : tokens) {
      words.add(token);
    }
    // return the list of words
    return words;
  }
  public static void main(String[] args) {
    //Map<String, Integer> wordFrequencies = countWordFrequencies("formated.txt");
    Map<String, List<String>> wordContexts = countWordContexts("formated.txt");
    System.out.println(wordContexts);
  }
  
  // define a method that takes a file name as input and returns a map of words and their contexts
  public static Map<String, List<String>> countWordContexts(String fileName) {
    // create a new map to store the words and their contexts
    Map<String, List<String>> wordContexts = new HashMap<>();
    // try-with-resources block to automatically close the reader when done
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      // read the file line by line
      String line;
      while ((line = reader.readLine()) != null) {
        // split the line into a list of words
        List<String> words = splitLineIntoWords(line);
        // add each word and its context to the map
        for (int i = 0; i < words.size(); i++) {
          String word = words.get(i);
          // get the previous and next words
          String previousWord = (i > 0) ? words.get(i-1) : null;
          String nextWord = (i < words.size()-1) ? words.get(i+1) : null;
          // add the previous and next words to the list of contexts for the current word
          List<String> contexts = wordContexts.getOrDefault(word, new ArrayList<>());
          if (previousWord != null) {
            contexts.add(previousWord);
          }
          if (nextWord != null) {
            contexts.add(nextWord);
          }
          wordContexts.put(word, contexts);
        }
      }
    }
    // handle any exceptions that may occur
    catch (IOException e) {
      System.err.println("An error occurred while reading the file: " + e.getMessage());
    }
    // return the map
    return wordContexts;
  }
}
