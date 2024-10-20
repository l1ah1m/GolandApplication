package indexer;

import java.io.File;
import java.util.Scanner;

public class Main {
    private static final Indexer indexer = new Indexer(new SimpleTokenizer());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        System.out.println("Welcome to the Text File Indexer!");
        System.out.println("Available commands: 'index' to index files, 'query' to search for a word, 'help' for instructions, or 'exit' to quit.");

        while (true) {
            System.out.print("\nEnter a command (index/query/help/exit): ");
            userInput = scanner.nextLine().trim().toLowerCase();

            switch (userInput) {
                case "index":
                    processIndexing(scanner);
                    break;
                case "query":
                    processQuery(scanner);
                    break;
                case "help":
                    printHelp();
                    break;
                case "exit":
                    System.out.println("Thank you for using Text File Indexer. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid command. Please enter 'index', 'query', 'help', or 'exit'.");
            }
        }
    }

    private static void processIndexing(Scanner scanner) {
        System.out.print("Please provide the full path of the file or directory to index: ");
        String path = scanner.nextLine().trim();
        File file = new File(path);

        if (file.exists()) {
            long startTime = System.currentTimeMillis();
            System.out.println(" 🕕 Indexing in progress... 🕣 \n");
            
            indexer.index(file);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.printf(" ✅ Indexing completed successfully in %d ms ✅ \n ", duration);
        } else {
            System.out.println(" ❌ Error: The specified path does not exist. Please check and try again. ❌ ");
        }
    }

    private static void processQuery(Scanner scanner) {
        System.out.print("Enter the word to search for: ");
        String word = scanner.nextLine().trim();
        var results = indexer.query(word);

        if (results.isEmpty()) {
            System.out.println("No files found containing the word: '" + word + "'.");
        } else {
            System.out.println("\nFiles containing the word '" + word + "':");
            results.forEach(System.out::println);
        }
    }

    private static void printHelp() {
        System.out.println("\nHelp - Available Commands:");
        System.out.println("'index' - Index a file or directory.");
        System.out.println("'query' - Search for files containing a specific word.");
        System.out.println("'help'  - Display this help message.");
        System.out.println("'exit'  - Quit the application.\n");
    }
}
