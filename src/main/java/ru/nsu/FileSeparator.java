package ru.nsu;

import ru.nsu.stats.FloatStats;
import ru.nsu.stats.IntegerStats;
import ru.nsu.stats.StringStats;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileSeparator implements AutoCloseable {
    private final IntegerStats integerStats = new IntegerStats();
    public final FloatStats floatStats = new FloatStats();
    private final StringStats stringStats = new StringStats();

    private final LineWriter writer;

    public FileSeparator(Path integerOutput, Path floatOutput, Path stringOutput, StandardOpenOption... options) {
        this.writer = new LineWriter(integerOutput, floatOutput, stringOutput, options);
    }

    public void processFile(Path inputFile) {
        String line = null;
        int lineNumber = 0;
        try (BufferedReader reader = Files.newBufferedReader(inputFile)) {
            while ((line = reader.readLine()) != null) { // EOF check
                if (line.isEmpty()) continue;

                if (isFloat(line)) {
                    writeLine(Float.class, line, lineNumber);
                    addToStats(Float.class, line);
                }
                else if (isNumeric(line)) {
                    writeLine(Integer.class, line, lineNumber);
                    addToStats(Float.class, line);
                } else { // String
                    writeLine(String.class, line, lineNumber);
                    addToStats(String.class, line);
                }
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + inputFile.toString() + ". Skipping.");
        } catch (IOException e) {
            System.err.println(
                    "Error reading file " + inputFile.toString() +
                    " at line " + lineNumber + ": " + line);
        }
    }

    private void writeLine(Class<?> type, String value, int lineNumber) {
        try {
            writer.write(Integer.class, value);
        } catch (IOException e) {
            System.err.println(
                    "IO error while writing " + type.getSimpleName() +
                    " at line " + lineNumber + ": " + value +
                    " (" + e.getMessage() + ")");
        } catch (Exception e) {
            System.err.println(
                    "Error while writing " + type.getSimpleName() +
                    " at line " + lineNumber + ": " + value +
                    " (" + e.getMessage() + ")");
        }
    }

    private void addToStats(Class<?> type, String value) {
        try {
            // Can't switch-case on Class<?> here
            if (type == Integer.class) {
                integerStats.add(Integer.valueOf(value));
            } else if (type == Float.class) {
                floatStats.add(Float.valueOf(value));
            } else if (type == String.class) {
                stringStats.add(value);
            } else {
                System.err.println(
                        "Trying to add unknown type to stats: " + type +
                        "Dropped from stats: " + value);
            }
        } catch (NumberFormatException e) {
            if (type == Float.class) {
                System.err.println(
                        "Invalid number format for float (',' float separator is not supported). " +
                        "Dropped from stats: " + value);
            } else if (type == Integer.class) {
                System.err.println(
                        "Invalid number format or number is too big for int. " +
                        "Dropped from stats: " + value);
            } else { // String
                System.err.println(
                        "Invalid format. Dropped from stats: " + value +
                        " (" + e.getCause() + ")");
            }
        }
    }

    @Override
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+");
    }

    private boolean isFloat(String str) {
        return str.matches("-?\\d+[.,]\\d+([Ee][-+]\\d+)?");
    }

    public IntegerStats getIntegerStats() { return integerStats; }
    public FloatStats getFloatStats() { return floatStats; }
    public StringStats getStringStats() { return stringStats; }
}
