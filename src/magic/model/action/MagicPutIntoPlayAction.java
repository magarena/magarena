package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicPermanentStatic;

import java.util.LinkedList;

public abstract class MagicPutIntoPlayAction extends MagicAction {

	private MagicPermanent permanent = MagicPermanent.NONE;
	private MagicPermanent enchantedPermanent = MagicPermanent.NONE;

	@Override
	public void doAction(final MagicGame game) {
		permanent=createPermanent(game);
		final int score=ArtificialScoringSystem.getTurnScore(game)-permanent.getStaticScore(game);
		
		final MagicPlayer controller=permanent.getController();
		controller.addPermanent(permanent);
				
		if (enchantedPermanent.isValid()) {
			enchantedPermanent.addAura(permanent);
			permanent.setEnchantedCreature(enchantedPermanent);			
		}

		final MagicCardDefinition cardDefinition=permanent.getCardDefinition();
		for (final MagicTrigger trigger : cardDefinition.getTriggers()) {
			game.addTrigger(permanent,trigger);
		}
		for (final MagicStatic mstatic : cardDefinition.getStatics()) {
			game.addStatic(permanent,mstatic);
		}
	
        //execute come into play triggers
		for (final MagicTrigger trigger : cardDefinition.getComeIntoPlayTriggers()) {
			game.executeTrigger(trigger,permanent,permanent,permanent.getController());
		}

        //execute other come into player triggers
		game.executeTrigger(MagicTriggerType.WhenOtherComesIntoPlay,permanent);
		
		setScore(controller,permanent.getScore(game)+permanent.getStaticScore(game)+score);
		
		game.checkLegendRule(permanent);
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {
		if (enchantedPermanent.isValid()) {			
			enchantedPermanent.removeAura(permanent);
			permanent.setEnchantedCreature(MagicPermanent.NONE);
		}
		permanent.getController().removePermanent(permanent);
		game.removeTriggers(permanent,new LinkedList<MagicPermanentTrigger>());
		game.removeStatics(permanent,new LinkedList<MagicPermanentStatic>());
	}
	
	void setEnchantedPermanent(final MagicPermanent enchantedPermanent) {
		this.enchantedPermanent=enchantedPermanent;
	}
	
	protected abstract MagicPermanent createPermanent(final MagicGame game);
	
	public MagicPermanent getPermanent() {
		return permanent;
	}
	
	@Override
	public String toString() {
        if (enchantedPermanent.isValid()) {
    		return getClass().getSimpleName()+" ("+permanent+','+enchantedPermanent+')';
        } else { 
    		return getClass().getSimpleName()+" ("+permanent+')';
        }
	}
}
