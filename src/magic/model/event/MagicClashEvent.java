package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenClashTrigger;
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
            "Clash with an opponent"
        );
        clashAction = aClashAction;
    }
    
    private static final MagicEventAction EventAction = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final boolean clashWin = executeClash(game, event);
            if (clashWin) {
                clashAction.executeEvent(game, event);
            };
            game.executeTrigger(MagicTriggerType.WhenClash, clashWin);
        }
    };
    
    public static boolean executeClash(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicPlayer opponent = player.getOpponent();
        final MagicCard playerCard = player.getLibrary().getCardAtTop();
        final MagicCard opponentCard = opponent.getLibrary().getCardAtTop();
           
        final boolean win = playerCard.getConvertedCost() > opponentCard.getConvertedCost();
            
        game.addFirstEvent(new MagicScryEvent(event.getSource(), player));
        game.addFirstEvent(new MagicScryEvent(event.getSource(), opponent));
                
        return win;
    }
}
