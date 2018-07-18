package edu.bator.ui;

import java.util.Objects;

import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import edu.bator.ui.events.AttackClickedEvent;
import edu.bator.ui.events.AttackTargetClickedEvent;
import edu.bator.ui.events.CardCastClickedEvent;
import edu.bator.ui.events.CardSacrificeClickedEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

class CardPainter {

    private static final Logger log = Logger.getLogger(CardPainter.class);

    @SuppressWarnings("unchecked")
    void paint(Card card, GridPane enemyHand, int index, GameState gameState) {
        GridPane gridPane = new GridPane();
        gridPane.setId(card.getUniqueId());
        gridPane.setPadding(new Insets(3));
        enemyHand.add(gridPane, index, 0);

        //Image image = new Image(getClass().getResourceAsStream("/images/" + card.getCode() + ".jpg"), 190, 265, false, true);
        Image image = new Image(getClass().getResourceAsStream("/images/" + card.getCode() + ".jpg"), 80, 133, true, true);
        ImageView cardImage = new ImageView(image);
        Tooltip.install(cardImage, new Tooltip(card.getDescription()));
        gridPane.add(cardImage, 0, 0, 2, 1);

        if (Objects.nonNull(card.getAttack())) {
            gridPane.add(new Label("ATK: " + card.getAttack() + " (" + card.getAttackType() + ")"), 0, 1);
        }

        if (Objects.nonNull(card.getCurrentHp())) {
            gridPane.add(new Label("  HP: " + card.getCurrentHp()), 1, 1);
        }

        if (gameState.cardIsInHand(card) && GameEngine.SACRIFICE_PHASES.contains(gameState.getGamePhase())) {
            gridPane.setOnMouseClicked(new CardSacrificeClickedEvent(card, gameState));
            gridPane.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
        }

        if (gameState.cardIsInHand(card) && card.isCastable() && GameEngine.ACTION_PHASES.contains(gameState.getGamePhase())) {
            gridPane.setOnMouseClicked(new CardCastClickedEvent(gameState, card));
            gridPane.setBorder(new Border(new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
        }

        if (gameState.cardIsInAllies(card) && card.isReadied() && GameEngine.ACTION_PHASES.contains(gameState.getGamePhase())) {
            Button attackButton = new Button("Attack.");
            attackButton.setOnMouseClicked(new AttackClickedEvent(gameState, card));
            gridPane.add(attackButton, 0, 2, 2, 1);
            gridPane.setBorder(new Border(new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
        }

        if (GameEngine.ACTION_PHASES.contains(gameState.getGamePhase()) && card.isPossibleAttackTarget()) {
            Button attackButton = new Button("Target.");
            attackButton.setOnMouseClicked(new AttackTargetClickedEvent(gameState, card));
            gridPane.add(attackButton, 0, 2, 2, 1);
            gridPane.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
        }
    }
}

