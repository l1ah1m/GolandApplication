package tests;

import indexer.Indexer;
import indexer.SimpleTokenizer;

import java.io.*;
import java.nio.file.*;

public class IndexerTest {
    public static void main(String[] args) throws IOException {
        testIndexAndQuery();
    }

    public static void testIndexAndQuery() throws IOException {
        Indexer indexer = new Indexer(new SimpleTokenizer());
        Path tempDir = Files.createTempDirectory("test_indexer");
        Path testFile1 = Files.createTempFile(tempDir, "sample1", ".txt");
        Path testFile2 = Files.createTempFile(tempDir, "sample2", ".txt");

        Files.write(testFile1, "Hello JetBrains\nWelcome to the Text File Indexer\n".getBytes());
        Files.write(testFile2, "Looking forward for the interview\nOther test case stuff like another hello\n".getBytes());
        
        indexer.index(tempDir.toFile());
        
        var helloResult = indexer.query("hello");
        var interviewResult = indexer.query("interview");
        if (helloResult.size() == 2 && 
                helloResult.contains(testFile1.toString()) &&
                helloResult.contains(testFile2.toString()) &&
                interviewResult.size() == 1 &&
                interviewResult.contains(testFile2.toString())

        ) {
            System.out.println(" ✅ Test passed: Query for 'hello' and 'interview' returned the expected result. ✅ ");
        } else {
            System.out.println(" ❌ Test failed: Query for 'hello' did not return the expected result. ❌ ");
        }
        
        Files.delete(testFile1);
        Files.delete(testFile2);
        Files.delete(tempDir);
    }
}
