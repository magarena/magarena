package magic.model;

import java.util.ArrayList;

public class MagicLogBook extends ArrayList<MagicMessage> {

	private static final long serialVersionUID = 1L;

	MagicLogBook() {}

    @Override
    public boolean add(final MagicMessage msg) {
    	MagicGameLog.log("LOG: " + msg.getText());
        if (System.getProperty("debug") != null) {
            System.err.println("LOG: " + msg.getText());
        }
        return super.add(msg);
    }
	
	/** Removes all messages from end to given index, inclusive. */
	public synchronized void removeTo(final int toIndex) {
		for (int index=size()-1;index>=toIndex;index--) {
			remove(index);
		}
	}
}
