package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Ratchet_Bomb {
    public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Destroy") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicPermanent permanent=source;
            return new MagicEvent[]{
                new MagicTapEvent(permanent),
                new MagicSacrificeEvent(permanent)};
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "Destroy each nonland permanent with converted mana cost equal to the number of charge counters on SN.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent source=event.getPermanent();
            final int amount=source.getCounters(MagicCounterType.Charge);
            final Collection<MagicPermanent> targets=
                game.filterPermanents(source.getController(), 
                        new MagicTargetFilter.MagicCMCPermanentFilter(
                            MagicTargetFilter.TARGET_NONLAND_PERMANENT, 
                            MagicTargetFilter.Operator.EQUAL, 
                            amount));
            game.doAction(new MagicDestroyAction(targets));
        }
    };
}
