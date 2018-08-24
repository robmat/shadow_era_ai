package edu.bator.enums;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.bator.cards.Card;
import org.junit.Assert;
import org.junit.Test;

public class AllDOneCardsCantInheritFromCard {

    @Test
    public void shouldTest() throws IOException, ClassNotFoundException {
        List<Path> files = Files.list(Paths.get("src", "main", "java", "edu", "bator", "cards", "done")).collect(Collectors.toList());
        for (Path file : files) {
            Class<?> clazz = Class.forName(file.toString().replace("src\\main\\java\\", "").replace(".java", "").replace("\\", "."));
            if (Card.class.equals(clazz.getSuperclass())) {
                Assert.fail(clazz + " should not inherit from Card.");
            }
        }
    }
}
