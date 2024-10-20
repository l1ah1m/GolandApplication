# Text File Indexer

A simple Java console application that provides a service for indexing text files. The application can recursively index files in directories and allows users to search for files containing specific words.

## Features
- Indexes text files and directories (recursively).
- Supports querying files that contain a specific word.
- Uses a customizable tokenization algorithm for splitting text into tokens (words).
- Efficient handling of text files via buffered input.

## How It Works

The program reads files (or directories) specified by the user and processes the text in those files using a tokenizer, which breaks down the text into individual tokens (words). It then builds an index that maps each word (token) to the files that contain it.

When the user queries a specific word, the program returns the list of files that contain that word, ignoring case.

---

## Getting Started

## Running the Application

1. **Clone the repository** (or download the files):

   ```bash
   git clone https://github.com/your-repo/text-file-indexer.git
   cd GolandApplication/src
   ```
2. **Compile & start the app** (or download the files):

   ```bash
   javac -d ../bin indexer/*.java && java -cp ../bin indexer.Main
   ```


## How to Test

The application includes a simple test in `tests/IndexerTest.java` to verify the indexing functionality.

### Running Tests

1. **Compile the test files**:

   Navigate to the `src` directory (if you're not already there):

   ```bash
   cd src
   javac -d ../bin indexer/*.java tests/*.java && java -cp ../bin tests.IndexerTest
    ```
