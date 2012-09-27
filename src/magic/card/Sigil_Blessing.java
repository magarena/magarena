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
import magic.model.target.MagicPumpTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Sigil_Blessing {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                    MagicPumpTargetPicker.create(),
                    this,
                    "Until end of turn, target creature$ PN controls gets +3/+3 and other creatures PN controls get +1/+1.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final Collection<MagicTarget> targets=game.filterTargets(
                            event.getPlayer(),
                            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                    for (final MagicTarget target : targets) {
                        final MagicPermanent permanent=(MagicPermanent)target;
                        if (permanent==creature) {
                            game.doAction(new MagicChangeTurnPTAction(permanent,3,3));
                        } else {
                            game.doAction(new MagicChangeTurnPTAction(permanent,1,1));
                        }
                    }
                }
            });
        }
    };
}
