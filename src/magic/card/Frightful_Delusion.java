package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.action.MagicCardOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicCounterUnlessEvent;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Frightful_Delusion {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_SPELL,
                    this,
                    "Counter target spell$ unless its controller pays {1}. " +
                    "That player discards a card.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    game.addEvent(new MagicCounterUnlessEvent(event.getSource(),targetSpell,MagicManaCost.create("{1}")));
                    game.addEvent(new MagicDiscardEvent(event.getSource(),targetSpell.getController(),1,false));
                }
            });
        }
    };
}
