package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
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
                this,
                "Creatures PN controls gain trample " +
                "and get +X/+X until end of turn, where X is " +
                "the number of creatures PN controls."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            final int amount = targets.size();
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
                game.doAction(new MagicChangeTurnPTAction(creature,amount,amount));
            }
        }
    };
}
