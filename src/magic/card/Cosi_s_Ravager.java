package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicLandfallTrigger;

public class Cosi_s_Ravager {
    public static final MagicLandfallTrigger T = new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicPlayer player = permanent.getController();
            return new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                            player + " may have " + permanent + " deal 1 damage to target player",
                            MagicTargetChoice.NEG_TARGET_PLAYER),
                    new MagicDamageTargetPicker(1),
                    MagicEvent.NO_DATA,
                    this,
                    player + " may$ have " + permanent + " deal 1 damage to target player$");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTarget(game,choiceResults,1,new MagicTargetAction() {
                    public void doAction(final MagicTarget target) {
                        final MagicDamage damage = new MagicDamage(event.getPermanent(),target,1,false);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                });
            }
        }        
    };
}
