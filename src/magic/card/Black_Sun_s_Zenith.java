package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeCardDestinationAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Black_Sun_s_Zenith {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                    cardOnStack,
                    new Object[]{amount},
                    this,
                    "Put "+amount+" -1/-1 counters on each creature. " + 
                    "Shuffle " + cardOnStack + " into its owner's library.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final int amount=(Integer)data[0];
            final Collection<MagicTarget> targets = 
                game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicChangeCountersAction(
                            (MagicPermanent)target,
                            MagicCounterType.MinusOne,amount,true));
            }
            game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.OwnersLibrary));
        }
    };
}
