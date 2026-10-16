package com.flexisa.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class DataLoader {

    private DataLoader() {
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
