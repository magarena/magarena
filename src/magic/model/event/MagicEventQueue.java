package magic.model.event;

import magic.model.MagicCopyMap;

import java.util.LinkedList;

public class MagicEventQueue extends LinkedList<MagicEvent> {
	
	private static final long serialVersionUID = 1L;

	public MagicEventQueue() {}
	
	public MagicEventQueue(final MagicCopyMap copyMap,final MagicEventQueue source) {
		for (final MagicEvent event : source) {
			add(copyMap.copy(event));
		}
	}	

    public long getEventsId() {
        int idx = 0;
        long[] input = new long[size() + 1];
        for (MagicEvent event : this) {
            input[idx] = event.getEventId();
            idx++;
        }
        return magic.MurmurHash3.hash(input);
    }
}
