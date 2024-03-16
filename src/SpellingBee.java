// Anya Kothari
// Code that creates a game similar to the game Spelling Bee, where the user
// puts in a given set of letters, and all the possible words are outputted.
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Anya Kothari
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // Call the recursive method
        makeWords("", letters);
    }
    public void makeWords(String word, String letters) {
        String newWord;
        // If letters is empty, then no additional words can be generated and return nothing
        if (letters.isEmpty()) {
            return;
        }
        // For each letter, add it to the existing word to create a new word
        for (int i = 0; i < letters.length(); i++) {
            newWord = word + letters.charAt(i);
            words.add(newWord);
            // Make a recursive call with the new word and the remaining letters
            makeWords(newWord,letters.substring(0 , i)+letters.substring(i + 1, letters.length()));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Call recursive merge sort on words
        words = mergeSort(words, 0, words.size() - 1);
    }
    private ArrayList<String> mergeSort(ArrayList<String> words, int low, int high) {
        // If the ArrayList of words has only one element, return the list
        if (high == low) {
            ArrayList<String> sortedWords = new ArrayList<String>();
            sortedWords.add(0, words.get(low));
            return sortedWords;
        }
        // Continuously splitting the list in half
        int med = (high +low) / 2;
        ArrayList<String> list1 = new ArrayList<>();
        // Call mergeSort on the first half of the list
        list1 = mergeSort(words, low, med);
        ArrayList<String> list2 = new ArrayList<>();
        // Call mergeSort on the second half of the list
        list2 = mergeSort(words, med + 1, high);
        // Merge the two lists together
        return merge(list1, list2);
    }

    public ArrayList<String> merge(ArrayList<String> list1, ArrayList<String> list2) {
        ArrayList<String> newList = new ArrayList<>();
        int index1 = 0;
        int index2 = 0;
        int count = 0;

        while (index1 < list1.size() && index2 < list2.size()) {
            // Compare the words in each list to each other
            if ((list1.get(index1).compareTo(list2.get(index2))) < 0) {
                newList.add(count, list1.get(index1++));
            }
            else {
                newList.add(count, list2.get(index2++));
            }
            count++;
        }
        // Copy over any remaining elements from the first list
        while (index1 < list1.size()) {
            newList.add(count++, list1.get(index1++));
        }
        // Copy over any remaining elements from the second list
        while (index2 < list2.size()) {
            newList.add(count++, list2.get(index2++));
        }
        return newList;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        for (int i = 0; i < words.size(); i++)
        {
            if (!(found(words.get(i), DICTIONARY, 0, DICTIONARY.length - 1))) {
                words.remove(i);
                i--;
            }
        }
    }

    // Returns true if s in the dictionary and false otherwise
    public boolean found(String s, String[] dictionary, int start, int end) {
        int mid = (start + end) / 2;
        if (dictionary[mid].equals(s)) {
            return true;
        }
        if (start >= end) {
            return false;
        }
        if ((s.compareTo(dictionary[mid]) < 0)) {
            return found(s, dictionary, start, mid - 1) ;
        }
        else {
            return found(s, dictionary, mid + 1, end);
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
