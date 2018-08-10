package edu.bator;

public class TempCode {

    public static void main(String[] args) {
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
    }
}
