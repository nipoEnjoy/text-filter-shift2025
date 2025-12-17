package ru.nsu;

public enum Options {
    APPEND("-a"),
    SHORT("-s"),
    FULL("-f"),
    PREFIX("-p"),
    OUTPUT_PATH("-o");

    private final String flag;

    Options(String flag) {
        this.flag = flag;
    }

    public static Options getOption(String flag) throws RuntimeException {
        for (Options opt : values()) {
            if (opt.flag.equals(flag)) {
                return opt;
            }
        }
        throw new RuntimeException("Unknown option: " + flag);
    }

    public String getFlag() {
        return flag;
    }

    public static boolean isOption(String option) {
        for (Options opt : values()) {
            if (opt.flag.equals(option)) {
                return true;
            }
        }
        return false;
    }
}
