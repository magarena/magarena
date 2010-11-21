package magic.model;

import java.util.ArrayList;

public class MagicPermanentList extends ArrayList<MagicPermanent> {

	private static final long serialVersionUID = 1L;

	public MagicPermanentList() {
		
	}
	
	public MagicPermanentList(final MagicPermanentList list) {
		
		addAll(list);
	}
	
	public MagicPermanentList(final MagicCopyMap copyMap,final MagicPermanentList list) {

		for (final MagicPermanent permanent : list) {
			
			add(copyMap.copy(permanent));
		}
	}
}