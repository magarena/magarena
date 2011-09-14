package magic.model.mstatic;

import magic.model.MagicCopyMap;

import java.util.TreeSet;
import java.util.Set;

public class MagicPermanentStaticList extends TreeSet<MagicPermanentStatic> {

	private static final long serialVersionUID = 1L;
	
	public MagicPermanentStaticList() {}
	
	public MagicPermanentStaticList(final MagicPermanentStaticList triggerList) {
		super(triggerList);
	}
	
	public MagicPermanentStaticList(final MagicCopyMap copyMap, final MagicPermanentStaticList sourceList) {
        for (MagicPermanentStatic mpstatic : sourceList) {
            add(new MagicPermanentStatic(copyMap, mpstatic));
        }
    }
}
