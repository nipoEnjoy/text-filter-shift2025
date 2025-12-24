package ru.nsu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WriteController implements AutoCloseable {
    private final Map<Class<?>, Path> pathsMap = new HashMap<>();
    private final Map<Class<?>, BufferedWriter> writerMap = new HashMap<>();
    private final StandardOpenOption[] openOptions;

    public WriteController(Path integerFilePath, Path floatFilePath, Path stringFilePath, StandardOpenOption... openOptions) {
        pathsMap.put(Integer.class, Objects.requireNonNull(integerFilePath));
        pathsMap.put(Float.class, Objects.requireNonNull(floatFilePath));
        pathsMap.put(String.class, Objects.requireNonNull(stringFilePath));
        this.openOptions = openOptions;
    }

    public void write(Class<?> type, String value) throws IOException {
        BufferedWriter writer = writerMap.get(type);
        if (writer == null) {
            writer = addWriter(type);
        }
        writer.write(value);
        writer.newLine(); // System specific line separator
    }

    private BufferedWriter addWriter(Class<?> type) throws IOException {
        Path path = pathsMap.get(type);
        File file = path.toFile();
        Files.createDirectories(path.getParent());

        if (file.exists() && file.canWrite()) {
            writerMap.put(type, Files.newBufferedWriter(pathsMap.get(type), openOptions));
            return writerMap.get(type);
        } else {
            if (file.exists()) {
                throw new IOException("File " + file.getAbsolutePath() + " is not writable");
            } else {
                throw new IOException("Failed to create file " + file.getAbsolutePath());
            }
        }
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
