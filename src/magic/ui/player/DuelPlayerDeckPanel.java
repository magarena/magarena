package magic.ui.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.data.DeckType;
import magic.model.MagicColor;
import magic.model.MagicDeckProfile;
import magic.ui.GraphicsUtilities;
import magic.ui.MagicFrame;
import magic.ui.dialog.DeckChooserDialog;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.widget.TexturedPanel;
import magic.ui.MagicStyle;
import net.miginfocom.swing.MigLayout;


/**
 * Displays the deck type and name.
 *
 */
@SuppressWarnings("serial")
public class DuelPlayerDeckPanel extends TexturedPanel implements IThemeStyle {

    // ui
    private final MigLayout migLayout = new MigLayout();
    private final MagicFrame frame;
    private final JLabel deckTypeLabel = new JLabel();
    private final JLabel deckValueLabel = new JLabel();
    // properties
    private DeckType deckType = DeckType.Random;
    private String deckValue = MagicDeckProfile.ANY_THREE;

    public DuelPlayerDeckPanel(final MagicFrame frame, final MagicDeckProfile deckProfile) {
        this.frame = frame;
        setDeckType(deckProfile.getDeckType());
        setDeckValue(deckProfile.getDeckValue());
        addMouseListener(getMouseAdapter());
        setLookAndFeel();
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(false);
        refreshStyle();
        setLayout(migLayout);
        // deck type label
        deckTypeLabel.setForeground(Color.WHITE);
        deckTypeLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
        deckTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // deck value label
        deckValueLabel.setForeground(Color.WHITE);
        deckValueLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        deckValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 6 0 0 0, gap 4");
        add(deckValueLabel, "w 100%");
        add(deckTypeLabel, "w 100%");
    }


    private void setDeckType(final DeckType value) {
        deckType = value;
        deckTypeLabel.setText(deckType + " deck");
        deckValueLabel.setText(getFormattedDeckValue());
    }
    public DeckType getDeckType() {
        return deckType;
    }

    private void setDeckValue(final String value) {
        deckValue = value;
        deckValueLabel.setText(getFormattedDeckValue());
    }
    public String getDeckValue() {
        return deckValue;
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

    private MouseAdapter getMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                GraphicsUtilities.setBusyMouseCursor(true);
                setDeckProfile();
                GraphicsUtilities.setBusyMouseCursor(false);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                MagicStyle.setHightlight(DuelPlayerDeckPanel.this, true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                MagicStyle.setHightlight(DuelPlayerDeckPanel.this, false);
            }
        };
    }

    public void setDeckProfile() {
        final DeckChooserDialog dialog = new DeckChooserDialog(deckType, deckValue);
        if (!dialog.isCancelled()) {
            setDeckType(dialog.getDeckType());
            setDeckValue(dialog.getDeckName());
            refreshLayout();
        }
    }

    @Override
    public void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG);
        setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    }

}

