package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Wretched_Banquet {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target creature$ if it has the least power or is " +
                    "tied for least power among creatures on the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                            event.getPlayer(),
                            MagicTargetFilter.TARGET_CREATURE);
                    final int power = creature.getPower();
                    boolean least = true;
                    for (final MagicPermanent permanent : targets) {
                        if (permanent.getPower() < power) {
                            least = false;
                            break;
                        }
                    }
                    if (least) {
                        game.doAction(new MagicDestroyAction(creature));
                    }
                }
            });
        }
    };
}
