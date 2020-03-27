package edu.bator;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class AllDoneCardsHaveToHaveNoArgConstructorTest {

  @Test
  public void shouldTest() throws IOException, ClassNotFoundException {
    List<Path> files = Files.list(Paths.get("src", "main", "java", "edu", "bator", "cards", "done"))
        .collect(Collectors.toList());
    for (Path file : files) {
      Class<?> clazz = Class.forName(
          file.toString().replace("src\\main\\java\\", "").replace(".java", "").replace("\\", "."));
      try {
        clazz.getConstructor().newInstance();
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        Assert.fail(e.getMessage());
      }
    }
  }
}
