package magic.model;

import magic.utility.MagicSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;

public class MagicLogBook implements Iterable<MagicMessage> {

    private List<MagicMessage> messages = new ArrayList<>();
    private List<ILogBookListener> _listeners = new ArrayList<>();

    MagicLogBook() {}

    public synchronized boolean add(final MagicMessage msg) {
        notifyMessageLogged(msg);
        final String player = msg.getPlayer().getIndex() == 0 ? "P" : "C";
        MagicGameLog.log("LOG (" + player + "): " + msg.getText());
        if (MagicSystem.isDebugMode()) {
            System.err.println("LOG: " + msg.getText());
        }
        return messages.add(msg);
    }

    /** Removes all messages from end to given index, inclusive. */
    public synchronized void removeTo(final int toIndex) {
        for (int index=size()-1;index>=toIndex;index--) {
            messages.remove(index);
        }
        notifyMessageLogged(null);
    }

    public int size() {
        return messages.size();
    }

    @Override
    public Iterator<MagicMessage> iterator() {
        return messages.iterator();
    }

    public ListIterator<MagicMessage> listIterator(int idx) {
        return messages.listIterator(idx);
    }

    public synchronized void addListener(ILogBookListener obj) {
        _listeners.add(obj);
    }

    public synchronized void removeListener(ILogBookListener obj) {
        _listeners.remove(obj);
    }

    private synchronized void notifyMessageLogged(final MagicMessage msg) {
        for (final ILogBookListener listener : _listeners) {
            listener.messageLogged(new MagicLogBookEvent(this, msg));
        }
    }
}
