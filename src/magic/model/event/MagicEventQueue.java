package magic.model.event;

import magic.model.MagicCopyMap;

import java.util.LinkedList;

@SuppressWarnings("serial")
public class MagicEventQueue extends LinkedList<MagicEvent> {

    public MagicEventQueue() {}

    public MagicEventQueue(final MagicCopyMap copyMap,final MagicEventQueue source) {
        for (final MagicEvent event : source) {
            add(copyMap.copy(event));
        }
    }

    public long getStateId() {
        final long[] keys = new long[size()];
        int idx = 0;
        for (final MagicEvent event : this) {
            keys[idx] = event.getStateId();
            idx++;
        }
        return magic.model.MurmurHash3.hash(keys);
    }
}
