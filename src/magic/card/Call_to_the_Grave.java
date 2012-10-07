package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtEndOfTurnTrigger;
import magic.model.trigger.MagicAtUpkeepTrigger;

import java.util.Collection;

public class Call_to_the_Grave {
    public static final MagicAtUpkeepTrigger T1 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                player,
                this,
                "PN sacrifices a non-Zombie creature.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        player,
                        MagicTargetChoice.SACRIFICE_NON_ZOMBIE));
        }
    };
    
    public static final MagicAtEndOfTurnTrigger T2 = new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            final Collection<MagicTarget> targets =
                    game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_CREATURE);
            return (targets.size() == 0) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }    
    };
}
