package ru.nsu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LineWriter implements AutoCloseable {
    private final Map<Class<?>, Path> pathsMap = new HashMap<>();
    private final Map<Class<?>, BufferedWriter> writerMap = new HashMap<>();
    private final StandardOpenOption[] openOptions;

    public LineWriter(Path integerFilePath, Path floatFilePath, Path stringFilePath, StandardOpenOption... openOptions) {
        pathsMap.put(Integer.class, Objects.requireNonNull(integerFilePath));
        pathsMap.put(Float.class, Objects.requireNonNull(floatFilePath));
        pathsMap.put(String.class, Objects.requireNonNull(stringFilePath));
        this.openOptions = openOptions;
    }

    public void write(Class<?> type, String value) throws IOException {
        BufferedWriter writer = writerMap.get(type);
        if (writer == null) {
            Path path = pathsMap.get(type);
            Files.createDirectories(path.getParent());
            writerMap.put(type, Files.newBufferedWriter(pathsMap.get(type), openOptions));
            writer = writerMap.get(type);
        }
        writer.write(value);
        writer.newLine(); // System specific line separator
    }

    @Override
    public void close() {
        for (BufferedWriter writer : writerMap.values()) {
            try {
                writer.close();
            } catch (IOException _) {} // Ignore
        }
    }
}
