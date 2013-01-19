package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPlayTokenAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

import java.util.Set;

public class Wren_s_Run_Packmaster {
    public static final MagicStatic S = new MagicStatic(
            MagicLayer.Ability, 
            MagicTargetFilter.TARGET_WOLF_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Deathtouch);
        }
    };
        
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.TWO_GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Token,true),
            "Token"
            ) {
        
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_GREEN)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "PN puts a 2/2 green Wolf creature token onto the battlefield.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("Wolf")));
        }
    };
}
