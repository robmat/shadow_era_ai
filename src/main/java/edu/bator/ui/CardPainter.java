package edu.bator.ui;

import java.util.HashMap;
import java.util.Objects;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;

public class CardPainter {

    private static final Logger log = Logger.getLogger(CardPainter.class);

    @SuppressWarnings("unchecked")
    public void paint(Card card, GridPane enemyHand, int index, GameState gameState) {
        GridPane gridPane = new GridPane();
        HashMap<Object, Object> userData = new HashMap<>();
        userData.put("card", card);
        userData.put("gameState", gameState);
        gridPane.setUserData(userData);
        gridPane.setPadding(new Insets(3));
        enemyHand.add(gridPane, index, 0);

        //Image image = new Image(getClass().getResourceAsStream("/images/" + card.getCode() + ".jpg"), 190, 265, false, true);
        Image image = new Image(getClass().getResourceAsStream("/images/" + card.getCode() + ".jpg"), 80, 133, true, true);
        ImageView cardImage = new ImageView(image);
        gridPane.add(cardImage, 0, 0, 2, 1);

        if (Objects.nonNull(card.getAttack())) {
            gridPane.add(new Label("ATK: " + card.getAttack() + " (" + card.getAttackType() + ")"), 0, 1);
        }

        if (Objects.nonNull(card.getCurrentHp())) {
            gridPane.add(new Label("  HP: " + card.getCurrentHp()), 1, 1);
        }

        cardImage.setOnMouseClicked((event) -> {
            if (gameState.getGamePhase().equals(GamePhase.YOU_ACTION)) {
                ImageView eventTarget = (ImageView) event.getTarget();
                HashMap<Object, Object> data = (HashMap<Object, Object>) eventTarget.getUserData();
                Card c = (Card) data.get("card");
                GameState gs = (GameState) data.get("gameState");
            }
        });
    }
}

