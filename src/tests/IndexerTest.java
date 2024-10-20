package tests;

import indexer.Indexer;
import indexer.SimpleTokenizer;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IndexerTest {

    public static void main(String[] args) throws IOException {
        System.out.println("Starting Indexer Tests...\n");

        List<Runnable> testCases = new ArrayList<>();
        testCases.add(IndexerTest::testIndexAndQuery);
        testCases.add(IndexerTest::testEmptyFile);
        testCases.add(IndexerTest::testMultipleOccurrences);
        testCases.add(IndexerTest::testCaseInsensitivity);
        testCases.add(IndexerTest::testFileWithoutMatchingWord);

        for (Runnable testCase : testCases) {
            testCase.run();
        }

        System.out.println("\nAll tests completed.");
    }

    public static void testIndexAndQuery() {
        System.out.println("Running test: testIndexAndQuery");
        Indexer indexer = new Indexer(new SimpleTokenizer());

        try {
            Path tempDir = Files.createTempDirectory("test_indexer");
            Path testFile1 = Files.createTempFile(tempDir, "sample1", ".txt");
            Path testFile2 = Files.createTempFile(tempDir, "sample2", ".txt");

            Files.write(testFile1, "Hello JetBrains\nWelcome to the Text File Indexer\n".getBytes());
            Files.write(testFile2, "Looking forward to the interview\nAnother hello in this file\n".getBytes());

            indexer.index(tempDir.toFile());

            var helloResult = indexer.query("hello");
            var interviewResult = indexer.query("interview");

            if (validateResults(helloResult, interviewResult, testFile1, testFile2)) {
                System.out.println(" ✅ Test passed: Query for 'hello' and 'interview' returned the expected result.");
            } else {
                System.out.println(" ❌ Test failed: Query results did not return the expected output.");
            }

            cleanUp(tempDir, testFile1, testFile2);

        } catch (IOException e) {
            System.out.println(" ❌ Test failed due to IOException: " + e.getMessage());
        }
    }

    public static void testEmptyFile() {
        System.out.println("Running test: testEmptyFile");
        Indexer indexer = new Indexer(new SimpleTokenizer());

        try {
            Path tempDir = Files.createTempDirectory("test_empty_file");
            Path testFile = Files.createTempFile(tempDir, "emptyFile", ".txt");

            Files.write(testFile, "".getBytes());

            indexer.index(tempDir.toFile());

            var result = indexer.query("anything");
            if (result.isEmpty()) {
                System.out.println(" ✅ Test passed: Empty file returned no results.");
            } else {
                System.out.println(" ❌ Test failed: Empty file should not return any results.");
            }

            cleanUp(tempDir, testFile);

        } catch (IOException e) {
            System.out.println(" ❌ Test failed due to IOException: " + e.getMessage());
        }
    }

    public static void testMultipleOccurrences() {
        System.out.println("Running test: testMultipleOccurrences");
        Indexer indexer = new Indexer(new SimpleTokenizer());

        try {
            Path tempDir = Files.createTempDirectory("test_multiple_occurrences");
            Path testFile1 = Files.createTempFile(tempDir, "sample1", ".txt");
            Path testFile2 = Files.createTempFile(tempDir, "sample2", ".txt");

            Files.write(testFile1, "hello world\nhello universe\n".getBytes());
            Files.write(testFile2, "hello everyone\nhello JetBrains\n".getBytes());

            indexer.index(tempDir.toFile());

            var helloResult = indexer.query("hello");

            if (helloResult.size() == 2 && helloResult.contains(testFile1.toString()) && helloResult.contains(testFile2.toString())) {
                System.out.println(" ✅ Test passed: 'hello' found in multiple files.");
            } else {
                System.out.println(" ❌ Test failed: Expected 'hello' to be found in multiple files.");
            }

            cleanUp(tempDir, testFile1, testFile2);

        } catch (IOException e) {
            System.out.println(" ❌ Test failed due to IOException: " + e.getMessage());
        }
    }

    public static void testCaseInsensitivity() {
        System.out.println("Running test: testCaseInsensitivity");
        Indexer indexer = new Indexer(new SimpleTokenizer());

        try {
            Path tempDir = Files.createTempDirectory("test_case_insensitivity");
            Path testFile = Files.createTempFile(tempDir, "sample", ".txt");

            Files.write(testFile, "Hello world\n".getBytes());

            indexer.index(tempDir.toFile());

            var lowerCaseResult = indexer.query("hello");
            var upperCaseResult = indexer.query("HELLO");

            if (lowerCaseResult.size() == 1 && upperCaseResult.size() == 1 && lowerCaseResult.equals(upperCaseResult)) {
                System.out.println(" ✅ Test passed: Case insensitivity works for queries.");
            } else {
                System.out.println(" ❌ Test failed: Query should be case insensitive.");
            }

            cleanUp(tempDir, testFile);

        } catch (IOException e) {
            System.out.println(" ❌ Test failed due to IOException: " + e.getMessage());
        }
    }

    public static void testFileWithoutMatchingWord() {
        System.out.println("Running test: testFileWithoutMatchingWord");
        Indexer indexer = new Indexer(new SimpleTokenizer());

        try {
            Path tempDir = Files.createTempDirectory("test_no_matching_word");
            Path testFile = Files.createTempFile(tempDir, "sample", ".txt");

            Files.write(testFile, "This file does not contain the query word.\n".getBytes());

            indexer.index(tempDir.toFile());

            var result = indexer.query("nonexistent");

            if (result.isEmpty()) {
                System.out.println(" ✅ Test passed: Query for a non-existing word returned no results.");
            } else {
                System.out.println(" ❌ Test failed: Non-existing word should return no results.");
            }

            cleanUp(tempDir, testFile);

        } catch (IOException e) {
            System.out.println(" ❌ Test failed due to IOException: " + e.getMessage());
        }
    }

    private static boolean validateResults(Set<String> helloResult, Set<String> interviewResult, Path testFile1, Path testFile2) {
        return helloResult.size() == 2 &&
                helloResult.contains(testFile1.toString()) &&
                helloResult.contains(testFile2.toString()) &&
                interviewResult.size() == 1 &&
                interviewResult.contains(testFile2.toString());
    }

    private static void cleanUp(Path tempDir, Path... files) throws IOException {
        for (Path file : files) {
            Files.deleteIfExists(file);
        }
        Files.deleteIfExists(tempDir);
    }
}
