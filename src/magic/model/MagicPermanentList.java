package magic.model;

import java.util.Collection;
import java.util.ArrayList;

public class MagicPermanentList extends ArrayList<MagicPermanent> implements MagicCopyable {

    private static final long serialVersionUID = 1L;

    public MagicPermanentList() {}
    
    public MagicPermanentList(final Collection<MagicPermanent> list) {
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
}
