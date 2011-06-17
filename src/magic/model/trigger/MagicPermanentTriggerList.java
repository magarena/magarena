package magic.model.trigger;

import java.util.ArrayList;
import java.util.Set;

public class MagicPermanentTriggerList extends ArrayList<MagicPermanentTrigger> {

	private static final long serialVersionUID = 1L;
	
	public MagicPermanentTriggerList() {}
	
	public MagicPermanentTriggerList(final MagicPermanentTriggerList triggerList) {
		super(triggerList);
	}
	
	public MagicPermanentTriggerList(final MagicPermanentTriggerMap triggerMap,final MagicPermanentTriggerList triggerList) {
		for (MagicPermanentTrigger permanentTrigger : triggerList) {
			final long id = permanentTrigger.getId();
			final Set<MagicPermanentTrigger> triggersSet = triggerMap.get(permanentTrigger.getTrigger().getType());
			for (final MagicPermanentTrigger otherPermanentTrigger : triggersSet) {
				if (otherPermanentTrigger.getId() == id) {
					add(otherPermanentTrigger);
					break;
				}
			}
		}
	}
}
