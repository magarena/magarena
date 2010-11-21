package magic.model.trigger;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import magic.model.MagicCopyMap;

public class MagicPermanentTriggerMap extends HashMap<MagicTriggerType,SortedSet<MagicPermanentTrigger>> {

	private static final long serialVersionUID = 1L;

	public MagicPermanentTriggerMap() {
		
		for (final MagicTriggerType type : MagicTriggerType.values()) {
			
			put(type,new TreeSet<MagicPermanentTrigger>());
		}
	}
	
	public MagicPermanentTriggerMap(final MagicCopyMap copyMap,final MagicPermanentTriggerMap source) {
		
		for (final MagicTriggerType type : source.keySet()) {
			
			final SortedSet<MagicPermanentTrigger> triggers=new TreeSet<MagicPermanentTrigger>();
			put(type,triggers);
			
			for (final MagicPermanentTrigger permanentTrigger : source.get(type)) {
				
				triggers.add(new MagicPermanentTrigger(copyMap,permanentTrigger));
			}
		}
	}	
}