package edu.bator.ui;

import java.util.Objects;
import java.util.stream.Collectors;

import edu.bator.cards.Card;
import edu.bator.cards.effects.Effect;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import edu.bator.ui.events.AbilityClickedEvent;
import edu.bator.ui.events.AbilityTargetClickedEvent;
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

    public static final Border ORANGE_BORDER = new Border(
            new BorderStroke(Color.ORANGE, BorderStrokeStyle.DASHED, CornerRadii.EMPTY,
                    BorderStroke.MEDIUM));
    public static final Border RED_BORDER = new Border(
            new BorderStroke(Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY,
                    BorderStroke.MEDIUM));
    public static final Border LIGHTGREEN_BORDER = new Border(
            new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.DASHED, CornerRadii.EMPTY,
                    BorderStroke.MEDIUM));
    private static final Logger log = Logger.getLogger(CardPainter.class);

    @SuppressWarnings("unchecked")
    void paint(Card card, GridPane enemyHand, int index, GameState gameState) {
        GridPane gridPane = new GridPane();
        gridPane.setId(card.getUniqueId());
        gridPane.setPadding(new Insets(3));
        enemyHand.add(gridPane, index, 0);

        //Image image = new Image(getClass().getResourceAsStream("/images/" + card.getCode() + ".jpg"), 190, 265, false, true);
        Image image = new Image(getClass().getResourceAsStream("/images/" + card.getCode() + ".jpg"),
                80, 133, true, true);
        ImageView cardImage = new ImageView(image);
        Tooltip.install(cardImage, new Tooltip(card.getDescription()));

        //row 1
        gridPane.add(cardImage, 0, 0, 2, 1);

        //row 2
        if (Objects.nonNull(card.getAttack())) {
            gridPane.add(new Label("ATK: " + card.getAttack() + " (" + card.getAttackType() + ")"), 0, 1);
        }

        if (Objects.nonNull(card.getCurrentHp())) {
            gridPane.add(new Label("  HP: " + card.getCurrentHp()), 1, 1);
        }

        //row 3
        if (card.cardIsAHero()) {
            gridPane.add(new Label("SE: " + card.getShadowEnergy()), 0, 2);
        }
        if (card.hasAbilityToUse(gameState)) {
            Button abilityButton = new Button("Ability.");
            abilityButton.setOnMouseClicked(new AbilityClickedEvent(gameState, card));
            gridPane.add(abilityButton, 1, 2);
            gridPane.setBorder(LIGHTGREEN_BORDER);
        }

        //row 4
        if (gameState.cardIsInAllies(card) && card.isAttackReadied() && GameEngine.ACTION_PHASES
                .contains(gameState.getGamePhase())) {
            Button attackButton = new Button("Attack.");
            attackButton.setOnMouseClicked(new AttackClickedEvent(gameState, card));
            gridPane.add(attackButton, 0, 3, 2, 1);
            gridPane.setBorder(LIGHTGREEN_BORDER);
        }

        if (GameEngine.ACTION_PHASES.contains(gameState.getGamePhase()) && (
                card.isPossibleAttackTarget() || card.isPossibleAbilityTarget())) {
            Button attackButton = new Button("Target.");
            if (card.isPossibleAttackTarget()) {
                attackButton.setOnMouseClicked(new AttackTargetClickedEvent(gameState, card));
            }
            if (card.isPossibleAbilityTarget()) {
                attackButton.setOnMouseClicked(new AbilityTargetClickedEvent(gameState, card));
            }
            gridPane.add(attackButton, 0, 3, 2, 1);
            gridPane.setBorder(ORANGE_BORDER);
        }
        //row 5
        if (!card.getEffects().isEmpty()) {
            String effectsString = card.getEffects()
                    .stream()
                    .map(Effect::getEffectType)
                    .collect(Collectors.toList())
                    .toString();
            gridPane.add(new Label(effectsString.substring(1, effectsString.length() - 1)), 0, 4, 2, 1);
        }
        //end row 5

        if (gameState.cardIsInHand(card) && GameEngine.SACRIFICE_PHASES
                .contains(gameState.getGamePhase())) {
            gridPane.setOnMouseClicked(new CardSacrificeClickedEvent(card, gameState));
            gridPane.setBorder(RED_BORDER);
        }

        if (gameState.cardIsInHand(card) && card.isCastable() && GameEngine.ACTION_PHASES
                .contains(gameState.getGamePhase())) {
            gridPane.setOnMouseClicked(new CardCastClickedEvent(gameState, card));
            gridPane.setBorder(LIGHTGREEN_BORDER);
        }

    }
}

