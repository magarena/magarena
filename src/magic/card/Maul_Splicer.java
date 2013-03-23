package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokensAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

import java.util.Set;

public class Maul_Splicer {
    public static final MagicStatic S = new MagicStatic(
            MagicLayer.Ability, 
            MagicTargetFilter.TARGET_GOLEM_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Trample);
        }
    };
        
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    this,
                    "PN puts two 3/3 colorless Golem artifact creature tokens onto the battlefield.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Golem3"),
                2
            ));
        }        
    };
}
