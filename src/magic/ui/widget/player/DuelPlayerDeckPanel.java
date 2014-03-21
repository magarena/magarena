package magic.ui.widget.player;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import magic.MagicUtility;
import magic.data.DeckType;
import magic.model.MagicColor;
import magic.model.MagicDeckProfile;
import magic.ui.MagicFrame;
import magic.ui.dialog.DeckChooserDialog;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class DuelPlayerDeckPanel extends TexturedPanel {

    private final MouseAdapter mouseAdapter = getMouseAdapter();
    private DeckType deckType = DeckType.Random;
    private String deckValue = MagicDeckProfile.ANY_THREE;
    private MagicFrame frame;

    public DuelPlayerDeckPanel(final MagicFrame frame, final MagicDeckProfile deckProfile) {
        this.frame = frame;
        this.deckType = deckProfile.getDeckType();
        this.deckValue = deckProfile.getDeckValue();
        setBorder(FontsAndBorders.BLACK_BORDER);
        setBackground(FontsAndBorders.MAGSCREEN_BAR_COLOR);
        setOpaque(false);
        addMouseListener(mouseAdapter);
        doMigLayout();
    }

    public DeckType getDeckType() {
        return deckType;
    }

    public String getDeckValue() {
        return deckValue;
    }

    private void doMigLayout() {
        removeAll();
        setLayout(new MigLayout("flowy, insets 6 0 0 0, gap 4"));
        add(getLabel(getFormattedDeckValue(), 16, Font.PLAIN), "w 100%");
        add(getLabel(deckType + " deck", 14, Font.ITALIC), "w 100%");
    }

    private String getFormattedDeckValue() {
        if (deckType == DeckType.Random) {
            if (deckValue.equals(MagicDeckProfile.ANY_THREE)) {
                return "Any three colors";
            } else if (deckValue.equals(MagicDeckProfile.ANY_TWO)) {
                return "Any two colors";
            } else if (deckValue.equals(MagicDeckProfile.ANY_ONE)) {
                return "Any single color";
            } else if (deckValue.equals(MagicDeckProfile.ANY_DECK)) {
                return "Preconstructed";
            } else {
                if (deckValue.length() <= 3) {
                    return getVerboseColors(deckValue);
                } else {
                    // random theme deck.
                    return deckValue;
                }
            }
        } else {
            return deckValue;
        }
    }

    private String getVerboseColors(final String colorCodes) {
        String colors = "";
        for (char ch: colorCodes.toCharArray()) {
            colors += MagicColor.getColor(ch).name() + ", ";
        }
        return colors.trim().substring(0, colors.trim().length() - 1);
    }

    private JLabel getLabel(final String text, final int fontSize, final int fontStyle) {
        final JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Dialog", fontStyle, fontSize));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private MouseAdapter getMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                MagicUtility.setBusyMouseCursor(true);
                setDeckProfile();
                MagicUtility.setBusyMouseCursor(false);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                setBackground(FontsAndBorders.MENUPANEL_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
                setBackground(FontsAndBorders.MAGSCREEN_BAR_COLOR);
            }
        };
    }

    public void setDeckProfile() {
        final DeckChooserDialog dialog = new DeckChooserDialog(frame);
        if (!dialog.isCancelled()) {
            deckType = dialog.getDeckType();
            deckValue = dialog.getDeckValue();
            doMigLayout();
            revalidate();
            repaint();
        }
    }

}

