package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicShuffleIntoLibraryAction;
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
            final MagicPlayer player=cardOnStack.getController();
            final MagicCard card=cardOnStack.getCard();
            final int amount=payedCost.getX();
            return new MagicEvent(
                    card,
                    player,
                    new Object[]{card,player,amount},
                    this,
                    "Put "+amount+" -1/-1 counters on each creature. " + 
                    "Shuffle " + card + " into its owner's library.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final int amount=(Integer)data[2];
            final Collection<MagicTarget> targets = 
                game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicChangeCountersAction(
                            (MagicPermanent)target,
                            MagicCounterType.MinusOne,amount,true));
            }
            game.doAction(new MagicShuffleIntoLibraryAction((MagicCard)data[0]));
        }
    };
}
