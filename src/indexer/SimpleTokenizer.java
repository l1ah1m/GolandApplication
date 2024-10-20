package indexer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleTokenizer implements Tokenizer {
    @Override
    public List<String> tokenize(String text) {
        return Arrays.stream(text.split("\\W+"))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
