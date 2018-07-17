package edu.bator;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.bator.cards.AllCardsSet;
import org.apache.commons.io.IOUtils;

public class ImageDownloader {

    public static void main(String[] args) {
        AllCardsSet allCardsSet = new AllCardsSet();
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
        });
    }
}
