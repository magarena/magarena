package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

import java.util.Collection;

public class Craterhoof_Behemoth {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    this,
                    "Creatures " + player + " controls gain trample " +
                    "and get +X/+X until end of turn, where X is " +
                    "the number of creatures " + player + " controls.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets = game.filterTargets(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            final int amount = targets.size();
            for (final MagicTarget target : targets) {
                final MagicPermanent creature = (MagicPermanent)target;
                game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
                game.doAction(new MagicChangeTurnPTAction(creature,amount,amount));
            }
        }
    };
}
