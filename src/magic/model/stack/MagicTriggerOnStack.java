package magic.model.stack;

import magic.data.IconImages;
import magic.model.MagicCopyable;
import magic.model.MagicCopyMap;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

import javax.swing.ImageIcon;

public class MagicTriggerOnStack extends MagicItemOnStack {
	public MagicTriggerOnStack(final MagicSource source,final MagicEvent event) {
		setSource(source);
		setController(source.getController());
		setEvent(event);
	}
	
    private MagicTriggerOnStack(final MagicCopyMap copyMap ,final MagicTriggerOnStack source) {
        super(copyMap, source);
	}
	
    @Override
	public MagicTriggerOnStack copy(final MagicCopyMap copyMap) {
        return new MagicTriggerOnStack(copyMap, this);
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
