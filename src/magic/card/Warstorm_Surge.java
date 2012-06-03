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
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Warstorm_Surge {
	public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player = permanent.getController();
            return (otherPermanent.isCreature() && otherPermanent.getController() == player) ?
				new MagicEvent(
                        permanent,
                        player,
                        MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                        new MagicDamageTargetPicker(otherPermanent.getPower()),
                        new Object[]{otherPermanent},
                        this,
                        "Whenever a creature enters the battlefield under your control, " +
                        "it deals damage equal to its power to target creature or player$.") :
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			final MagicPermanent permanent = (MagicPermanent)data[0];
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage(permanent,target,permanent.getPower(),false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
		}	
    };
}
