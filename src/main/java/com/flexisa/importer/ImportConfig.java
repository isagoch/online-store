package com.flexisa.importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import com.flexisa.exception.ConfigurationException;

public record ImportConfig(Path inputFile, Path outputDir) {

    public static final String INPUT_ENV = "FLEXISA_INPUT_FILE";
    public static final String OUTPUT_ENV = "FLEXISA_OUTPUT_DIR";

    public static ImportConfig load(Map<String, String> env, Path configFile)
            throws ConfigurationException {
        String input = env.get(INPUT_ENV);
        String output = env.get(OUTPUT_ENV);

        if ((!hasValue(input) || !hasValue(output)) && Files.exists(configFile)) {
            Properties properties = new Properties();
            try (var reader = Files.newBufferedReader(configFile)) {
                properties.load(reader);
            } catch (IOException exception) {
                throw new ConfigurationException("Failed to read configuration file", exception);
            }

            if (!hasValue(input)) {
                input = properties.getProperty("input.file");
            }
            if (!hasValue(output)) {
                output = properties.getProperty("output.dir");
            }
        }

        if (!hasValue(input)) {
            throw new ConfigurationException("Missing configuration value: " + INPUT_ENV);
        }
        if (!hasValue(output)) {
            throw new ConfigurationException("Missing configuration value: " + OUTPUT_ENV);
        }

        return new ImportConfig(Path.of(input.trim()), Path.of(output.trim()));
    }

    private static boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }
}
