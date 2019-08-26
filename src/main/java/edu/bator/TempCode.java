package edu.bator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static edu.bator.EntryPoint.objectJsonMapper;

public class TempCode {

    public static void main(String[] args) throws IOException {
        //download images
        /* AllCardsSet allCardsSet = new AllCardsSet();
        allCardsSet.getAllCards().forEach(card -> {
            try {
                URL url = new URL("http://www.shadowera.com/cards/"+card.getCode()+".jpg");
                URLConnection c = url.openConnection();
                InputStream is = c.getInputStream();
                byte[] bytes = IOUtils.toByteArray(is);
                Files.write(Paths.get("src", "main", "resources", "images", card.getCode() + ".jpg"), bytes);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/

        //write specific card classes
        /* for (Card card : new AllCardsSet().getAllCards()) {
                try {
                    String name = card.getName().replaceAll("[ :'!,-]", "");
                    Files.write(Paths.get("src","main","java","edu","bator","cards","todo", name + ".java"), String.format("package edu.bator.cards.todo;\n" +
                            "\n" +
                            "import edu.bator.cards.Card;\n" +
                            "\n" +
                            "public class %s extends Card {\n" +
                            "    public %s(Card cloneFrom) {\n" +
                            "        super(cloneFrom);\n" +
                            "    }\n" +
                            "}", name, name).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        //write card class files
        List<Map> cards = objectJsonMapper
                .readValue(CardParser.class.getResourceAsStream("/cards.json"), List.class);


        for (Map card : cards) {
            String name = card.get("name").toString();
            Path cardClassFile = Paths.get("src", "main", "java", "edu", "bator", "cards", "todo", name.replaceAll("[ :'!,-]", ""), ".java");
            if (!Files.exists(cardClassFile)) {
                System.out.println(name);
                name = name.replaceAll("[ :'!,-]", "");
                Files.write(Paths.get("src","main","java","edu","bator","cards","todo", name + ".java"), String.format("package edu.bator.cards.todo;\n" +
                        "\n" +
                        "import edu.bator.cards.Card;\n" +
                        "\n" +
                        "public class %s extends Card {\n" +
                        "    public %s(Card cloneFrom) {\n" +
                        "        super(cloneFrom);\n" +
                        "    }\n" +
                        "}", name, name).getBytes());
            }
        }
    }
}
