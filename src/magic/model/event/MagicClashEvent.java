package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.trigger.MagicTriggerType;

public class MagicClashEvent extends MagicEvent {

    public MagicClashEvent(final MagicEvent event, final MagicEventAction aClashAction) {
        this(event.getSource(), event.getPlayer(), aClashAction);
    }

    public MagicClashEvent(final MagicSource source, final MagicPlayer player, final MagicEventAction aClashAction) {
        super(
            source,
            player,
            EventAction(aClashAction),
            "Clash with an opponent."
        );
    }

    public static final MagicEventAction EventAction(final MagicEventAction clashAction) {
        return (final MagicGame game, final MagicEvent event) -> {
            final MagicPlayer winner = executeClash(game, event);
            if (winner == event.getPlayer()) {
                clashAction.executeEvent(game, event);
            };
            game.executeTrigger(MagicTriggerType.WhenClash, winner);
        };
    }

    public static MagicPlayer executeClash(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicPlayer opponent = player.getOpponent();
        final MagicCardList clashCards = player.getLibrary().getCardsFromTop(1);
        clashCards.addAll(opponent.getLibrary().getCardsFromTop(1));

        // 701.20c A player wins a clash if that player revealed a card with a
        // higher converted mana cost than all other cards revealed in that clash.
        MagicPlayer winner = MagicPlayer.NONE;
        int maxCMC = -1;
        for (final MagicCard card : clashCards) {
            if (card.getConvertedCost() > maxCMC) {
                maxCMC = card.getConvertedCost();
                winner = card.getOwner();
            } else if (card.getConvertedCost() == maxCMC) {
                winner = MagicPlayer.NONE;
            }
        }

        if (winner == MagicPlayer.NONE) {
            game.logAppendMessage(player, "It is a tie.");
        } else if (winner == player) {
            game.logAppendMessage(player, player + " won the clash.");
        } else {
            game.logAppendMessage(player, player + " lost the clash.");
        }

        game.addFirstEvent(MagicScryEvent.Pseudo(event.getSource(), opponent));
        game.addFirstEvent(MagicScryEvent.Pseudo(event.getSource(), player));

        return winner;
    }
}
