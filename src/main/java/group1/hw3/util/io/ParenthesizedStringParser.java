package group1.hw3.util.io;

import group1.hw3.util.Pair;

public class ParenthesizedStringParser {
    public ParenthesizedStringParser() {

    }

    public Pair<String, String> parseTwoTuple(final String input) {
        if (!input.startsWith("(") || !input.endsWith(")")) {
            throw new RuntimeException("Cannot parse input " + input);
        }
        final String inputContent = input.substring(1, input.length() - 1);
        final String[] components = inputContent.split(",");
        if (components.length != 2) {
            throw new RuntimeException("Cannot parse input " + input);
        }
        return new Pair<>(components[0].trim(), components[1].trim());
    }
}
