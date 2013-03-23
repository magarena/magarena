package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Moonveil_Dragon {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                    MagicConditionFactory.ManaCost("{R}")},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                    new MagicPayManaCostEvent(
                            source,
                            source.getController(),
                            MagicManaCost.create("{R}"))};
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    this,
                    "Each creature you control gets +1/+0 until end of turn.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                for (final MagicPermanent target : targets) {
                    game.doAction(new MagicChangeTurnPTAction(target,1,0));
                }
        }
    };
}
