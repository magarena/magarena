package magic.ui.viewer;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import magic.data.IconImages;
import magic.model.MagicGame;
import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class GameDuelViewer extends TexturedPanel implements ChangeListener {

    private final MigLayout migLayout = new MigLayout("insets 0, gap 0");
    private final GameViewer gameViewer;
    private final GameController controller;
    private boolean isNewTurnNotification = false;
    private final NewTurnPanel newTurnPanel;
    private final TurnStatusPanel turnStatusPanel;


    public GameDuelViewer(final GameController controller) {

        this.controller = controller;

        // create UI components
        gameViewer = new GameViewer(controller);
        newTurnPanel = new NewTurnPanel();
        turnStatusPanel = new TurnStatusPanel();

        setLookAndFeel();
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        setLayout(migLayout);
        add(isNewTurnNotification ? newTurnPanel : turnStatusPanel, "w 100%, h 100%");
        revalidate();
        repaint();
    }

    private void setLookAndFeel() {
        gameViewer.setOpaque(false);
    }

    public GameViewer getGameViewer() {
        return gameViewer;
    }

    public void update() {
        turnStatusPanel.refresh(controller.getGame());
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        update();
    }

    public void showNewTurnNotification(final MagicGame game) {
        assert SwingUtilities.isEventDispatchThread();
        isNewTurnNotification = true;
        newTurnPanel.refreshData(game);
        refreshLayout();
    }

    public void hideNewTurnNotification() {
        assert SwingUtilities.isEventDispatchThread();
        isNewTurnNotification = false;
        refreshLayout();
    }

    private class NewTurnPanel extends JPanel {

        private final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();
        private final MigLayout migLayout = new MigLayout("flowy, gapy 0");
        private final JLabel iconLabel = new JLabel();
        private final JLabel turnLabel = new JLabel();
        private final JLabel playerLabel = new JLabel();

        public NewTurnPanel() {
            setLookAndFeel();
            refreshLayout();
        }

        private void setLookAndFeel() {
            setBackground(THEME.getColor(Theme.COLOR_TITLE_BACKGROUND));
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            //
            turnLabel.setFont(turnLabel.getFont().deriveFont(16f));
            turnLabel.setForeground(Color.WHITE);
            turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
            //
            playerLabel.setFont(turnLabel.getFont().deriveFont(20f));
            playerLabel.setForeground(Color.WHITE);
            playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        private void refreshLayout() {
            setLayout(migLayout);
            add(iconLabel, "alignx center");
            add(playerLabel, "w 100%, h 100%");
            add(turnLabel, "w 100%, h 100%");
        }

        public void refreshData(final MagicGame game) {
            iconLabel.setIcon(game.getTurnPlayer().getPlayerDefinition().getAvatar().getIcon(3));
            turnLabel.setText("Turn " + game.getTurn());
            playerLabel.setText(game.getTurnPlayer().getName());
        }

    }

    private class TurnStatusPanel extends JPanel {

        private final MigLayout miglayout = new MigLayout("insets 0, gap 0, flowy");
        private final TurnTitlePanel turnTitlePanel = new TurnTitlePanel();
        private final PhaseStepViewer phaseStepViewer = new PhaseStepViewer();

        public TurnStatusPanel() {
            setLookAndFeel();
            setLayout(miglayout);
            refreshLayout();
        }

        private void setLookAndFeel() {
            setOpaque(false);
            //
            phaseStepViewer.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
            phaseStepViewer.setOpaque(false);
        }

        private void refreshLayout() {
            removeAll();
            add(turnTitlePanel, "w 100%");
            add(phaseStepViewer, "w 100%");
            add(gameViewer, "w 100%, h 100%");
        }

        public void refresh(final MagicGame game) {
            turnTitlePanel.refresh(game);
            phaseStepViewer.setPhaseStep(gameViewer.getMagicPhaseType());
        }

    }

    private class TurnTitlePanel extends JPanel {

        private final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();

        private final MigLayout miglayout = new MigLayout("insets 0, gap 0");
        private final JLabel playerLabel = new JLabel();
        private final JLabel playerAvatar = new JLabel();
        private final JLabel turnLabel = new JLabel();
        private final JLabel gameLabel = new JLabel();

        public TurnTitlePanel() {
            setLookAndFeel();
            setLayout(miglayout);
            refreshLayout();
        }

        private void setLookAndFeel() {
            setOpaque(true);
            setBackground(THEME.getColor(Theme.COLOR_TITLE_BACKGROUND));
            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
            //
            playerLabel.setForeground(Color.WHITE);
            turnLabel.setForeground(Color.WHITE);
            gameLabel.setForeground(Color.WHITE);
        }

        private void refreshLayout() {
            removeAll();
            add(playerAvatar, "w 54px!, h 54px!, cell 1 1 1 3, gapright 4");
            add(gameLabel, "w 100%, h 17px!, cell 2 1");
            add(playerLabel, "w 100%, h 18px!, cell 2 2");
            add(turnLabel, "w 100%, h 17px!, cell 2 3, top");
            add(getOptionsIconButton(), "w 32!, h 32!, cell 3 1 1 3, gapright 10");
        }

        private JButton getOptionsIconButton() {
            JButton btn = new JButton(IconImages.OPTIONS_ICON);
            btn.setHorizontalAlignment(SwingConstants.RIGHT);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setToolTipText("Options [ESC]");
            setButtonTransparent(btn);
            btn.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showOptionsMenu();
                }
            });
            return btn;
        }

        private void showOptionsMenu() {
            try {
                Robot robot;
                robot = new Robot();
                robot.keyPress(KeyEvent.VK_ESCAPE);
            } catch (AWTException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        public void refresh(final MagicGame game) {
            playerAvatar.setIcon(gameViewer.getTurnSizedPlayerAvatar());
            playerLabel.setText(game.getPriorityPlayer().getName() + " has priority");
            turnLabel.setText(gameViewer.getTurnCaption());
            gameLabel.setText(
                    "Game " + game.getDuel().getGameNr()
                    + " of " + game.getDuel().getConfiguration().getNrOfGames());
        }

        private void setButtonTransparent(final JButton btn) {
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setBorder(null);
        }

    }

}
