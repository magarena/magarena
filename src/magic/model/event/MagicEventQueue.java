package magic.model.event;

import java.util.LinkedList;

import magic.model.MagicCopyMap;

public class MagicEventQueue extends LinkedList<MagicEvent> {
	
	private static final long serialVersionUID = 1L;

	public MagicEventQueue() {
		
	}
	
	public MagicEventQueue(final MagicCopyMap copyMap,final MagicEventQueue source) {
		
		for (final MagicEvent event : source) {
			
			add(copyMap.copy(event));
		}
	}	
}