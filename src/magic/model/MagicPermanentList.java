package magic.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("serial")
public class MagicPermanentList extends ArrayList<MagicPermanent> implements MagicCopyable {

    public MagicPermanentList() {}

    public MagicPermanentList(final MagicPermanent... permanents) {
        addAll(Arrays.asList(permanents));
    }

    public MagicPermanentList(final List<MagicPermanent> list) {
        addAll(list);
    }

    MagicPermanentList(final MagicCopyMap copyMap,final MagicPermanentList list) {
        for (final MagicPermanent permanent : list) {
            add(copyMap.copy(permanent));
        }
    }

    @Override
    public MagicPermanentList copy(final MagicCopyMap copyMap) {
        return new MagicPermanentList(copyMap, this);
    }

    public long getStateId() {
        final long[] keys = new long[size()];
        int idx = 0;
        for (final MagicPermanent permanent : this) {
            keys[idx] = permanent.getStateId();
            idx++;
        }
        return MurmurHash3.hash(keys);
    }
}
