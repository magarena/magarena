package magic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import magic.utility.MagicSystem;

public class MagicLogBook {

    private List<MagicMessage> messages = new ArrayList<>();

    MagicLogBook() {}

    public boolean add(final MagicMessage msg) {
        final String player = msg.getPlayer().getIndex() == 0 ? "P" : "C";
        MagicGameLog.log("LOG (" + player + "): " + msg.getText());
        if (MagicSystem.isDebugMode()) {
            System.err.println("LOG: " + msg.getText());
        }
        return messages.add(msg);
    }

    /** Removes all messages from end to given index, inclusive. */
    public void removeTo(final int toIndex) {
        for (int index=size()-1;index>=toIndex;index--) {
            messages.remove(index);
        }
    }

    public int size() {
        return messages.size();
    }

    public ListIterator<MagicMessage> listIterator(int idx) {
        return messages.listIterator(idx);
    }
}
