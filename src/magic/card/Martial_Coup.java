package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;
import java.util.Collections;

public class Martial_Coup {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int x=payedCost.getX();
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Put "+x+" 1/1 white Soldier creature tokens onto the battlefield." + 
                    (x >= 5 ? " Destroy all other creatures.":""));
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player=event.getPlayer();
            int x = event.getCardOnStack().getX();
            if (x >= 5) {
                final Collection<MagicTarget> targets = game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE);
                for (final MagicTarget target : targets) {
                    game.doAction(new MagicDestroyAction((MagicPermanent)target));
                }
            } 
            for (;x>0;x--) {
                game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Soldier")));
            }
        }
    };
}
