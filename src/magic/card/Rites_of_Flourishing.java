package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

public class Rites_of_Flourishing {
    public static final MagicAtUpkeepTrigger T3 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN draws a card."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player,1));
        }
    };
    
    public static final MagicStatic S = new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.incMaxLand();
        }
    };
}
