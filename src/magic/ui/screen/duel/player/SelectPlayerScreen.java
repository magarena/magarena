package magic.ui.screen.duel.player;

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
import javax.swing.SwingUtilities;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.helpers.KeyEventAction;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.interfaces.IAvatarImageConsumer;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.PlainMenuButton;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class SelectPlayerScreen extends HeaderFooterScreen
    implements IAvatarImageConsumer {

    // translatable strings
    private static final String _S1 = "Avatar";
    private static final String _S2 = "Choose an avatar image for the selected player profile.";
    private static final String _S3 = "There must be at least one player defined.";
    private static final String _S4 = "This will delete the <b>%s</b> player profile.";
    private static final String _S5 = "Delete Player?";
    private static final String _S6 = "Delete";
    private static final String _S7 = "Cancel";
    private static final String _S9 = "Select";
    private static final String _S10 = "Settings";
    private static final String _S11 = "Update player profile settings.";
    private static final String _S12 = "New";
    private static final String _S13 = "Create a new player profile.";
    private static final String _S15 = "Delete selected player profile (confirmation required).";
    private static final String _S16 = "All associated information such as player stats will also be removed.";
    private static final String _S17 = "This action cannot be undone!";

    private final List<IPlayerProfileListener> listeners = new ArrayList<>();
    private final JList<? extends PlayerProfile> playersJList;

    protected HashMap<String, PlayerProfile> profilesMap = new HashMap<>();

    protected abstract void createDefaultPlayerProfiles() throws IOException;
    protected abstract int getPreferredWidth();
    protected abstract void refreshProfilesJList();
    protected abstract void refreshProfilesJList(final PlayerProfile playerProfile);
    protected abstract HashMap<String, PlayerProfile> getPlayerProfilesMap();
    protected abstract void doNewPlayerAction();
    protected abstract void doEditPlayerAction();

    // CTR
    protected SelectPlayerScreen(String name, final JList<? extends PlayerProfile> playersJList) {
        super(name);
        this.playersJList = playersJList;
        this.playersJList.addMouseListener(new DoubleClickAdapter());
        setMainContent(new ScreenContent());
        setFooter();
        setKeyEvents();
    }



    private void setFooter() {
        setLeftFooter(PlainMenuButton.getCloseScreenButton(MText.get(_S7)));
        setRightFooter(PlainMenuButton.build(this::doNextAction, MText.get(_S9)));
        addToFooter(PlainMenuButton.build(this::doEditPlayerAction,
                MText.get(_S10), MText.get(_S11)
            ),
            PlainMenuButton.build(this::doNewPlayerAction,
                MText.get(_S12), MText.get(_S13)
            ),
            PlainMenuButton.build(this::deleteSelectedPlayer,
                MText.get(_S6), MText.get(_S15)
            ),
            new SelectAvatarActionButton()
        );
    }

    private void setKeyEvents() {
        KeyEventAction.doAction(this, this::doNextAction)
            .onFocus(0, KeyEvent.VK_ENTER);
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
                return o1.getPlayerName().compareToIgnoreCase(o2.getPlayerName());
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
        MagicImages.getClearAvatarsCache();
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
            super(MText.get(_S1), MText.get(_S2), new SelectAvatarAction());
        }
    }

    private class SelectAvatarAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            ScreenController.showAvatarImagesScreen(SelectPlayerScreen.this);
        }
    }

    private boolean isDeletePlayerConfirmedByUser(final PlayerProfile profile) {
        final int action = JOptionPane.showOptionDialog(ScreenController.getFrame(),
            String.format("<html>%s<br>%s<br><br><b>%s</b></html>",
                MText.get(_S4, profile.getPlayerName()),
                MText.get(_S16),
                MText.get(_S17)),
            MText.get(_S5),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{MText.get(_S6), MText.get(_S7)}, MText.get(_S7));
        return (action == JOptionPane.YES_OPTION);
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
            ScreenController.showWarningMessage(MText.get(_S3));
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
        MouseHelper.showBusyCursor();
        notifyPlayerSelected(getSelectedPlayer());
        ScreenController.closeActiveScreen(false);
        MouseHelper.showDefaultCursor();
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
            listener.playerProfileUpdated(player);
        }
    }

    private synchronized void notifyPlayerDeleted(final PlayerProfile player) {
        for (final IPlayerProfileListener listener : listeners) {
            listener.playerProfileDeleted(player);
        }
    }

    private synchronized void notifyPlayerSelected(final PlayerProfile player) {
        for (final IPlayerProfileListener listener : listeners) {
            listener.playerProfileSelected(player);
        }
    }

    protected JList<? extends PlayerProfile> getJList() {
        return playersJList;
    }
}
