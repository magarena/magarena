package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;


public class Draining_Whelk {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                player,
                MagicTargetChoice.TARGET_SPELL,
                this,
                "Counter target spell$. Put X +1/+1 counters on SN " + 
                ", where X is that spell's converted mana cost.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetCardOnStack(game,choiceResults,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack card) {
                    game.doAction(new MagicCounterItemOnStackAction(card));
                    game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        MagicCounterType.PlusOne,
                        card.getConvertedCost(),
                        true
                    ));
                }
            });
        }
    };
}
