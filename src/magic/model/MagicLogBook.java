package magic.model;

import java.util.ArrayList;

public class MagicLogBook extends ArrayList<MagicMessage> {

	private static final long serialVersionUID = 1L;

	public MagicLogBook() {
		
	}

    @Override
    public boolean add(MagicMessage msg) {
        if (System.getProperty("debug") != null) {
            System.err.println(msg.getText());
        }
        return super.add(msg);
    }
	
	/** Removes all messages from end to given index, inclusive. */
	public void removeTo(final int toIndex) {
		
		for (int index=size()-1;index>=toIndex;index--) {

			remove(index);
		}
	}
}
