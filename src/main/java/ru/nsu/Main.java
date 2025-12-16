package ru.nsu;

public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome! " + args.length);

        if (args.length > 0) {
            parseArgs(args);
        }

//        FileSeparator fs = new FileSeparator();
//        fs.processFile();

//        fs.getIntegerStats().toString();
    }

    private static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.println(i + ". = " + args[i]);

            switch (args[i]) {
                case "-p" -> {
                    if (i + 1 >= args.length) {
                        System.err.println("Error: '-p' requires a value.");
                        System.exit(1);
                    }
                    handlePrefixOption(args[++i]);
                }
                case "-o" -> {
                    if (i + 1 >= args.length) {
                        System.err.println("Error: '-o' requires a value.");
                        System.exit(1);
                    }
                    handleOutputPathOption(args[++i]);
                }
                case "-a" -> {
                    handleAppendModeOption();
                }
                case "-s" -> {
                    handleShortModeOption();
                }
                case "-f" -> {
                    handleFullModeOption();
                }
                case "-v", "--verbose" -> {
                    System.out.println("Verbose mode enabled.");
                }
                default -> {
                    System.out.println("Unknown argument: " + args[i]);
                }
            }
        }
    }

    private static void handlePrefixOption(String value) {

    }

    private static void handleOutputPathOption(String value) {

    }

    private static void handleAppendModeOption() {

    }

    private static void handleShortModeOption() {

    }

    private static void handleFullModeOption() {

    }
}