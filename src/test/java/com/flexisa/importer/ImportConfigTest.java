package com.flexisa.importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.flexisa.exception.ConfigurationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImportConfigTest {

    @TempDir
    private Path tempDir;

    @Test
    void load_readsValuesFromPropertiesFile() throws IOException, ConfigurationException {
        Path configFile = tempDir.resolve("importer.properties");
        Files.writeString(configFile, "input.file=orders.csv\noutput.dir=results\n");

        ImportConfig config = ImportConfig.load(Map.of(), configFile);

        assertEquals(Path.of("orders.csv"), config.inputFile());
        assertEquals(Path.of("results"), config.outputDir());
    }

    @Test
    void load_environmentValuesOverridePropertiesAndAreTrimmed()
            throws IOException, ConfigurationException {
        Path configFile = tempDir.resolve("importer.properties");
        Files.writeString(configFile, "input.file=file-orders.csv\noutput.dir=file-output\n");

        ImportConfig config = ImportConfig.load(
                Map.of(ImportConfig.INPUT_ENV, " env-orders.csv ",
                        ImportConfig.OUTPUT_ENV, " env-output "),
                configFile);

        assertEquals(Path.of("env-orders.csv"), config.inputFile());
        assertEquals(Path.of("env-output"), config.outputDir());
    }

    @Test
    void load_fillsBlankEnvironmentValuesFromProperties()
            throws IOException, ConfigurationException {
        Path configFile = tempDir.resolve("importer.properties");
        Files.writeString(configFile, "input.file=orders.csv\noutput.dir=results\n");

        ImportConfig config = ImportConfig.load(
                Map.of(ImportConfig.INPUT_ENV, " ", ImportConfig.OUTPUT_ENV, ""),
                configFile);

        assertEquals(Path.of("orders.csv"), config.inputFile());
        assertEquals(Path.of("results"), config.outputDir());
    }

    @Test
    void load_usesPropertiesOnlyForMissingEnvironmentValues()
            throws IOException, ConfigurationException {
        Path configFile = tempDir.resolve("importer.properties");
        Files.writeString(configFile, "input.file=file-orders.csv\noutput.dir=file-output\n");

        ImportConfig config = ImportConfig.load(
                Map.of(ImportConfig.INPUT_ENV, "env-orders.csv"),
                configFile);

        assertEquals(Path.of("env-orders.csv"), config.inputFile());
        assertEquals(Path.of("file-output"), config.outputDir());
    }

    @Test
    void load_throwsForMissingInputSetting() {
        ConfigurationException exception = assertThrows(
                ConfigurationException.class,
                () -> ImportConfig.load(
                        Map.of(ImportConfig.OUTPUT_ENV, "results"),
                        tempDir.resolve("missing.properties")));

        assertEquals("Missing configuration value: " + ImportConfig.INPUT_ENV, exception.getMessage());
    }

    @Test
    void load_throwsForMissingOutputSetting() {
        ConfigurationException exception = assertThrows(
                ConfigurationException.class,
                () -> ImportConfig.load(
                        Map.of(ImportConfig.INPUT_ENV, "orders.csv"),
                        tempDir.resolve("missing.properties")));

        assertEquals("Missing configuration value: " + ImportConfig.OUTPUT_ENV, exception.getMessage());
    }

    @Test
    void load_wrapsConfigurationFileReadErrorsWithoutExposingItsPath() throws IOException {
        Path configDirectory = Files.createDirectory(tempDir.resolve("not-a-file"));

        ConfigurationException exception = assertThrows(
                ConfigurationException.class,
                () -> ImportConfig.load(Map.of(), configDirectory));

        assertEquals("Failed to read configuration file", exception.getMessage());
        assertTrue(exception.getCause() instanceof IOException);
        assertFalse(exception.getMessage().contains(configDirectory.toString()));
    }
}
