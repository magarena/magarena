package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTriggerType;

public class MagicClashEvent extends MagicEvent {
    
    private static MagicEventAction clashAction;
    
    public MagicClashEvent(final MagicEvent event, final MagicEventAction aClashAction) {
        this(event.getSource(), event.getPlayer(), aClashAction);
    }

    public MagicClashEvent(final MagicSource source, final MagicPlayer player, final MagicEventAction aClashAction) {
        super(
            source,
            player,
            EventAction,
            "Clash with an opponent."
        );
        clashAction = aClashAction;
    }
    
    private static final MagicEventAction EventAction = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer winner = executeClash(game, event);
            if (winner == event.getPlayer()) {
                clashAction.executeEvent(game, event);
            };
            game.executeTrigger(MagicTriggerType.WhenClash, winner);
        }
    };
    
    public static MagicPlayer executeClash(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicPlayer opponent = player.getOpponent();
        final MagicCard playerCard = player.getLibrary().getCardAtTop();
        final MagicCard opponentCard = opponent.getLibrary().getCardAtTop();
        final MagicPlayer winner;
        if (playerCard.getConvertedCost() > opponentCard.getConvertedCost()) {
            game.logAppendMessage(player, player + " won the clash.");
            winner = player;
        } else if (playerCard.getConvertedCost() < opponentCard.getConvertedCost()) {
            game.logAppendMessage(player, player + " lost the clash.");
            winner = opponent;
        } else {
            game.logAppendMessage(player, "It is a tie.");
            winner = MagicPlayer.NONE;
        }
            
        game.addFirstEvent(new MagicScryEvent(event.getSource(), player));
        game.addFirstEvent(new MagicScryEvent(event.getSource(), opponent));
                
        return winner;
    }
}
