package ru.nsu;

import ru.nsu.stats.FloatStats;
import ru.nsu.stats.IntegerStats;
import ru.nsu.stats.StringStats;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileSeparator {
    private final IntegerStats integerStats = new IntegerStats();
    public final FloatStats floatStats = new FloatStats();
    private final StringStats stringStats = new StringStats();

    private final Path integerOutput;
    private final Path floatOutput;
    private final Path stringOutput;

    public FileSeparator(Path integerOutput, Path floatOutput, Path stringOutput) {
        this.integerOutput = integerOutput;
        this.floatOutput = floatOutput;
        this.stringOutput = stringOutput;
    }

    public void processFile(Path inputFile, StandardOpenOption... options) {
        try (LineWriter writer = new LineWriter(integerOutput, floatOutput, stringOutput);
             BufferedReader reader = Files.newBufferedReader(inputFile)
        ) {
            String line;
            while ((line = reader.readLine()) != null) { // EOF check
                System.out.println("\nRead line: " + line);
                if (line.isEmpty()) continue;
                System.out.println("Type check: Float-" + isFloat(line) + ", Integer-" + isNumeric(line) + ", String-" + (!isNumeric(line) && !isFloat(line)));

                if (isFloat(line)) {
                    System.out.println("Float determined: " + line);
                    writer.write(Float.class, line);
                    floatStats.add(Float.valueOf(line));
                }
                else if (isNumeric(line)) {
                    System.out.println("Integer determined: " + line);
                    writer.write(Integer.class, line);
                    integerStats.add(Integer.valueOf(line));
                } else { // String
                    System.out.println("String determined: " + line);
                    writer.write(String.class, line);
                    stringStats.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format");
        } catch (IOException e) {
            throw new RuntimeException("Processing failed", e);
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+");
    }

    private boolean isFloat(String str) {
        return str.matches("-?\\d+\\.\\d+(E-\\d+)?"); //  || str.matches("-?\\d+(\\,\\d+)")
    }

    public IntegerStats getIntegerStats() { return integerStats; }
    public FloatStats getFloatStats() { return floatStats; }
    public StringStats getStringStats() { return stringStats; }
}
