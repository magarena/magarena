package magic.ui.dialog.prefs;

import java.awt.Component;
import java.awt.event.MouseListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.translate.MText;
import magic.ui.mwidgets.MCheckBox;
import magic.ui.widget.duel.animation.AnimationFx;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class AnimationFlagsPanel extends JPanel {

    private final Map<Long, MCheckBox> cbMap = new LinkedHashMap<>();
    private final MouseListener listener;

    private static final String _S1 = "Draw card from library";
    private static final String _S2 = "The card image grows from your library button to full size preview where it waits for a number of seconds (see <b>preview</b> sliders) before shrinking back down to the hand button.";
    private static final String _S3 = "Play card from hand";
    private static final String _S4 = "AI only. The card image grows from the AI's hand button to full size preview where it waits for a number of seconds (see <b>preview</b> sliders) before shrinking back down to the stack or battlefield.";
    private static final String _S5 = "Flip card on draw/play";
    private static final String _S6 = "The card is played or drawn face down and is turned over for previewing.";
    private static final String _S7 = "Card shadow";
    private static final String _S8 = "Displays a very simple card shadow effect when card image is previewed during the play/draw animations.";
    private static final String _S9 = "Static play-from arrow";
    private static final String _S10 = "Indicates which player zone a card originates from during the preview stage of the draw/play animations.";
    private static final String _S11 = "Pulse zone button icon";
    private static final String _S16 = "The icon in a player's zone button pulses a few times when an event of note takes place in that zone.";
    private static final String _S12 = "Avatar selection pulse";
    private static final String _S17 = "When a player is a valid choice then that player's avatar border pulses on and off.";
    private static final String _S13 = "New turn message";
    private static final String _S18 = "Displays a message at the beginning of each new turn and pauses game for a number of seconds (see <b>New turn delay</b> slider).";
    private static final String _S14 = "Card popup fade-in";
    private static final String _S15 = "Uses a fade-in effect when displaying the full-size card image during a game.";
    private static final String _S20 = "Elastic play-from arrow";
    private static final String _S21 = "Indicates which player zone a card originates from using an arrow that grows and follows the card animation to the preview stage.";

    AnimationFlagsPanel(final MouseListener aListener) {

        this.listener = aListener;

        createCheckBox(AnimationFx.DRAW_CARD, MText.asHtml(_S1), MText.get(_S2));
        createCheckBox(AnimationFx.PLAY_CARD, MText.asHtml(_S3), MText.get(_S4));
        createCheckBox(AnimationFx.FLIP_CARD, MText.asHtml(_S5), MText.get(_S6));
        createCheckBox(AnimationFx.CARD_SHADOW, MText.asHtml(_S7), MText.get(_S8));
        createCheckBox(AnimationFx.STATIC_ARROW, MText.asHtml(_S9), MText.get(_S10));
        createCheckBox(AnimationFx.ELASTIC_ARROW, MText.asHtml(_S20), MText.get(_S21));
        createCheckBox(AnimationFx.ZBUTTON_PULSE, MText.asHtml(_S11), MText.get(_S16));
        createCheckBox(AnimationFx.AVATAR_PULSE, MText.asHtml(_S12), MText.get(_S17));
        createCheckBox(AnimationFx.NEW_TURN_MSG, MText.asHtml(_S13), MText.get(_S18));
        createCheckBox(AnimationFx.CARD_FADEIN, MText.asHtml(_S14), MText.get(_S15));

        setLayout(new MigLayout("flowx, wrap 2, insets 0, gapy 6, gapx 10"));
        for (MCheckBox cb : cbMap.values()) {
            add(cb.component(), "aligny top");
        }
    }

    private void createCheckBox(long flag, String caption, String tooltip) {
        final MCheckBox cb = new MCheckBox(caption, AnimationFx.isOn(flag));
        cb.setToolTipText(tooltip);
        cb.addMouseListener(listener);
        cb.setVerticalTextPosition(SwingConstants.TOP);
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
