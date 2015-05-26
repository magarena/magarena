package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.data.CubeDefinitions;
import magic.data.DuelConfig;
import magic.data.MagicIcon;
import magic.model.MagicCubeDefinition;
import magic.ui.utility.GraphicsUtils;
import magic.ui.IconImages;
import magic.ui.MagicFrame;
import magic.ui.dialog.DuelPropertiesDialog;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DuelSettingsPanel extends TexturedPanel implements IThemeStyle {

    private final MagicFrame frame;
    private final DuelConfig config;
    private int startLife;
    private int handSize;
    private int maxGames = 7;
    private MagicCubeDefinition cube = CubeDefinitions.getDefaultCube();
    private final MouseAdapter mouseAdapter = getMouseAdapter();

    public DuelSettingsPanel(final MagicFrame frame, final DuelConfig config) {

        this.frame = frame;
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
            final StringBuilder sb = new StringBuilder();
            sb.append("<html><b>Duel Settings</b><br>");
            sb.append("Initial Player life: ").append(startLife).append("<br>");
            sb.append("Initial Hand size: ").append(handSize).append("<br>");
            sb.append("Maximum games: ").append(maxGames).append(" (first to ").append(getGamesRequiredToWinDuel()).append(")<br>");
            sb.append("Cube: ").append(cube.getLabel()).append("</html>");
            setToolTipText(sb.toString());
        }
    }

    private int getGamesRequiredToWinDuel() {
        return (int)Math.ceil(maxGames/2.0);
    }

    private void refreshDisplay() {
        removeAll();
        add(getDuelSettingsLabel(IconImages.getIcon(MagicIcon.LIFE_ICON), "" + startLife), "h 100%");
        add(getDuelSettingsLabel(IconImages.getIcon(MagicIcon.HAND_ICON), "" + handSize), "h 100%");
        add(getDuelSettingsLabel(IconImages.getIcon(MagicIcon.TARGET_ICON), "" + maxGames), "h 100%");
        add(getDuelSettingsLabel(IconImages.getIcon(MagicIcon.CUBE_ICON), " " + getCubeNameWithoutSize()), "h 100%");
        revalidate();
        repaint();
    }

    private MouseAdapter getMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                GraphicsUtils.setBusyMouseCursor(true);
                updateDuelSettings();
                GraphicsUtils.setBusyMouseCursor(false);
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
                frame,
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
        config.setCube(cube.getLabel());
    }

    public MagicCubeDefinition getCube() {
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

    private String getCubeNameWithoutSize() {
        return toTitleCase(cube.getName());
    }

    private String toTitleCase(final String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @Override
    public void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG);
        setBorder(FontsAndBorders.BLACK_BORDER);
    }

}
