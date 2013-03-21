package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicPowerTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Alpha_Brawl {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    MagicPowerTargetPicker.getInstance(),
                    this,
                    "Target creature$ an opponent controls deals damage equal to " +
                    "its power to each other creature that player controls, then " +
                    "each of those creatures deals damage equal to its power to that creature.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                final MagicPlayer player = event.getPlayer();
                public void doAction(final MagicPermanent permanent) {
                    final Collection<MagicPermanent> creatures = 
                            game.filterPermanents(player.getOpponent(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                    for (final MagicPermanent creature : creatures) {
                        final MagicDamage damage = new MagicDamage(
                            permanent,
                            creature,
                            permanent.getPower()
                        );
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                    for (final MagicPermanent creature : creatures) {
                        final MagicDamage damage = new MagicDamage(
                            creature,
                            permanent,
                            creature.getPower()
                        );
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                }
            });
        }
    };
}
