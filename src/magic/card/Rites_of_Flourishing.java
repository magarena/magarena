package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLandPlayedAction;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Rites_of_Flourishing {
    public static final MagicWhenComesIntoPlayTrigger T1 = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                this,
                "PN may play an additional land this turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeLandPlayedAction(-1));
        }
    };
    
    public static final MagicAtUpkeepTrigger T2 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN may play an additional land this turn."
            );
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeLandPlayedAction(-1));
        }        
    };
    
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
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {    
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player,1));
        }
    };
}
