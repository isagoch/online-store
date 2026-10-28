package com.flexisa.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.flexisa.exception.DataFileException;

public final class DataLoader {

    private DataLoader() {
    }

    public static List<String> readResourceLines(String resourceName) {
        InputStream inputStream = DataLoader.class.getResourceAsStream("/" + resourceName);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return readDataLines(reader);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static List<String> readFileLines(Path path) throws DataFileException {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null");
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return readDataLines(reader);
        } catch (IOException exception) {
            throw new DataFileException(
                    "Failed to read file: " + path.getFileName(),
                    exception);
        }
    }

    private static List<String> readDataLines(BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        boolean header = true;
        while ((line = reader.readLine()) != null) {
            if (header) {
                header = false;
            } else if (!line.isBlank()) {
                lines.add(line);
            }
        }
        return List.copyOf(lines);
    }

    public static <T> LoadResult<T> parseAll(List<String> lines, Function<String, T> parser) {
        List<T> valid = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            try {
                valid.add(parser.apply(lines.get(i)));
            } catch (IllegalArgumentException exception) {
                errors.add("Line " + (i + 1) + ": " + exception.getMessage());
            }
        }

        return new LoadResult<>(valid, errors);
    }
}
