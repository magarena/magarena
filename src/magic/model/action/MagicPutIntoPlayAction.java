package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public abstract class MagicPutIntoPlayAction extends MagicAction {

	private MagicPermanent permanent;
	private MagicPermanent enchantedPermanent;

	@Override
	public void doAction(final MagicGame game) {
		permanent=createPermanent(game);
		final int score=ArtificialScoringSystem.getTurnScore(game)-permanent.getStaticScore(game);
		
		final MagicPlayer controller=permanent.getController();
		controller.addPermanent(permanent);
				
		if (enchantedPermanent!=null) {
			enchantedPermanent.addAura(permanent);
			permanent.setEnchantedCreature(enchantedPermanent);			
		}

		final MagicCardDefinition cardDefinition=permanent.getCardDefinition();
		for (final MagicTrigger trigger : cardDefinition.getTriggers()) {
			game.addTrigger(permanent,trigger);
		}
		
		for (final MagicTrigger trigger : cardDefinition.getComeIntoPlayTriggers()) {
			game.executeTrigger(trigger,permanent,permanent,null);
		}
		game.executeTrigger(MagicTriggerType.WhenOtherComesIntoPlay,permanent);
		
		setScore(controller,permanent.getScore(game)+permanent.getStaticScore(game)+score);
		
		game.checkLegendRule(permanent);
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {
		if (enchantedPermanent!=null) {			
			enchantedPermanent.removeAura(permanent);
			permanent.setEnchantedCreature(null);
		}
		permanent.getController().removePermanent(permanent);
		game.removeTriggers(permanent,null);
	}
	
	protected void setEnchantedPermanent(final MagicPermanent enchantedPermanent) {
		this.enchantedPermanent=enchantedPermanent;
	}
	
	protected abstract MagicPermanent createPermanent(final MagicGame game);
	
	public MagicPermanent getPermanent() {
		return permanent;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+" ("+permanent+','+enchantedPermanent+')';
	}
}
