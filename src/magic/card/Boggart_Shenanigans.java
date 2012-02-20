package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Boggart_Shenanigans {    
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
	@Override
	public MagicEvent executeTrigger(
		final MagicGame game,
		final MagicPermanent permanent, 
		final MagicPermanent otherPermanent) {
	    final MagicPlayer player = permanent.getController();
	    return (permanent != otherPermanent &&
		    otherPermanent.getController() == player &&
		    otherPermanent.hasSubType(MagicSubType.Goblin, game)) ? new MagicEvent(
	                    permanent,
	                    permanent.getController(),
	                    new MagicMayChoice(
	                	    "You may have " + permanent + " deal 1 damage to target player.",
	                            MagicTargetChoice.NEG_TARGET_PLAYER),
	                    new MagicDamageTargetPicker(1),
	                    new Object[] { permanent },
	                    this,
	                    "You may$ have " + permanent + " deal 1 damage to target player$.") : MagicEvent.NONE;
	}

	@Override
    	public void executeEvent(final MagicGame game, final MagicEvent event, final Object data[], final Object[] choiceResults) {
    	    if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTarget(game, choiceResults, 1, new MagicTargetAction() {
                    public void doAction(final MagicTarget target) {
                        final MagicDamage damage = new MagicDamage( (MagicPermanent)data[0], target, 1, false);
                        game.doAction( new MagicDealDamageAction(damage) );
                    }
                });
    	    }
    	}
    };
}
