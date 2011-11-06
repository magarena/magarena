package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.trigger.MagicTriggerType;
import magic.model.mstatic.MagicPermanentStatic;
import java.util.Collection;

public class MagicRemoveFromPlayAction extends MagicAction {

	private final MagicPermanent permanent;
	private final MagicLocationType toLocation;
	private Collection<MagicPermanentTrigger> removedTriggers;
	private Collection<MagicPermanentStatic> removedStatics;
	private boolean valid;
	
	public MagicRemoveFromPlayAction(final MagicPermanent permanent,final MagicLocationType toLocation) {
		this.permanent=permanent;
		this.toLocation=toLocation;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		final MagicPlayer controller=permanent.getController();
		
		// Check if this is still a valid action.
		valid=controller.controlsPermanent(permanent);
		if (!valid) {
			return;
		}
			
		final int score=permanent.getScore(game)+permanent.getStaticScore(game);

		// Execute trigger here so that full permanent state is preserved.
		if (toLocation==MagicLocationType.Graveyard) {
			game.executeTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,permanent);
			if (permanent.isCreature(game)) {
				game.setCreatureDiedThisTurn(true);
			}
		}
		
		// Equipment
		if (permanent.getEquippedCreature().isValid()) {
			permanent.getEquippedCreature().removeEquipment(permanent);
		}
		for (final MagicPermanent equipment : permanent.getEquipmentPermanents()) {
			equipment.setEquippedCreature(MagicPermanent.NONE);
		}

		// Aura
		if (permanent.getEnchantedCreature().isValid()) {
			permanent.getEnchantedCreature().removeAura(permanent);
		}
		for (final MagicPermanent aura : permanent.getAuraPermanents()) {
			aura.setEnchantedCreature(MagicPermanent.NONE);
		}

		game.doAction(new MagicRemoveFromCombatAction(permanent));

		controller.removePermanent(permanent);

		setScore(controller,permanent.getStaticScore(game)-score);
	
        game.executeTrigger(MagicTriggerType.WhenLeavesPlay,permanent);
        
        // Trigger
		removedTriggers = game.removeTriggers(permanent);
		
        // Static
        removedStatics = game.removeAllStatics(permanent);
        
		game.doAction(new MagicMoveCardAction(permanent,toLocation));
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {

		if (!valid) {
			return;
		}
		
		permanent.getController().addPermanent(permanent);
		
		// Equipment
		if (permanent.getEquippedCreature().isValid()) {
			permanent.getEquippedCreature().addEquipment(permanent);
		}
		for (final MagicPermanent equipment : permanent.getEquipmentPermanents()) {
			equipment.setEquippedCreature(permanent);
		}
		
		// Aura
		if (permanent.getEnchantedCreature().isValid()) {
			permanent.getEnchantedCreature().addAura(permanent);
		}
		for (final MagicPermanent aura : permanent.getAuraPermanents()) {
			aura.setEnchantedCreature(permanent);
		}

        // Trigger
		for (final MagicPermanentTrigger permanentTrigger : removedTriggers) {
			game.addTrigger(permanentTrigger);
		}

        // Static
		for (final MagicPermanentStatic permanentStatic : removedStatics) {
			game.addStatic(permanentStatic);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+" ("+permanent.getName()+','+permanent.getAuraPermanents().size()+')';
	}
}
