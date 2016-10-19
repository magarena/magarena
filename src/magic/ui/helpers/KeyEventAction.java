package magic.ui.helpers;

import java.awt.event.ActionEvent;
import java.util.UUID;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import magic.ui.screen.MScreen;

/**
 * Helper class that streamlines adding keyboard shortcuts
 * to a given component using a simple fluent-like interface.
 */
@SuppressWarnings("serial")
public final class KeyEventAction {

    private final JComponent com;
    private final Object key;

    private KeyEventAction(JComponent c, Runnable r) {
        com = c;
        key = UUID.randomUUID();
        c.getActionMap().put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r.run();
            }
        });
    }

    private KeyEventAction() {
        this.com = null;
        this.key = null;
    }

    /**
     * Assigns one or more key events that will trigger an action
     * only on the condition WHEN_IN_FOCUSED_WINDOW.
     *
     * @param keymod key modifier (see {@link java.awt.event.InputEvent}).
     * @param events one or more {@link java.awt.event.KeyEvent} values.
     *
     * @return {@link KeyEventAction}
     *
     * @see javax.swing.KeyStroke
     * @see java.awt.event.KeyEvent
     * @see java.awt.event.InputEvent
     */
    public KeyEventAction onFocus(int keymod, int... events) {
        for (int evt : events) {
            com.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke(evt, keymod), key);
        }
        return this;
    }

    /**
     * Assigns one or more key events that will trigger an action.
     *
     * @param keymod key modifier (see {@link java.awt.event.InputEvent}).
     * @param events one or more {@link java.awt.event.KeyEvent} values.
     *
     * @return {@link KeyEventAction}
     *
     * @see javax.swing.KeyStroke
     * @see java.awt.event.KeyEvent
     * @see java.awt.event.InputEvent
     */
    public KeyEventAction on(int keymod, int... events) {
        for (int evt : events) {
            com.getInputMap().put(KeyStroke.getKeyStroke(evt, keymod), key);
        }
        return this;
    }

    /**
     * Registers an action to run for a given component.
     * Key events that will trigger the action are added using the
     * {@code on} method of the returned {@link KeyEventAction} object.
     *
     * @param c component
     * @param r action to perform as a {@link Runnable}.
     * @return
     */
    public static KeyEventAction doAction(JComponent c, Runnable r) {
        return new KeyEventAction(c, r);
    }

    /**
     * Registers an action to run for a given {@link MScreen}.
     * Key events that will trigger the action are added using the
     * {@code on} method of the returned {@link KeyEventAction} object.
     *
     * @param s {@link MScreen}
     * @param r action to perform as a {@link Runnable}.
     * @return
     */
    public static KeyEventAction doAction(MScreen s, Runnable r) {
        return s.getKeyEventAction(r);
    }


}
