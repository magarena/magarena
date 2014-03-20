package magic.ui.screen;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import magic.MagicMain;
import magic.model.player.AiPlayer;
import magic.model.player.HumanPlayer;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.ui.screen.interfaces.IAvatarImageConsumer;
import magic.ui.screen.interfaces.IPlayerProfileConsumer;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class SelectPlayerAbstractScreen
    extends AbstractScreen
    implements IAvatarImageConsumer {

    protected final Path playersPath;
    protected HashMap<String, PlayerProfile> profilesMap = new HashMap<String, PlayerProfile>();
    protected final IPlayerProfileConsumer consumer;

    protected abstract JPanel getProfilesListPanel();
    protected abstract String getPlayerType();
    protected abstract void createDefaultPlayerProfiles() throws IOException;
    protected abstract int getPreferredWidth();
    protected abstract JList<? extends PlayerProfile> getProfilesJList();
    protected abstract void refreshProfilesJList();
    protected abstract HashMap<String, PlayerProfile> getPlayerProfilesMap();

    // CTR
    protected SelectPlayerAbstractScreen(final IPlayerProfileConsumer consumer) {
        this.consumer = consumer;
        this.playersPath = Paths.get(MagicMain.getPlayerProfilesPath()).resolve(getPlayerType());
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
            getProfilesJList().setSelectedIndex(0);
        } else {
            getProfilesJList().setSelectedValue(profilesMap.get(playerProfile.getId()), true);
        }
        setFocusInProfilesJList(getProfilesJList());
    }

    protected List<PlayerProfile> getSortedPlayersList() {
        profilesMap = getPlayerProfilesMap();
        final List<PlayerProfile> profilesByName = new ArrayList<PlayerProfile>(profilesMap.values());
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
    }

    protected PlayerProfile getSelectedPlayer() {
        return getProfilesJList().getSelectedValue();
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
            getFrame().showAvatarImagesScreen(SelectPlayerAbstractScreen.this);
        }
    }

    private class ScreenContent extends JPanel {
        public ScreenContent() {
            setOpaque(false);
            setLayout(new MigLayout("insets 2, center, center"));
            add(getProfilesListPanel(), "w " + getPreferredWidth() + "!, h 80%");
        }
    }

    protected class ContainerPanel extends TexturedPanel {

        public ContainerPanel(final JList<? extends PlayerProfile> profilesJList) {
            profilesJList.setOpaque(false);
            setBorder(FontsAndBorders.BLACK_BORDER);
            setBackground(FontsAndBorders.MENUPANEL_COLOR);
            setLayout(new MigLayout("insets 0, gap 0, flowy"));
            add(new ScrollPane(profilesJList), "w 100%, h 100%");
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

    protected class DeletePlayerAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteSelectedPlayer();
        }

        private void deleteSelectedPlayer() {
            final PlayerProfile condemnedPlayer = getSelectedPlayer();
            if (isDeletePlayerValid(condemnedPlayer)) {
                if (isDeletePlayerConfirmedByUser(condemnedPlayer)) {
                    PlayerProfiles.deletePlayer(condemnedPlayer);
                    refreshProfilesJList();
                    if (condemnedPlayer.equals(consumer.getPlayer())) {
                        consumer.setPlayerProfile(getSelectedPlayer());
                    }
                }
            }
        }

        private boolean isDeletePlayerValid(final PlayerProfile playerProfile) {
            boolean isDeletePlayerValid = true;
            if (playerProfile instanceof HumanPlayer) {
                isDeletePlayerValid = (PlayerProfiles.getHumanPlayerProfiles().size() > 1);
            } else if (playerProfile instanceof AiPlayer) {
                isDeletePlayerValid = (PlayerProfiles.getAiPlayerProfiles().size() > 1);
            }
            if (!isDeletePlayerValid) {
                JOptionPane.showMessageDialog(
                        MagicMain.rootFrame,
                        "There must be at least one player defined.",
                        "Invalid Action",
                        JOptionPane.WARNING_MESSAGE);
            }
            return isDeletePlayerValid;
        }

        private boolean isDeletePlayerConfirmedByUser(final PlayerProfile profile) {
            final int action = JOptionPane.showOptionDialog(
                    MagicMain.rootFrame,
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

    protected void doNextAction() {
        consumer.setPlayerProfile(getSelectedPlayer());
        getFrame().closeActiveScreen(false);
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

}
