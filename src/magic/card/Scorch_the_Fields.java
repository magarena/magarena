package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
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

public class Scorch_the_Fields {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_LAND,
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target land$. " + 
                    "SN deals 1 damage to each Human creature.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                    final Collection<MagicTarget> targets = game.filterTargets(
                            event.getPlayer(),
                            MagicTargetFilter.TARGET_HUMAN);
                    for (final MagicTarget target : targets) {
                        final MagicDamage damage = new MagicDamage(
                                event.getSource(),
                                target,
                                1,
                                false);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                }
            });
        }
    };
}
