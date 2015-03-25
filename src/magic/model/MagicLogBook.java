package magic.model;

import magic.utility.MagicSystem;

import java.util.ArrayList;
import java.util.List;

public class MagicLogBook extends ArrayList<MagicMessage> {

    private static final long serialVersionUID = 1L;

    private List<ILogBookListener> _listeners = new ArrayList<>();

    MagicLogBook() {}

    @Override
    public boolean add(final MagicMessage msg) {
        notifyMessageLogged(msg);
        final String player = msg.getPlayer().getIndex() == 0 ? "P" : "C";
        MagicGameLog.log("LOG (" + player + "): " + msg.getText());
        if (MagicSystem.isDebugMode()) {
            System.err.println("LOG: " + msg.getText());
        }
        return super.add(msg);
    }

    /** Removes all messages from end to given index, inclusive. */
    public synchronized void removeTo(final int toIndex) {
        for (int index=size()-1;index>=toIndex;index--) {
            remove(index);
        }
        notifyMessageLogged(null);
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
