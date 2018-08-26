package edu.bator.enums;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class EnumParserTest {

    @Test
    @Ignore
    public void parse() throws IOException {
        Files.list(Paths.get("src", "main", "java", "edu", "bator", "cards", "todo")).forEach(cardFile -> {
            try {
                List<String> lines = new ArrayList<>(Files.readAllLines(cardFile));
                int index = -1;
                String cardName = null;
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains("public class")) {
                        index = i;
                        cardName = lines.get(i).substring(12, lines.get(i).indexOf(" extends ")).trim();
                    }
                }
                lines.add(2, "import lombok.EqualsAndHashCode;");
                lines.add(index - 1, "public " + cardName + "() {};");
                //Files.write(cardFile, lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}