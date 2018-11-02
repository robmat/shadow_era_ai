package edu.bator.cards.done;

import edu.bator.EntryPoint;
import edu.bator.cards.Artifact;
import edu.bator.cards.Card;
import edu.bator.cards.Weapon;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import edu.bator.game.GraveyardLinkedList;
import edu.bator.ui.cards.CardPainter;
import edu.bator.ui.cards.CardUiHelper;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.EqualsAndHashCode;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
public class ReserveWeapon extends Artifact {

    public ReserveWeapon() {
    }

    public ReserveWeapon(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void supportIsCast(GameState gameState) {
        super.supportIsCast(gameState);
    }

    @Override
    public Integer modifiesAttack(Card card, GameState gameState) {
        boolean cardIsAWeapon = card.cardIsAWeapon();
        boolean yourHeroWeapon = card.equals(gameState.getYourHero().getWeapon());
        boolean thisInYourSupport = gameState.getYourSupport().contains(this);
        if (cardIsAWeapon &&
                yourHeroWeapon &&
                thisInYourSupport) {
            return 1;
        }
        boolean enemyHeroWeapon = card.equals(gameState.getEnemyHero().getWeapon());
        boolean thisInEnemySupport = gameState.getEnemySupport().contains(this);
        if (cardIsAWeapon &&
                enemyHeroWeapon &&
                thisInEnemySupport) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean hasAbilityToUse(GameState gameState) {
        return isYou(gameState) || isEnemy(gameState);
    }

    @Override
    public void abilityClickEvent(GameState gameState) {
        BiConsumer<Stage, GridPane> consumer = (d, g) -> paintUi(gameState, d, g);
        CardUiHelper.showDialog(consumer);
    }

    private void paintUi(GameState gameState, Stage dialog, GridPane cardsGrid) {
        GraveyardLinkedList graveyard = new GraveyardLinkedList();
        if (isYou(gameState)) {
            graveyard = gameState.getYourGraveyard();
        }
        if (isEnemy(gameState)) {
            graveyard = gameState.getEnemyGraveyard();
        }
        AtomicInteger index = new AtomicInteger(0);
        graveyard
                .stream()
                .filter(Card::cardIsAWeapon)
                .forEach(card -> {
                    EventHandler<MouseEvent> reserveWeaponClickedEvent = new ReserveWeaponClickedEvent(gameState, card, dialog, this);
                    CardUiHelper.paintCardOnDialogGridPane(gameState, cardsGrid, index, card, reserveWeaponClickedEvent);
                });

    }

    private boolean isEnemy(GameState gameState) {
        return GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
                gameState.getEnemyGraveyard().stream().anyMatch(Card::cardIsAWeapon) &&
                gameState.getEnemySupport().contains(this);
    }

    private boolean isYou(GameState gameState) {
        return GamePhase.YOU_ACTION.equals(gameState.getGamePhase()) &&
                gameState.getYourGraveyard().stream().anyMatch(Card::cardIsAWeapon) &&
                gameState.getYourSupport().contains(this);
    }

    @Override
    public void applyAbility(Card target, GameState gameState) {
        super.applyAbility(target, gameState);
    }

    public class ReserveWeaponClickedEvent implements EventHandler<MouseEvent> {

        private final GameState gameState;
        private final Card target;
        private final Stage dialog;
        private final ReserveWeapon source;

        ReserveWeaponClickedEvent(GameState gameState, Card target, Stage dialog,
                                  ReserveWeapon reserveWeapon) {
            this.gameState = gameState;
            this.target = target;
            this.dialog = dialog;
            this.source = reserveWeapon;
        }

        @Override
        public void handle(MouseEvent event) {
            target.resetAttackAnHp();
            target.setBaseAttack(target.getBaseAttack() + 1);
            if (gameState.getYourGraveyard().remove(target)) {
                gameState.getYourHero().setWeapon((Weapon) target);
                if (gameState.getYourSupport().remove(source)) {
                    gameState.getYourGraveyard().add(source);
                }
            }
            if (gameState.getEnemyGraveyard().remove(target)) {
                gameState.getEnemyHero().setWeapon((Weapon) target);
                if (gameState.getEnemySupport().remove(source)) {
                    gameState.getEnemyGraveyard().add(source);
                }
            }
            dialog.close();
        }
    }
}
