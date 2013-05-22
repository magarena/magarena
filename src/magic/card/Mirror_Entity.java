package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicPlayAbilityAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;
import java.util.Set;

public class Mirror_Entity {
    public static final MagicPermanentActivation A  = new MagicPermanentActivation( 
            new MagicCondition[]{MagicConditionFactory.ManaCost("{X}")},
            new MagicActivationHints(MagicTiming.Pump,true,1),
            "X/X") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                    new MagicPayManaCostEvent(source,"{X}"),
                    new MagicPlayAbilityEvent(source)};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int x=payedCost.getX();
            return new MagicEvent(
                    source,
                    x,
                    this,
                    "Creatures PN controls become "+
                    x+"/"+x+" and gain all creature types until end of turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final int X = event.getRefInt();
            final MagicPermanent permanent = event.getPermanent();
            final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
                @Override
                public void modPowerToughness(
                        final MagicPermanent source,
                        final MagicPermanent permanent,
                        final MagicPowerToughness pt) {
                    pt.set(X,X);
                }
            };
            final MagicStatic ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
                @Override
                public void modSubTypeFlags(
                        final MagicPermanent permanent,
                        final Set<MagicSubType> flags) {
                    flags.addAll(MagicSubType.ALL_CREATURES);
                }
            };
            final Collection<MagicPermanent> creatures=game.filterPermanents(
                    permanent.getController(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicBecomesCreatureAction(creature,PT,ST));
            }
            game.doAction(new MagicPlayAbilityAction(permanent));
        }
    };
}
