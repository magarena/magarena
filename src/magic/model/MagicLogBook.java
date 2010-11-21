package magic.model;

import java.util.ArrayList;

public class MagicLogBook extends ArrayList<MagicMessage> {

	private static final long serialVersionUID = 1L;

	public MagicLogBook() {
		
	}
	
	/** Removes all messages from end to given index, inclusive. */
	public void removeTo(final int toIndex) {
		
		for (int index=size()-1;index>=toIndex;index--) {

			remove(index);
		}
	}
}