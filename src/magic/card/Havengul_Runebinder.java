package magic.card;

import java.util.Collection;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicExileCardEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Havengul_Runebinder {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                    MagicCondition.CAN_TAP_CONDITION,
                    MagicManaCost.TWO_BLUE.getCondition(),
                    MagicCondition.GRAVEYARD_CONTAINS_CREATURE
            },
            new MagicActivationHints(MagicTiming.Token),
            "Token") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{
                    new MagicPayManaCostTapEvent(
                            source,
                            source.getController(),
                            MagicManaCost.TWO_BLUE),
                    new MagicExileCardEvent(
                            source,
                            source.getController(),
                            MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD)
            };
        }
        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    this,
                    "Put a 2/2 black Zombie creature token onto the battlefield, " +
                    "then put a +1/+1 counter on each Zombie creature you control.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicPlayTokenAction(
                    player,
                    TokenCardDefinitions.get("Zombie")));
            final Collection<MagicTarget> targets = game.filterTargets(
                    player,
                    MagicTargetFilter.TARGET_ZOMBIE_YOU_CONTROL);        
                for (final MagicTarget target : targets) {
                    game.doAction(new MagicChangeCountersAction(
                            (MagicPermanent)target,
                            MagicCounterType.PlusOne,
                            1,
                            true));
                }
        }
    };
}
