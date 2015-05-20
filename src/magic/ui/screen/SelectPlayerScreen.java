package magic.ui.screen;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.ui.IconImages;
import magic.ui.ScreenController;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IAvatarImageConsumer;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.theme.Theme;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class SelectPlayerScreen
    extends AbstractScreen
    implements IAvatarImageConsumer, IActionBar {

    private final List<IPlayerProfileListener> listeners = new ArrayList<>();
    private final JList<? extends PlayerProfile> playersJList;

    protected HashMap<String, PlayerProfile> profilesMap = new HashMap<>();

    protected abstract void createDefaultPlayerProfiles() throws IOException;
    protected abstract int getPreferredWidth();
    protected abstract void refreshProfilesJList();
    protected abstract void refreshProfilesJList(final PlayerProfile playerProfile);
    protected abstract HashMap<String, PlayerProfile> getPlayerProfilesMap();
    protected abstract AbstractAction getNewPlayerAction();
    protected abstract AbstractAction getEditPlayerAction();

    // CTR
    protected SelectPlayerScreen(final JList<? extends PlayerProfile> playersJList) {
        this.playersJList = playersJList;
        this.playersJList.addMouseListener(new DoubleClickAdapter());
        setContent(new ScreenContent());
        setEnterKeyInputMap();
    }

    private void setEnterKeyInputMap() {
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "EnterAction");
        getActionMap().put("EnterAction", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                doNextAction();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    protected void setFocusInProfilesJList(final JList<? extends PlayerProfile> profilesJList) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                profilesJList.requestFocusInWindow();
            }
        });
    }

    protected void setSelectedListItem(final PlayerProfile playerProfile) {
        if (playerProfile == null) {
            playersJList.setSelectedIndex(0);
        } else {
            playersJList.setSelectedValue(profilesMap.get(playerProfile.getId()), true);
        }
        setFocusInProfilesJList(playersJList);
    }

    protected List<PlayerProfile> getSortedPlayersList() {
        profilesMap = getPlayerProfilesMap();
        final List<PlayerProfile> profilesByName = new ArrayList<>(profilesMap.values());
        Collections.sort(profilesByName, new Comparator<PlayerProfile>() {
            @Override
            public int compare(PlayerProfile o1, PlayerProfile o2) {
                return o1.getPlayerName().toLowerCase().compareTo(o2.getPlayerName().toLowerCase());
            }
        });
        return profilesByName;
    }

    protected void updateAvatarImage(final Path imagePath, final PlayerProfile playerProfile) {
        final Path targetPath = playerProfile.getProfilePath().resolve("player.avatar");
        try {
            Files.copy(imagePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        IconImages.getClearAvatarsCache();
    }

    @Override
    public void setSelectedAvatarPath(final Path imagePath) {
        final PlayerProfile profile = getSelectedPlayer();
        updateAvatarImage(imagePath, profile);
        refreshProfilesJList(profile);
        notifyPlayerUpdated(profile);
    }

    protected PlayerProfile getSelectedPlayer() {
        return playersJList.getSelectedValue();
    }

    protected class SelectAvatarActionButton extends ActionBarButton {
        public SelectAvatarActionButton() {
            super("Avatar", "Choose an avatar image for the selected player profile.",
                    new SelectAvatarAction());
        }
    }

    private class SelectAvatarAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            ScreenController.showAvatarImagesScreen(SelectPlayerScreen.this);
        }
    }

    protected class DeletePlayerAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteSelectedPlayer();
        }

        private void deleteSelectedPlayer() {
            final PlayerProfile condemnedPlayer = getSelectedPlayer();
            if (PlayerProfiles.canDeleteProfile(condemnedPlayer)) {
                if (isDeletePlayerConfirmedByUser(condemnedPlayer)) {
                    PlayerProfiles.deletePlayer(condemnedPlayer);
                    refreshProfilesJList();
                    notifyPlayerDeleted(condemnedPlayer);
                }
            } else {
                ScreenController.showWarningMessage("There must be at least one player defined.");
            }
        }

        private boolean isDeletePlayerConfirmedByUser(final PlayerProfile profile) {
            final int action = JOptionPane.showOptionDialog(
                    ScreenController.getMainFrame(),
                    "<html>This will delete the <b>" + profile.getPlayerName() + "</b> player profile.<br>" +
                    "All associated information such as player stats will also be removed.<br><br>" +
                    "<b>This action cannot be undone!</b></html>",
                    "Delete Player?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] {"Delete", "Cancel"}, "Cancel");
            return (action == JOptionPane.YES_OPTION);
        }

    }

    private class ScreenContent extends JPanel {
        public ScreenContent() {
            setOpaque(false);
            setLayout(new MigLayout("insets 2, center, center"));
            add(new ContainerPanel(playersJList), "w " + getPreferredWidth() + "!, h 80%");
        }
    }

    protected class ContainerPanel extends TexturedPanel implements IThemeStyle {

        public ContainerPanel(final JList<? extends PlayerProfile> profilesJList) {
            profilesJList.setOpaque(false);
            refreshStyle();
            setLayout(new MigLayout("insets 0, gap 0, flowy"));
            add(new ScrollPane(profilesJList), "w 100%, h 100%");
        }

        @Override
        public final void refreshStyle() {
            final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
            final Color thisBG = MagicStyle.getTranslucentColor(refBG, 200);
            setBackground(thisBG);
            setBorder(FontsAndBorders.BLACK_BORDER);
        }

        private class ScrollPane extends JScrollPane {
            public ScrollPane(final JList<? extends PlayerProfile> profilesJList) {
                setViewportView(profilesJList);
                setBorder(BorderFactory.createEmptyBorder());
                setOpaque(false);
                getViewport().setOpaque(false);
            }
        }

    }

    protected void doNextAction() {
        notifyPlayerSelected(getSelectedPlayer());
        ScreenController.closeActiveScreen(false);
    }

    protected class DoubleClickAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                doNextAction();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    public synchronized void addListener(IPlayerProfileListener obj) {
        listeners.add(obj);
    }

    public synchronized void removeListener(IPlayerProfileListener obj) {
        listeners.remove(obj);
    }

    protected synchronized void notifyPlayerUpdated(final PlayerProfile player) {
        for (final IPlayerProfileListener listener : listeners) {
            listener.PlayerProfileUpdated(player);
        }
    }

    private synchronized void notifyPlayerDeleted(final PlayerProfile player) {
        for (final IPlayerProfileListener listener : listeners) {
            listener.PlayerProfileDeleted(player);
        }
    }

    private synchronized void notifyPlayerSelected(final PlayerProfile player) {
        for (final IPlayerProfileListener listener : listeners) {
            listener.PlayerProfileSelected(player);
        }
    }

    protected JList<? extends PlayerProfile> getJList() {
        return playersJList;
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.interfaces.IActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton("Cancel");
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.interfaces.IActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        return new MenuButton("Select", new AbstractAction() {
          @Override
          public void actionPerformed(final ActionEvent e) {
              setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
              doNextAction();
              setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
          }
      });
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.interfaces.IActionBar#getMiddleActions()
     */
    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(
                new ActionBarButton(
                        "Settings", "Update player profile settings.",
                        getEditPlayerAction()));
        buttons.add(
                new ActionBarButton(
                        "New", "Create a new player profile.",
                        getNewPlayerAction()));
        buttons.add(
                new ActionBarButton(
                        "Delete", "Delete selected player profile (confirmation required).",
                        new DeletePlayerAction()));
        buttons.add(
                new SelectAvatarActionButton());
        return buttons;
    }

}
