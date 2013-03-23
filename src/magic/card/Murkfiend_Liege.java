package magic.card;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;

import java.util.Collection;

public class Murkfiend_Liege {
    public static final MagicStatic S1 = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_GREEN_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    };
    public static final MagicStatic S2 = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_BLUE_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    };
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isOpponent(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Untap all green and/or blue creatures you control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                if (creature.isTapped() && 
                    (creature.hasColor(MagicColor.Blue) || 
                     creature.hasColor(MagicColor.Green))) {
                    game.doAction(new MagicUntapAction(creature));
                }
            }
        }
        @Override
        public boolean usesStack() {
            return false;
        }
    };
}
