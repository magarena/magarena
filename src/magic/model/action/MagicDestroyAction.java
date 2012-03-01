package magic.model.action;

import java.util.ArrayList;
import java.util.Collection;
import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.target.MagicTarget;

public class MagicDestroyAction extends MagicAction {
	
	final private Collection<MagicTarget> targets = new ArrayList<MagicTarget>();
	
	public MagicDestroyAction(final MagicPermanent permanent) {
		this.targets.add(permanent);
	}
	
	public MagicDestroyAction(final Collection<MagicTarget> targets) {
		this.targets.addAll(targets);
	}
	
	@Override
	public void doAction(final MagicGame game) {
		final Collection<MagicPermanent> toBeDestroyed = new ArrayList<MagicPermanent>();
		for (final MagicTarget target : targets) {
			boolean destroy = true;
			final MagicPermanent permanent = (MagicPermanent)target;
			
			// Indestructible
			if (permanent.hasAbility(game,MagicAbility.Indestructible)) {
				System.err.println("destroy - target Indestructible="+target);
	            destroy = false;
	        }
			
			// Regeneration
	        if (permanent.isRegenerated()) {
	            game.logAppendMessage(permanent.getController(),permanent.getName()+" is regenerated.");
	            game.doAction(new MagicTapAction(permanent,false));
	            game.doAction(new MagicRemoveAllDamageAction(permanent));
	            game.doAction(new MagicRemoveFromCombatAction(permanent));
	            game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.Regenerated,false));
	            destroy = false;
	        } 
	        
	        // Totem armor.
	        if (permanent.isEnchanted()) {
	            for (final MagicPermanent aura : permanent.getAuraPermanents()) {
	                if (aura.getCardDefinition().hasAbility(MagicAbility.TotemArmor)) {
	                    game.logAppendMessage(permanent.getController(),"Remove all damage from "+permanent.getName()+'.');
	                    game.doAction(new MagicRemoveAllDamageAction(permanent));
	                    game.doAction(new MagicDestroyAction(aura));
	                    destroy = false;
	                }
	            }
	        }
	        
	        if (destroy) {
				toBeDestroyed.add(permanent);
			}
		}
        
		for (final MagicPermanent permanent : toBeDestroyed) {
			// Destroyed
	        game.logAppendMessage(permanent.getController(),permanent.getName()+" is destroyed.");
	        game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Graveyard));
		}  
	}

	@Override
	public void undoAction(final MagicGame game) {}
}
