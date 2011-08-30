package magic.model.action;

import magic.model.*;

public class MagicDestroyAction extends MagicAction {
	
	private final MagicPermanent permanent;
	
	public MagicDestroyAction(final MagicPermanent permanent) {
		this.permanent=permanent;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		if (!permanent.hasAbility(game,MagicAbility.Indestructible)) {		
			if (permanent.isRegenerated()) {
				game.logAppendMessage(permanent.getController(),permanent.getName()+" is regenerated.");
				game.doAction(new MagicTapAction(permanent,false));
				game.doAction(new MagicRemoveAllDamageAction(permanent));
				game.doAction(new MagicRemoveFromCombatAction(permanent));
				game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.Regenerated,false));
			} else {
				// Totem armor.
				if (permanent.isEnchanted()) {
					for (final MagicPermanent aura : permanent.getAuraPermanents()) {
						
						if (aura.getCardDefinition().hasAbility(MagicAbility.TotemArmor)) {
							game.logAppendMessage(permanent.getController(),"Remove all damage from "+permanent.getName()+'.');
							game.doAction(new MagicRemoveAllDamageAction(permanent));
							game.doAction(new MagicDestroyAction(aura));
							return;
						}
					}
				}							
				game.logAppendMessage(permanent.getController(),permanent.getName()+" is destroyed.");
				game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Graveyard));
			}
		}
	}

	@Override
	public void undoAction(final MagicGame game) {
		
	}
}
