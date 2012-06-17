package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksTrigger;

import java.util.Collection;

public class Soltari_Champion {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent data) {
            final MagicPlayer player = permanent.getController();
            return (permanent == data) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent,player},
                    this,
                    "Other creatures " + player +
                    " controls get +1/+1 until end of turn."):
                MagicEvent.NONE;
        }    
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent = (MagicPermanent)data[0];
            final Collection<MagicTarget> targets = game.filterTargets(
                    (MagicPlayer)data[1],
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                if (target != permanent) {
                    game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,1,1));
                }
            }
        }        
    };
}
