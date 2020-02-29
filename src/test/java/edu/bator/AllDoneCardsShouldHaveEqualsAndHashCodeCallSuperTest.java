package edu.bator;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class AllDoneCardsShouldHaveEqualsAndHashCodeCallSuperTest {

    @Test
    public void shouldTest() throws IOException, ClassNotFoundException {
        List<Path> files = Files.list(Paths.get("src", "main", "java", "edu", "bator", "cards", "done")).collect(Collectors.toList());
        for (Path file : files) {
            List<String> lines = Files.readAllLines(file);
            boolean found = lines.stream().anyMatch(
                line -> StringUtils.contains(line, "@EqualsAndHashCode(callSuper = true"));
            assertTrue(file + " should have @EqualsAndHashCode(callSuper = true)", found);
        }
    }
}
