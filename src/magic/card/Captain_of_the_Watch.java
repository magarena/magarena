package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

import java.util.Set;

public class Captain_of_the_Watch {
    public static final MagicStatic S1 = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_SOLDIER_YOU_CONTROL) {
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
        MagicLayer.Ability, 
        MagicTargetFilter.TARGET_SOLDIER_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Vigilance);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    };

    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    this,
                    "PN puts three 1/1 white Soldier creature tokens onto the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            for (int count=3;count>0;count--) {
                game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Soldier")));
            }
        }        
    };
}
