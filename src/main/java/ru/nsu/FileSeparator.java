package ru.nsu;

import ru.nsu.stats.FloatStats;
import ru.nsu.stats.IntegerStats;
import ru.nsu.stats.StringStats;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileSeparator {
//    private BufferedReader inFile;
    private final IntegerStats integerStats = new IntegerStats();
    public final FloatStats floatStats = new FloatStats();
    private final StringStats stringStats = new StringStats();

    public FileSeparator() {

    }

    public void processFile(Path inputFile, Path integerOutput, Path floatOutput, Path stringOutput) {
        try (LineWriter writer = new LineWriter(integerOutput, floatOutput, stringOutput);
             BufferedReader reader = Files.newBufferedReader(inputFile)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (readline.isNumber())
                    intgerStats.add(readline)
                else if (readline.isFloat())

                esle if (readline.isString())
                    stringStats.add(readline)

            }

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Processing failed", e);
        }
    }

    public void filterFile(String fileName) {
        Path fullPath = Path.of(fileName).toAbsolutePath();
        File file = fullPath.toFile();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file)))) {
            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) break; // EOF

//                valueHandler.addValue(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File " + fileName + " is not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d");
    }

    public static boolean isFloat(String str) {
        return str.matches("-?\\d+(\\.\\d+)?") || str.matches("-?\\d+(\\,\\d+)?");
    }

    public IntegerStats getIntegerStats() { return integerStats; }
    public FloatStats getFloatStats() { return floatStats; }
    public StringStats getStringStats() { return stringStats; }
}
