package magic.model;

import java.util.TreeSet;

@SuppressWarnings("serial")
public class MagicPermanentSet extends TreeSet<MagicPermanent> {

    MagicPermanentSet() {}

    MagicPermanentSet(final MagicCopyMap copyMap,final MagicPermanentSet source) {
        for (final MagicPermanent permanent : source) {
            add(copyMap.copy(permanent));
        }
    }

    MagicPermanent getPermanent(final long id) {
        for (final MagicPermanent permanent : this) {
            if (permanent.getId() == id) {
                return permanent;
            }
        }
        return MagicPermanent.NONE;
    }

    long getStateId() {
        final long[] keys = new long[size()];
        int idx = 0;
        for (final MagicPermanent permanent : this) {
            keys[idx] = permanent.getStateId();
            idx++;
        }
        return MurmurHash3.hash(keys);
    }
}
