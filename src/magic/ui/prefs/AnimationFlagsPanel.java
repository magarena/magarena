package magic.ui.prefs;

import java.awt.Component;
import java.awt.event.MouseListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import magic.ui.duel.animation.AnimationFx;
import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
class AnimationFlagsPanel extends JPanel {

    private final Map<Long, JCheckBox> cbMap = new LinkedHashMap<>();
    private final MouseListener listener;

    AnimationFlagsPanel(final MouseListener aListener) {

        this.listener = aListener;

        createCheckBox(AnimationFx.DRAW_CARD, "Draw card from library.", "Shows you the card drawn from your library before being added to your hand.");
        createCheckBox(AnimationFx.PLAY_CARD, "Play card from hand.", "Shows you the card played by the AI before it is added to the stack or battlefield.");
        createCheckBox(AnimationFx.FLIP_CARD, "Flip card on draw/play.", "The card is played or drawn face down and flips to the front face for previewing.");
        createCheckBox(AnimationFx.CARD_SHADOW, "Card shadow.", "Displays card shadow when previewed.");
        createCheckBox(AnimationFx.FROM_ARROW, "Play-from arrows.", "Indicates which player zone a card was played from.");
        createCheckBox(AnimationFx.FLASH_ZONE, "Flash player zone.", "");
        createCheckBox(AnimationFx.AVATAR_STROBE, "Avatar strobe.", "");
        createCheckBox(AnimationFx.NEW_TURN_MSG, "New turn message.", "");

        setLayout(new MigLayout("flowx, wrap 2, insets 0, gapy 6, gapx 10"));
        for (JCheckBox cb : cbMap.values()) {
            add(cb);
        }
    }

    private void createCheckBox(long flag, String caption, String tooltip) {
        final JCheckBox cb = new JCheckBox(caption, AnimationFx.isOn(flag));
        cb.setToolTipText(tooltip);
        cb.addMouseListener(listener);
        cbMap.put(flag, cb);
    }

    void saveSettings() {
        for (long flag : cbMap.keySet()) {
            AnimationFx.setFlag(flag, cbMap.get(flag).isSelected());
        }
    }

    @Override
    public void setEnabled(boolean b) {
        for (Component c : getComponents()) {
            c.setEnabled(b);
        }
        super.setEnabled(b);
    }

}
