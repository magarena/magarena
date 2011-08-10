package magic.model.stack;

import magic.data.IconImages;
import magic.model.MagicCopyable;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.event.MagicPermanentActivation;

import javax.swing.*;

public class MagicAbilityOnStack extends MagicItemOnStack {
		
	public MagicAbilityOnStack(final MagicPermanentActivation activation,final MagicPermanent permanent,final MagicPayedCost payedCost) {

		setSource(permanent);
		setController(permanent.getController());
		setActivation(activation);
		setEvent(activation.getPermanentEvent(permanent,payedCost));
	}

	private MagicAbilityOnStack() {
		
	}
	
	@Override
	public MagicCopyable create() {
		
		return new MagicAbilityOnStack();
	}

	@Override
	public boolean isSpell() {

		return false;
	}

	@Override
	public boolean canBeCountered() {

		return true;
	}
		
	@Override
	public ImageIcon getIcon() {
		
		return IconImages.ABILITY;
	}	
}