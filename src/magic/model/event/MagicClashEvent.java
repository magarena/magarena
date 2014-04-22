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
    
    private static MagicAction clashAction;
    
    public MagicClashEvent(final MagicEvent event, final MagicAction aClashAction) {
        this(event.getSource(), event.getPlayer(), aClashAction);
    }

    public MagicClashEvent(final MagicSource source, final MagicPlayer player, final MagicAction aClashAction) {
        super(
            source,
            player,
            EventAction,
            ""
        );
        clashAction = aClashAction;
    }
    
    private static final MagicEventAction EventAction = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final boolean clashWin = executeClash(game, event);
            if (clashWin) {
                game.doAction(clashAction);                
            }
            game.executeTrigger(MagicTriggerType.WhenClash, clashWin);
        }
    };
    
    public static boolean executeClash(final MagicGame game, final MagicEvent event) {
        final boolean win;
        final MagicCard playerCard = event.getPlayer().getLibrary().getCardAtTop();
        final MagicCard opponentCard = event.getPlayer().getOpponent().getLibrary().getCardAtTop();
           
        if(playerCard.getConvertedCost() > opponentCard.getConvertedCost()) win = true;
        else win = false;
            
        game.addFirstEvent(new MagicScryXEvent(event.getSource(), event.getPlayer(), 1));
        game.addFirstEvent(new MagicScryXEvent(event.getSource(), event.getPlayer().getOpponent(), 1));
                
        return win;
    }
    
    public boolean usesStack() {
        return true;
    } 
}
