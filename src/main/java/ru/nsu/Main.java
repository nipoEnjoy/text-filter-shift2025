package ru.nsu;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.nsu.Options;

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
        } else {
            System.out.println("No arguments provided.");
            System.exit(0);
        }

        try (FileSeparator fileSeparator = createFileSeparator();) {
            if (fileSeparator == null) {
                System.exit(1); // Can't do anything without fileSeparator
            }

            for (Path inputPath : inputPaths) {
                try {
                    fileSeparator.processFile(inputPath);
                } catch (Exception e) {
                    if (fullMode) {
                        System.err.println("Error processing file: " + e.getMessage());
                    }
                }
            }

            if (fullMode) {
                System.out.printf(fileSeparator.getIntegerStats().toString() + "\n" +
                        fileSeparator.getFloatStats().toString() + "\n" +
                        fileSeparator.getStringStats().toString() + "\n");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Calls System.exit(1) on invalid arguments
    private static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (!Options.isOption(args[i])) { // Arg is an input file
                Path path = Path.of(args[i]);

                if (!path.toFile().exists()) {
                    System.err.println("Error: file " + path + " does not exist.");
                    System.exit(1); // Can't do anything without input file
                }
                inputPaths.add(Path.of(args[i]));
                continue;
            }

            Options option = Options.getOption(args[i]);
            switch (option) {
                case Options.PREFIX -> {
                    if (i + 1 >= args.length || Options.isOption(args[i + 1])) {
                        System.err.println("Error: '-p' requires a value.");
                        System.exit(1);
                    }
                    prefix = args[++i];
                }
                case Options.OUTPUT_PATH -> {
                    if (i + 1 >= args.length || Options.isOption(args[i + 1])) {
                        System.err.println("Error: '-o' requires a value.");
                        System.exit(1);
                    }
                    outputPath = Path.of(args[++i]);
                }
                case Options.APPEND -> {
                    // Keep [] array to pass as StandardOpenOption... parameter for writer
                    StandardOpenOption[] tmpOpenOptions = Arrays.copyOf(openOptions, openOptions.length + 1);
                    tmpOpenOptions[tmpOpenOptions.length - 1] = StandardOpenOption.APPEND;
                    openOptions = tmpOpenOptions;
                }
                case Options.SHORT -> fullMode = false;
                case Options.FULL -> fullMode = true;
                default -> System.err.println("Unknown argument: " + args[i]);
            }
        }
    }

    private static FileSeparator createFileSeparator() {
        try {
            return new FileSeparator(
                getOutputFilePath("integers.txt"),
                getOutputFilePath("floats.txt"),
                getOutputFilePath("strings.txt"),
                openOptions
            );
        } catch (InvalidPathException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    private static Path getOutputFilePath(String fileName) throws InvalidPathException {
        return Path.of(outputPath.toAbsolutePath().toString(), prefix + fileName);
    }
}
