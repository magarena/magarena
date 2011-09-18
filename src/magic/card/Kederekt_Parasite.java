package magic.card;

import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenDrawnTrigger;

public class Kederekt_Parasite {
	
	private static boolean isValid(final MagicPermanent owner, final MagicGame game) {
		for (final MagicPermanent permanent : owner.getController().getPermanents()) {
			if (permanent != owner && MagicColor.Red.hasColor(permanent.getColorFlags(game))) {
				return true;
			}
		}
		return false;
	}

    public static final MagicWhenDrawnTrigger T = new MagicWhenDrawnTrigger() {
    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard data) {
    		final MagicPlayer player = data.getOwner();
    		return (permanent.getController() != player && isValid(permanent,game)) ?
    			new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{permanent,player},
                    this,
                    permanent + " deals 1 damage to your opponent."):
                MagicEvent.NONE;
    	}
    	
    	@Override
    	public void executeEvent(
    			final MagicGame game,
    			final MagicEvent event,
    			final Object data[],
    			final Object[] choiceResults) {
    		final MagicDamage damage = new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],1,false);
    		game.doAction(new MagicDealDamageAction(damage));
    	}		
    };
}
