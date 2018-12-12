package group1.hw3.util.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvReader {
    public CsvReader() {

    }

    public List<List<String>> readAll(Path csvFilePath) {
        try {
            List<String> lines = Files.readAllLines(csvFilePath);
            return getTrimmedLines(lines);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private List<List<String>> getTrimmedLines(List<String> lines) {
        return lines.stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                // Use a nice regex to get commas not in parenthesised lists
                .map(line -> line.split("(?!\\B\\([^)]*),(?![^(]*\\)\\B)"))
                .map(arr -> (List<String>) new ArrayList(Arrays.asList(arr)))
                .collect(Collectors.toList());
    }
}
