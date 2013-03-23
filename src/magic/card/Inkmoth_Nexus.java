package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicType;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

import java.util.Set;

public class Inkmoth_Nexus {
    
    private static final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(1,1);
        }
    };
    private static final MagicStatic AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Flying);
            flags.add(MagicAbility.Infect);
        }
    };
    private static final MagicStatic ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Artifact.getMask()|MagicType.Creature.getMask();
        }
    };

    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{new MagicArtificialCondition(
                    MagicConditionFactory.ManaCost("{1}"),
                    MagicConditionFactory.ManaCost("{1}"))},
            new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{1}"))};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this, 
                    "SN becomes a 1/1 Blinkmoth artifact creature with flying and infect until end of turn. " + 
                    "It's still a land.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            game.doAction(new MagicBecomesCreatureAction(permanent,PT,AB,ST));
        }
    };
}
