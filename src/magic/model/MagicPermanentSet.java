package magic.model;

import java.util.TreeSet;

public class MagicPermanentSet extends TreeSet<MagicPermanent> {

    private static final long serialVersionUID = 1L;

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
    
    long getPermanentsId() {
        int idx = 0;
        final long[] input = new long[size() + 1];
        for (final MagicPermanent permanent : this) {
            input[idx] = permanent.getPermanentId();
            idx++;
        }
        return magic.MurmurHash3.hash(input);
    }
}
