package edu.bator;

import java.net.URL;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import static java.util.Objects.nonNull;

public class CardParser {

    private static final Logger log = Logger.getLogger(CardParser.class);

    public static void main(String[] args) throws Exception {

        Document cardDoc = Jsoup.parse(new URL("http://www.shadowera.com/cards.php"), 10000);
        //log.info(cardDoc);
        Elements allRows = cardDoc.getElementsByTag("tr");
        for (Element row : allRows) {
            log.info(row);
            if (row.childNodeSize() > 0 &&
                    nonNull(row.childNode(0)) &&
                    row.childNode(0).childNodeSize() > 0 &&
                    "td".equalsIgnoreCase(row.childNode(0).nodeName()) &&
                    row.childNode(0).childNode(0) instanceof TextNode &&
                    ((TextNode)row.childNode(0).childNode(0)).text().matches("[a-z][a-z]\\d\\d\\d"))
            {
                log.info(row.childNode(0).childNode(0));
            }
        }
    }
}
