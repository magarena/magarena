package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Close_Quarters {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPlayer player = permanent.getController();
            return (player == data.getController() ) ?
                    new MagicEvent(
                            permanent,
                            player,
                            MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                            new MagicDamageTargetPicker(1),
                            new Object[]{permanent},
                            this,
                            permanent + " deals 1 damage to target creature or player$."):
                    MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage((MagicPermanent)data[0],target,1,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    };
}
