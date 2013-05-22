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
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Stronghold_Overseer {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicConditionFactory.ManaCost("{B}{B}")},
            new MagicActivationHints(MagicTiming.Pump,false,1),
            "Pump") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                    new MagicPayManaCostEvent(source,"{B}{B}"),
                    new MagicPlayAbilityEvent(source)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    this,
                    "Creatures with shadow get +1/+0 until end of turn and " +
                    "creatures without shadow get -1/-0 until end of turn.");
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            Collection<MagicPermanent> targets =
                    game.filterPermanents(game.getPlayer(0),MagicTargetFilter.TARGET_CREATURE_WITH_SHADOW);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeTurnPTAction(target,1,0));
            }
            targets = game.filterPermanents(game.getPlayer(0),MagicTargetFilter.TARGET_CREATURE_WITHOUT_SHADOW);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeTurnPTAction(target,-1,0));
            }
        }
    };
}
