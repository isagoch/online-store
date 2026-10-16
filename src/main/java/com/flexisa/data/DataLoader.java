package com.flexisa.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
            return reader.lines()
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .toList();
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
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
