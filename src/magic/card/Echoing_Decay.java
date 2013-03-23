package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicWeakenTargetPicker;

import java.util.Collection;

public class Echoing_Decay {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicWeakenTargetPicker(2,2),
                    this,
                    "Target creature$ and all other creatures with the same " +
                    "name as that creature get -2/-2 until end of turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent targetPermanent) {
                    final MagicTargetFilter<MagicPermanent> targetFilter = 
                        new MagicTargetFilter.NameTargetFilter(targetPermanent.getName());
                    final Collection<MagicPermanent> targets = 
                        game.filterPermanents(event.getPlayer(),targetFilter);
                    for (final MagicPermanent permanent : targets) {
                        if (permanent.isCreature()) {
                            game.doAction(new MagicChangeTurnPTAction(permanent,-2,-2));
                        }
                    }
                }
            });
        }
    };
}
