package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Overwhelming_Stampede {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Until end of turn, creatures you control gain trample and get +X/+X, where X is the greatest power among creatures you control.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            int power=0;
            for (final MagicPermanent creature : targets) {
                power=Math.max(power,creature.getPowerToughness().power());
            }
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicChangeTurnPTAction(creature,power,power));
                game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
            }
        }
    };
}
