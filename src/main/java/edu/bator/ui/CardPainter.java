package edu.bator.ui;

import static com.google.common.base.Strings.nullToEmpty;

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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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

public class CardPainter {

    private static final Border ORANGE_BORDER = new Border(
            new BorderStroke(Color.ORANGE, BorderStrokeStyle.DASHED, CornerRadii.EMPTY,
                    BorderStroke.MEDIUM));
    private static final Border RED_BORDER = new Border(
            new BorderStroke(Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY,
                    BorderStroke.MEDIUM));
    private static final Border LIGHTGREEN_BORDER = new Border(
            new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.DASHED, CornerRadii.EMPTY,
                    BorderStroke.MEDIUM));

    @SuppressWarnings("unchecked")
    public GridPane paint(Card card, GridPane grid, int index, GameState gameState) {
        GridPane gridPane = new GridPane();
        gridPane.setId(card.getUniqueId());
        gridPane.setPadding(new Insets(3));
        grid.add(gridPane, index, 0);

        //Image image = new Image(getClass().getResourceAsStream("/images/" + card.getCode() + ".jpg"), 190, 265, false, true);
        Image image = new Image(getClass().getResourceAsStream("/images/" + card.getCode() + ".jpg"),
                80, 133, true, true);
        ImageView cardImage = new ImageView(image);
        StringBuilder tooltipBuilder = new StringBuilder()
                .append(card.getDescription())
                .append(" Cost: ")
                .append(nullToEmpty(card.getResourceCost() + ""))
                .append(" ")
                .append(card.getAbilities().isEmpty() ? "" : card.getAbilities());

        buildWeaponText(card, tooltipBuilder, gameState);
        buildArmorText(card, tooltipBuilder, gameState);

        String tooltipText = tooltipBuilder.toString();

        Tooltip tooltip = new Tooltip(tooltipText);
        Tooltip.install(cardImage, tooltip);

        //row 1
        gridPane.add(cardImage, 0, 0, 2, 1);

        //row 2
        if (Objects.nonNull(card.getAttack(gameState))) {
            gridPane.add(new Label("ATK: " + card.getAttack(gameState) + " (" + card.getAttackType() + ")"), 0, 1);
        }

        if (Objects.nonNull(card.getBaseDefence())) {
            gridPane.add(new Label("DEF: " + card.getBaseDefence()), 0, 1);
        }

        if (Objects.nonNull(card.getCurrentHp())) {
            gridPane.add(new Label("  HP: " + card.getCurrentHp()), 1, 1);
        }

        //row 3
        if (card.cardIsAHero()) {
            StringBuilder text = new StringBuilder("SE: " + card.getShadowEnergy());
            buildWeaponText(card, text, gameState);
            buildArmorText(card, text, gameState);

            gridPane.add(new Label(text.toString()), 0, 2);
        }
        if (card.hasAbilityToUse(gameState)) {
            Button abilityButton = new Button("Ability.");
            abilityButton.setOnMouseClicked(new AbilityClickedEvent(gameState, card));
            gridPane.add(abilityButton, 1, 2);
            gridPane.setBorder(LIGHTGREEN_BORDER);
        }

        //row 4
        if ((gameState.cardIsCurrentHero(card) || gameState.cardIsInCurrentAllies(card)) && card.canAttack(gameState)) {
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
        return gridPane;
    }

    private void buildWeaponText(Card card, StringBuilder text, GameState gameState) {
        Optional.ofNullable(card.getWeapon()).ifPresent(weapon -> {
            text
                    .append("\nWeapon: ")
                    .append(weapon.getName())
                    .append(" DUR: ")
                    .append(weapon.getCurrentHp())
                    .append(" ATK: ")
                    .append(weapon.getAttack(gameState));
        });
    }

    private void buildArmorText(Card card, StringBuilder text, GameState gameState) {
        Optional.ofNullable(card.getArmor()).ifPresent(armor -> {
            text
                    .append("\nArmor: ")
                    .append(armor.getName())
                    .append(" DUR: ")
                    .append(armor.getCurrentHp())
                    .append(" DEF: ")
                    .append(armor.getBaseDefence());
        });
    }
}

