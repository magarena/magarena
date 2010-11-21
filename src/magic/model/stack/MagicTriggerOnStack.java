package magic.model.stack;

import javax.swing.ImageIcon;

import magic.data.IconImages;
import magic.model.MagicCopyable;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

public class MagicTriggerOnStack extends MagicItemOnStack {

	public MagicTriggerOnStack(final MagicSource source,final MagicEvent event) {

		setSource(source);
		setController(source.getController());
		setEvent(event);
	}

	private MagicTriggerOnStack() {
		
	}
	
	@Override
	public MagicCopyable create() {
		
		return new MagicTriggerOnStack();
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
		
		return IconImages.TRIGGER;
	}	
}