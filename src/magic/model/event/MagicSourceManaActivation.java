package magic.model.event;

import magic.model.MagicCostManaType;
import magic.model.MagicGame;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;

/** Each source can only be activated once for mana. Each mana type must come from at most one activation. */
public class MagicSourceManaActivation {
	
	public final MagicPermanent permanent;
	public final MagicManaActivation activations[];
	public boolean available;
	public MagicManaType manaType=null;
	
	public MagicSourceManaActivation(final MagicGame game,final MagicPermanent permanent) {
		
		this.permanent=permanent;
		activations=new MagicManaActivation[MagicManaType.NR_OF_TYPES];
		available=false;
		
		for (final MagicManaActivation activation: permanent.getCardDefinition().getManaActivations()) {

			if (activation.canPlay(game,permanent)) {
				available=true;
				for (final MagicManaType manaType : activation.getManaTypes()) {
					
					activations[manaType.ordinal()]=activation;
				}
			}
		}
	}
	
	public MagicManaType canProduce(final MagicCostManaType costManaType) {

		if (available) {
			for (final MagicManaType manaType : costManaType.getTypes()) {
			
				if (activations[manaType.ordinal()]!=null) {
					return manaType;
				}
			}
		}		
		return null;
	}
	
	public void produce(final MagicGame game,final MagicCostManaType costManaType) {

		MagicManaActivation bestManaActivation=null;				
		for (final MagicManaType manaType : costManaType.getTypes()) {
			
			final MagicManaActivation manaActivation=activations[manaType.ordinal()];
			if (manaActivation!=null&&(bestManaActivation==null||bestManaActivation.getWeight()>manaActivation.getWeight())) {
				bestManaActivation=manaActivation;
			}
		}

		if (bestManaActivation==null) {
			throw new IllegalStateException("This mana source cannot produce "+costManaType.getText()+".");
		}
		final MagicSourceManaActivationResult bestSourceManaActivation=new MagicSourceManaActivationResult(permanent,bestManaActivation);
		bestSourceManaActivation.doActivation(game);
	}
			
	public int getWeight() {
		
		return activations[manaType.ordinal()].getWeight();
	}
	
	public MagicSourceManaActivationResult getResult() {
		
		return new MagicSourceManaActivationResult(permanent,activations[manaType.ordinal()]);
	}
}
