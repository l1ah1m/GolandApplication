package indexer;

import java.util.List;

public interface Tokenizer {
    List<String> tokenize(String text);
}
