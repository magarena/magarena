package magic.ui.prefs;

import java.awt.Component;
import java.awt.event.MouseListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import magic.translate.UiString;
import magic.ui.duel.animation.AnimationFx;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class AnimationFlagsPanel extends JPanel {

    private final Map<Long, JCheckBox> cbMap = new LinkedHashMap<>();
    private final MouseListener listener;

    private static final String _S1 = "Draw card from library.";
    private static final String _S2 = "Shows you the card drawn from your library before being added to your hand.";
    private static final String _S3 = "Play card from hand.";
    private static final String _S4 = "Shows you the card played by the AI before it is added to the stack or battlefield.";
    private static final String _S5 = "Flip card on draw/play.";
    private static final String _S6 = "The card is played or drawn face down and flips to the front face for previewing.";
    private static final String _S7 = "Card shadow.";
    private static final String _S8 = "Displays card shadow when previewed.";
    private static final String _S9 = "Play-from arrows.";
    private static final String _S10 = "Indicates which player zone a card was played from.";
    private static final String _S11 = "Flash player zone.";
    private static final String _S12 = "Avatar strobe.";
    private static final String _S13 = "New turn message.";

    AnimationFlagsPanel(final MouseListener aListener) {

        this.listener = aListener;

        createCheckBox(AnimationFx.DRAW_CARD, UiString.get(_S1), UiString.get(_S2));
        createCheckBox(AnimationFx.PLAY_CARD, UiString.get(_S3), UiString.get(_S4));
        createCheckBox(AnimationFx.FLIP_CARD, UiString.get(_S5), UiString.get(_S6));
        createCheckBox(AnimationFx.CARD_SHADOW, UiString.get(_S7), UiString.get(_S8));
        createCheckBox(AnimationFx.FROM_ARROW, UiString.get(_S9), UiString.get(_S10));
        createCheckBox(AnimationFx.FLASH_ZONE, UiString.get(_S11), "");
        createCheckBox(AnimationFx.AVATAR_STROBE, UiString.get(_S12), "");
        createCheckBox(AnimationFx.NEW_TURN_MSG, UiString.get(_S13), "");

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
