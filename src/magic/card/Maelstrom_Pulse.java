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

public class Maelstrom_Pulse {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_NONLAND_PERMANENT,
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target nonland permanent$ and all other permanents with the same name as that permanent.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent targetPermanent) {
                    final MagicTargetFilter targetFilter = 
                        new MagicTargetFilter.NameTargetFilter(targetPermanent.getName());
                    final Collection<MagicPermanent> targets = 
                        game.filterPermanents(event.getPlayer(),targetFilter);
                    for (final MagicPermanent target : targets) {
                        game.doAction(new MagicDestroyAction(target));
                    }
                }
            });
        }
    };
}
