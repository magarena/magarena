package magic.card;

import java.util.Collection;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicSetAbilityAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Vault_of_the_Archangel {
    public static final MagicPermanentActivation A = new MagicPermanentActivation( 
            new MagicCondition[]{
                MagicManaCost.THREE_WHITE_BLACK.getCondition(), //add ONE for the card itself
                MagicCondition.CAN_TAP_CONDITION
            },
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{
                    new MagicTapEvent((MagicPermanent)source),
                    new MagicPayManaCostEvent(
                            source,
                            source.getController(),
                            MagicManaCost.TWO_WHITE_BLACK)
            };
        }
        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source.getController()},
                    this,
                    "Creatures " + source.getController() +
                    " controls gain deathtouch and lifelink until end of turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets = game.filterTargets(
                    (MagicPlayer)data[0],
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                final MagicPermanent creature = (MagicPermanent)target;
                game.doAction(new MagicSetAbilityAction(
                        creature,
                        MagicAbility.Deathtouch.getMask()|MagicAbility.LifeLink.getMask()));
            }
        }
    };
}
