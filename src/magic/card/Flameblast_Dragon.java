package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicPayManaCostResult;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenAttacksTrigger;


public class Flameblast_Dragon {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player=permanent.getController();
			return (permanent==creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                            "You may pay {X}{R}.",
                            new MagicPayManaCostChoice(MagicManaCost.X_RED),
                            MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER), 
                        new MagicDamageTargetPicker(player.getMaximumX(game,MagicManaCost.X_RED)),
                        new Object[]{permanent},
                        this,
                        "You may pay$ {X}{R}$. If you do, " + permanent + 
                        " deals X damage to target creature or player$."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTarget(game,choiceResults,2,new MagicTargetAction() {
                    public void doAction(final MagicTarget target) {
                        final MagicPayManaCostResult payedManaCost=(MagicPayManaCostResult)choiceResults[1];
                        final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],target,payedManaCost.getX(),false);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                });
            }
		}
    };
}
