package ru.nsu;

import ru.nsu.stats.FloatStats;
import ru.nsu.stats.IntegerStats;
import ru.nsu.stats.StringStats;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final List<Path> inputPaths = new ArrayList<>();
    private static Path outputPath = Path.of("./");
    private static boolean fullMode = false;
    private static String prefix = "";

    static StandardOpenOption[] openOptions = {
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE
    };

    public static void main(String[] args) {
        if (args.length > 0) {
            parseArgs(args);
            if (inputPaths.isEmpty()) {
                System.out.println("No correct input files provided.");
                System.exit(0);
            }
        } else {
            System.out.println("No arguments provided.");
            System.out.println(
                    "Usage: java -jar TextFilter.jar " +
                    "[-p prefix] [-o output_path] [-a] [-s] [-f] input_file1 [input_file2 ...]");
            System.exit(0);
        }
        startTextFiltering(); // Main program logic
    }

    private static void startTextFiltering() {
        try (FileSeparator fileSeparator = createFileSeparator();) {
            for (Path inputPath : inputPaths) {
                fileSeparator.processFile(inputPath);
            }
            printStats(
                    fileSeparator.getIntegerStats(),
                    fileSeparator.getFloatStats(),
                    fileSeparator.getStringStats());
        } catch (InvalidPathException e) {
            System.err.println("Error: Invalid path format: " + e.getMessage() + ".");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + ".");
        }
    }

    private static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (Options.isOption(args[i])) {
                Options option = Options.getOption(args[i]);
                switch (option) {
                    case Options.PREFIX -> {
                        if (i + 1 >= args.length || Options.isOption(args[i + 1])) {
                            System.err.println("Error: '-p' requires a value. Using no prefix (by default).");
                            continue;
                        }
                        prefix = args[++i];
                    }
                    case Options.OUTPUT_PATH -> {
                        if (i + 1 >= args.length || Options.isOption(args[i + 1])) {
                            System.err.println(
                                    "Error: '-o' requires a value. " +
                                    "Using current directory as output path (by default).");
                            continue;
                        }
                        outputPath = Path.of(args[++i]);
                    }
                    case Options.APPEND -> {
                        // Keep [] array to pass as StandardOpenOption... parameter to writer
                        StandardOpenOption[] tmpOpenOptions = Arrays.copyOf(openOptions, openOptions.length + 1);
                        tmpOpenOptions[tmpOpenOptions.length - 1] = StandardOpenOption.APPEND;
                        openOptions = tmpOpenOptions;
                    }
                    case Options.SHORT -> fullMode = false;
                    case Options.FULL -> fullMode = true;
                    default -> System.err.println("Unknown argument: " + args[i]);
                }
            } else { // Arg is an input file
                Path path = Path.of(args[i]);
                if (!path.toFile().exists()) {
                    System.err.println("Error: file " + path + " does not exist. Skipping.");
                    continue;
                }
                inputPaths.add(Path.of(args[i]));
            }
        }
    }

    private static FileSeparator createFileSeparator() throws InvalidPathException{
        return new FileSeparator(
            getOutputFilePath("integers.txt"),
            getOutputFilePath("floats.txt"),
            getOutputFilePath("strings.txt"),
            openOptions
        );
    }

    private static void printStats(IntegerStats integerStats, FloatStats floatStats, StringStats stringStats) {
        System.out.println(buildIntegerStatsString(integerStats));
        System.out.println(buildFloatStatsString(floatStats));
        System.out.println(buildStringStatsString(stringStats));
    }

    private static String buildIntegerStatsString(IntegerStats stats) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("Integer stats:\n")
                .append("\t- Count: " + stats.getCount() + "\n");
        if (fullMode) {
            stringBuilder
                    .append("\t- Sum: " + stats.getSum() + "\n")
                    .append("\t- Max value: " + stats.getMaxValue() + "\n")
                    .append("\t- Min value: " + stats.getMinValue() + "\n");
        }
        return stringBuilder.toString();
    }

    private static String buildFloatStatsString(FloatStats stats) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("Float stats:\n")
                .append("\t- Count: " + stats.getCount() + "\n");
        if (fullMode) {
            stringBuilder
                    .append("\t- Sum: " + stats.getSum() + "\n")
                    .append("\t- Max value: " + stats.getMaxValue() + "\n")
                    .append("\t- Min value: " + stats.getMinValue() + "\n");
        }
        return stringBuilder.toString();
    }

    private static String buildStringStatsString(StringStats stats) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("String stats:\n")
                .append("\t- Count: " + stats.getCount() + "\n");
        if (fullMode) {
            stringBuilder
                    .append("\t- Max length: " + stats.getMaxLength() + "\n");
        }
        return stringBuilder.toString();
    }

    private static Path getOutputFilePath(String fileName) throws InvalidPathException {
        return Path.of(outputPath.toAbsolutePath().toString(), prefix + fileName);
    }
}
