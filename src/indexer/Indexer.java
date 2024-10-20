package indexer;

import java.io.*;
import java.util.*;

public class Indexer {
    private final Map<String, Set<String>> index = new HashMap<>();
    private final Tokenizer tokenizer;

    public Indexer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public void index(File file) {
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                index(f);
            }
        } else if (file.isFile()) {
            indexFile(file);
        }
    }

    private void indexFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String token : tokenizer.tokenize(line)) {
                    index.computeIfAbsent(token, k -> new HashSet<>()).add(file.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + file.getAbsolutePath());
        }
    }

    public Set<String> query(String word) {
        return index.getOrDefault(word.toLowerCase(), Collections.emptySet());
    }
}
