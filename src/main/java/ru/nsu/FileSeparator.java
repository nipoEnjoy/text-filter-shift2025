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
        try (BufferedReader reader = Files.newBufferedReader(inputFile)) {
            String line;
            while ((line = reader.readLine()) != null) { // EOF check
                if (line.isEmpty()) continue;

                if (isFloat(line)) {
                    writer.write(Float.class, line);
                    floatStats.add(Float.valueOf(line));
                }
                else if (isNumeric(line)) {
                    writer.write(Integer.class, line);
                    integerStats.add(Integer.valueOf(line));
                } else { // String
                    writer.write(String.class, line);
                    stringStats.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Processing failed for file " + inputFile.toString(), e);
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
