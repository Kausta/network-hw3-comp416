package group1.hw3.routing;

import group1.hw3.util.Pair;
import group1.hw3.util.io.CsvReader;
import group1.hw3.util.io.ParenthesizedStringParser;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputParser {
    private final CsvReader reader;
    private final ParenthesizedStringParser parser;

    public InputParser() {
        this.reader = new CsvReader();
        this.parser = new ParenthesizedStringParser();
    }

    public Map<Integer, InputNode> parseInputFile(Path csvFilePath) {
        return reader.readAll(csvFilePath)
                .stream()
                .filter(line -> !line.isEmpty())
                .map(line -> new InputNode(toInt(line.get(0)), parseEdges(line)))
                .collect(Collectors.toMap(InputNode::getNodeId, node -> node));
    }

    private List<Pair<Integer, Integer>> parseEdges(List<String> edgeList) {
        return edgeList.stream()
                .skip(1)
                .map(parser::parseTwoTuple)
                .map(pair -> pair.map((key, value) -> new Pair<Integer, Integer>(toInt(key), toInt(value))))
                .collect(Collectors.toList());
    }

    private static int toInt(String str) {
        return Integer.parseInt(str);
    }
}
