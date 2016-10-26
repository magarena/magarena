package magic.ui.screen.duel.setup;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.data.DeckType;
import magic.model.MagicColor;
import magic.model.MagicDeckProfile;
import magic.translate.StringContext;
import magic.translate.MText;
import magic.ui.dialog.DeckChooserDialog;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.widget.TexturedPanel;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;


/**
 * Displays the deck type and name.
 *
 */
@SuppressWarnings("serial")
class DuelPlayerDeckPanel extends TexturedPanel implements IThemeStyle {

    // translatable strings
    @StringContext(eg = "'Prebuilt deck' or 'Random deck'")
    private static final String _S1 = "%s deck";
    private static final String _S2 = "Any three colors";
    private static final String _S3 = "Any two colors";
    private static final String _S4 = "Any single color";
    private static final String _S5 = "Preconstructed";

    // ui
    private final MigLayout migLayout = new MigLayout();
    private final JLabel deckTypeLabel = new JLabel();
    private final JLabel deckValueLabel = new JLabel();
    // properties
    private DeckType deckType = DeckType.Random;
    private String deckValue = MagicDeckProfile.ANY_THREE;

    DuelPlayerDeckPanel(final MagicDeckProfile deckProfile) {
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
        deckTypeLabel.setText(MText.get(_S1, deckType));
        deckValueLabel.setText(getFormattedDeckValue());
    }

    DeckType getDeckType() {
        return deckType;
    }

    private void setDeckValue(final String value) {
        deckValue = value;
        deckValueLabel.setText(getFormattedDeckValue());
    }

    String getDeckValue() {
        return deckValue;
    }

    private String getFormattedDeckValue() {
        if (deckType == DeckType.Random) {
            switch (deckValue) {
                case MagicDeckProfile.ANY_THREE:
                    return MText.get(_S2);
                case MagicDeckProfile.ANY_TWO:
                    return MText.get(_S3);
                case MagicDeckProfile.ANY_ONE:
                    return MText.get(_S4);
                case MagicDeckProfile.ANY_DECK:
                    return MText.get(_S5);
                default:
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
            colors += MagicColor.getColor(ch).getDisplayName() + ", ";
        }
        return colors.trim().substring(0, colors.trim().length() - 1);
    }

    private MouseAdapter getMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                MouseHelper.showBusyCursor();
                setDeckProfile();
                MouseHelper.showDefaultCursor();
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

    void setDeckProfile() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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

