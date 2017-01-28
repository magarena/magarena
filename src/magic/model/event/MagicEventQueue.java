package magic.model.event;

import java.util.LinkedList;

import magic.model.MagicCopyMap;
import magic.model.MurmurHash3;

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
        return MurmurHash3.hash(keys);
    }
}
