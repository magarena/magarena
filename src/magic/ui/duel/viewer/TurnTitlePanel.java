/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import magic.data.MagicIcon;
import magic.model.MagicGame;
import magic.ui.IconImages;
import magic.ui.MagicStyle;
import magic.ui.SwingGameController;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.theme.Theme;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TurnTitlePanel extends JPanel {

    private final MigLayout miglayout = new MigLayout("insets 0, gap 0");
    private final JLabel playerLabel = new JLabel();
    private final JLabel playerAvatar = new JLabel();
    private final JLabel turnLabel = new JLabel();
    private final JLabel gameLabel = new JLabel();
    private final UserActionPanel userActionPanel;
    private final SwingGameController controller;

    public TurnTitlePanel(final UserActionPanel userActionPanel, final SwingGameController controller) {
        this.userActionPanel = userActionPanel;
        this.controller = controller;
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
            ((IOptionsMenu) parent).showOptionsMenuOverlay();
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
