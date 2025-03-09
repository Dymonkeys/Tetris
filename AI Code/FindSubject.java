/*
 * File: FindSubject.java
 * Author: David Neufeld
 * Created Date: Thu Dec 08 2022 at 1:32:38 AM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * 
 * Collaboration: 
 * 
 */
public class FindSubject {
    public static void main(String[] args) {
        System.out.println(findSubject("all cats are red"));
        
    }
    // define a method that takes a sentence as input and returns the subject of the sentence
public static String findSubject(String sentence) {
    // split the sentence into words using the space character as a delimiter
    String[] words = sentence.split(" ");
    // loop through the words in the sentence, starting from the end
    for (int i = words.length-1; i >= 0; i--) {
      String word = words[i];
      // check if the word is a common noun (i.e. not a proper noun or a pronoun)
      if (!Character.isUpperCase(word.charAt(0)) && !isPronoun(word)) {
        // if the word is a common noun, return it as the subject of the sentence
        return word;
      }
    }
    // if no subject was found, return null
    return null;
  }
  
  // define a helper method that takes a word as input and returns true if the word is a pronoun, and false otherwise
  private static boolean isPronoun(String word) {
    // define a list of pronouns
    String[] pronouns = {"I", "you", "he", "she", "it", "we", "they"};
    // check if the word is in the list of pronouns
    for (String pronoun : pronouns) {
      if (pronoun.equalsIgnoreCase(word)) {
        return true;
      }
    }
    // if the word is not in the list of pronouns, return false
    return false;
  }
  
}
