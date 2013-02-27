package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Iizuka_the_Ruthless {
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                    MagicConditionFactory.ManaCost("{2}{R}"),
                    MagicCondition.ONE_CREATURE_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Strike") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                    new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{2}{R}")),
                    new MagicSacrificePermanentEvent(
                            source,
                            source.getController(),
                            MagicTargetChoice.SACRIFICE_SAMURAI)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "Samurai creatures PN controls gain double strike until end of turn.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_SAMURAI_YOU_CONTROL);
                for (final MagicPermanent creature : targets) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.DoubleStrike));
                }
        }
    };
}
