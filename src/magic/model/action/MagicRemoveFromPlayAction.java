package magic.model.action;

import java.util.ArrayList;
import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.trigger.MagicTriggerType;

public class MagicRemoveFromPlayAction extends MagicAction {

	private final MagicPermanent permanent;
	private final MagicLocationType toLocation;
	private Collection<MagicPermanentTrigger> removedTriggers;
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
		}
		
		// Equipment
		if (permanent.getEquippedCreature()!=null) {
			permanent.getEquippedCreature().removeEquipment(permanent);
		}
		for (final MagicPermanent equipment : permanent.getEquipmentPermanents()) {
			
			equipment.setEquippedCreature(null);
		}

		// Aura
		if (permanent.getEnchantedCreature()!=null) {
			permanent.getEnchantedCreature().removeAura(permanent);
		}
		for (final MagicPermanent aura : permanent.getAuraPermanents()) {
		
			aura.setEnchantedCreature(null);
		}

		game.doAction(new MagicRemoveFromCombatAction(permanent));

		controller.removePermanent(permanent);

		setScore(controller,permanent.getStaticScore(game)-score);
		
		removedTriggers=new ArrayList<MagicPermanentTrigger>();
		game.removeTriggers(permanent,removedTriggers);
		
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
		if (permanent.getEquippedCreature()!=null) {
			permanent.getEquippedCreature().addEquipment(permanent);
		}
		for (final MagicPermanent equipment : permanent.getEquipmentPermanents()) {
		
			equipment.setEquippedCreature(permanent);
		}
		
		// Aura
		if (permanent.getEnchantedCreature()!=null) {
			permanent.getEnchantedCreature().addAura(permanent);
		}
		for (final MagicPermanent aura : permanent.getAuraPermanents()) {
		
			aura.setEnchantedCreature(permanent);
		}
		
		for (final MagicPermanentTrigger permanentTrigger : removedTriggers) {
			
			game.addTrigger(permanentTrigger);
		}
	}

	@Override
	public String toString() {

		return getClass().getSimpleName()+" ("+permanent.getName()+','+permanent.getAuraPermanents().size()+')';
	}
}