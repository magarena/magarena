package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicType;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;

import java.util.Set;

public class Eldrazi_Monument {
    public static final MagicStatic S1 = new MagicStatic(
        MagicLayer.Ability, 
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Flying);
            flags.add(MagicAbility.Indestructible);
        }
    };

    public static final MagicStatic S2 = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
    };

    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN sacrifices a creature. If you can't, sacrifice SN."):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPlayer player=event.getPlayer();
            if (player.controlsPermanentWithType(MagicType.Creature)) {
                game.addEvent(new MagicSacrificePermanentEvent(permanent,player,MagicTargetChoice.SACRIFICE_CREATURE));
            } else {
                game.doAction(new MagicSacrificeAction(permanent));                
            }            
        }
    };
}
