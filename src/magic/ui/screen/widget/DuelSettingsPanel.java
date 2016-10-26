package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.data.DuelConfig;
import magic.data.MagicIcon;
import magic.data.MagicFormat;
import magic.ui.MagicImages;
import magic.translate.MText;
import magic.ui.dialog.DuelPropertiesDialog;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DuelSettingsPanel extends TexturedPanel implements IThemeStyle {

    // translatable strings
    private static final String _S1 = "Duel Settings";
    private static final String _S2 = "Initial Player life: %d";
    private static final String _S3 = "Initial Hand size: %d";
    private static final String _S4 = "Maximum games: %d (first to %d)";
    private static final String _S5 = "Cube: %s";

    private final DuelConfig config;
    private int startLife;
    private int handSize;
    private int maxGames = 7;
    private MagicFormat cube = MagicFormat.ALL;
    private final MouseAdapter mouseAdapter = getMouseAdapter();

    public DuelSettingsPanel(final DuelConfig config) {

        this.config = config;

        startLife = config.getStartLife();
        handSize = config.getHandSize();
        maxGames = config.getNrOfGames();
        cube = config.getCube();

        refreshStyle();

        addMouseListener(mouseAdapter);

        setLayout(new MigLayout("insets 0 5 0 0, gap 30, center"));
        refreshDisplay();

    }

    @Override
    public void setEnabled(boolean enabled) {
        removeMouseListener(mouseAdapter);
        if (enabled) {
            addMouseListener(mouseAdapter);
        } else {
            setBorder(null);
            setBackground(FontsAndBorders.TEXTAREA_TRANSPARENT_COLOR_HACK);
            setToolTipText(String.format("<html><b>%s</b><br>%s<br>%s<br>%s<br>%s</html>",
                    MText.get(_S1),
                    MText.get(_S2, startLife),
                    MText.get(_S3, handSize),
                    MText.get(_S4, maxGames, getGamesRequiredToWinDuel()),
                    MText.get(_S5, cube.getLabel())
            ));
        }
    }

    private int getGamesRequiredToWinDuel() {
        return (int)Math.ceil(maxGames/2.0);
    }

    private void refreshDisplay() {
        removeAll();
        add(getDuelSettingsLabel(MagicImages.getIcon(MagicIcon.LIFE), "" + startLife), "h 100%");
        add(getDuelSettingsLabel(MagicImages.getIcon(MagicIcon.HAND_ICON), "" + handSize), "h 100%");
        add(getDuelSettingsLabel(MagicImages.getIcon(MagicIcon.TARGET_ICON), "" + maxGames), "h 100%");
        add(getDuelSettingsLabel(MagicImages.getIcon(MagicIcon.CUBE), " " + cube.getName()), "h 100%");
        revalidate();
        repaint();
    }

    private MouseAdapter getMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                MouseHelper.showBusyCursor();
                updateDuelSettings();
                MouseHelper.showDefaultCursor();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                MagicStyle.setHightlight(DuelSettingsPanel.this, true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                MagicStyle.setHightlight(DuelSettingsPanel.this, false);
            }
        };
    }

    private JLabel getDuelSettingsLabel(final ImageIcon icon, final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setIcon(icon);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Dialog", Font.PLAIN, 18));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    public void updateDuelSettings() {
        final DuelPropertiesDialog dialog = new DuelPropertiesDialog(
                handSize,
                startLife,
                maxGames,
                cube
        );
        if (!dialog.isCancelled()) {
            startLife = dialog.getStartLife();
            handSize = dialog.getHandSize();
            maxGames = dialog.getNrOfGames();
            cube = dialog.getCube();
            saveSettings();
            refreshDisplay();
        }
    }

    private void saveSettings() {
        config.setStartLife(startLife);
        config.setHandSize(handSize);
        config.setNrOfGames(maxGames);
        config.setCube(cube);
    }

    public MagicFormat getCube() {
        return cube;
    }

    public int getStartLife() {
        return startLife;
    }

    public int getHandSize() {
        return handSize;
    }

    public int getNrOfGames() {
        return maxGames;
    }

    @Override
    public void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG);
        setBorder(FontsAndBorders.BLACK_BORDER);
    }

}
