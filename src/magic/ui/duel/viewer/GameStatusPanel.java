package magic.ui.duel.viewer;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import magic.utility.MagicSystem;
import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.model.MagicGame;
import magic.ui.SwingGameController;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.theme.Theme;
import magic.ui.widget.TexturedPanel;
import magic.ui.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class GameStatusPanel extends TexturedPanel implements ChangeListener {

    private final MigLayout migLayout = new MigLayout("insets 0, gap 0");
    private final UserActionPanel userActionPanel;
    private final SwingGameController controller;
    private boolean isNewTurnNotification = false;
    private final NewTurnPanel newTurnPanel;
    private final TurnStatusPanel turnStatusPanel;


    public GameStatusPanel(final SwingGameController controller) {

        this.controller = controller;

        // create UI components
        userActionPanel = new UserActionPanel(controller);
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
        userActionPanel.setOpaque(false);
    }

    public UserActionPanel getUserActionPanel() {
        return userActionPanel;
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
            add(userActionPanel, "w 100%, h 100%");
        }

        public void refresh(final MagicGame game) {
            turnTitlePanel.refresh(game);
            phaseStepViewer.setPhaseStep(userActionPanel.getMagicPhaseType());
        }

    }

    private class TurnTitlePanel extends JPanel {

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
            setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
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
            JButton btn = new JButton(IconImages.getIcon(MagicIcon.OPTIONS_ICON));
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
            if (MagicSystem.isDevMode()) {
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e)) {
                            showDevPopupMenu(e);
                        } else {
                            super.mouseReleased(e);
                        }
                    }
                });
            }
            return btn;
        }

        private void showOptionsMenu() {
            Container parent = getParent();
            while (parent != null) {
                if (parent instanceof IOptionsMenu) {
                    break;
                }
                parent = parent.getParent();
            }
            if (parent != null) {
                ((IOptionsMenu)parent).showOptionsMenuOverlay();
            }
        }

        public void refresh(final MagicGame game) {
            playerAvatar.setIcon(userActionPanel.getTurnSizedPlayerAvatar());
            playerLabel.setText(game.getPriorityPlayer().getName() + " has priority");
            turnLabel.setText(userActionPanel.getTurnCaption());
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

        private void showDevPopupMenu(final MouseEvent e) {
            final JPopupMenu menu = new JPopupMenu();
            final JMenuItem item1 = new JMenuItem(new AbstractAction("Save Game") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.doSaveGame();
                }
            });
            menu.add(item1);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }

    }

}
