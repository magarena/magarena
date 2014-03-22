package magic.ui.screen;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import magic.model.player.HumanPlayer;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.player.HumanPlayerJList;

@SuppressWarnings("serial")
public class SelectHumanPlayerScreen
    extends SelectPlayerAbstractScreen
    implements IStatusBar, IActionBar {

    private HumanPlayerJList profilesJList;

    // CTR
    public SelectHumanPlayerScreen(final IPlayerProfileListener listener, final PlayerProfile playerProfile) {
        addListener(listener);
        refreshProfilesJList(playerProfile);
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.PlayerScreenUtil#getProfilesListPanel()
     */
    @Override
    protected JPanel getProfilesListPanel() {
        profilesJList = new HumanPlayerJList();
        profilesJList.addMouseListener(new DoubleClickAdapter());
        return new ContainerPanel(profilesJList);
    }

    private HumanPlayer[] getPlayerProfilesArray() {
        final List<PlayerProfile> sortedPlayersList = getSortedPlayersList();
        return sortedPlayersList.toArray(new HumanPlayer[sortedPlayersList.size()]);
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.SelectPlayerAbstractScreen#createDefaultPlayerProfiles()
     */
    @Override
    protected void createDefaultPlayerProfiles() throws IOException {
        final HumanPlayer profile = new HumanPlayer();
        profile.setPlayerName(getDefaultPlayerProfileName());
        profile.save();
    }

    private String getDefaultPlayerProfileName() {
        final String systemUserName = System.getProperty("user.name");
        return systemUserName == null ? "Player" : systemUserName;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "Select Player";
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        return super.getLeftAction();
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        return super.getRightAction();
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getMiddleActions()
     */
    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<MenuButton>();
        buttons.add(
                new ActionBarButton(
                        "New", "Create a new player profile.",
                        new NewPlayerAction()));
        buttons.add(
                new ActionBarButton(
                        "Edit", "Update selected player's name.",
                        new EditPlayerAction()));
        buttons.add(
                new ActionBarButton(
                        "Delete", "Delete selected player profile.",
                        new DeletePlayerAction()));
        buttons.add(
                new SelectAvatarActionButton());
        return buttons;
    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#canScreenClose()
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.interfaces.IStatusBar#getStatusPanel()
     */
    @Override
    public JPanel getStatusPanel() {
        return null;
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.SelectPlayerAbstractScreen#getPreferredWidth()
     */
    @Override
    protected int getPreferredWidth() {
        return 420;
    }

    private class NewPlayerAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            doNewPlayerProfile();
        }

        private void doNewPlayerProfile() {
            final String newName = (String)JOptionPane.showInputDialog(
                    getFrame(),
                    "<html><b>Player Name</b><br></html>",
                    "New Player",
                    JOptionPane.PLAIN_MESSAGE,
                    null, null, null);
            if (newName != null && !newName.trim().isEmpty()) {
                final PlayerProfile newProfile = new HumanPlayer();
                newProfile.setPlayerName(newName);
                newProfile.save();
                PlayerProfiles.getPlayerProfiles().put(newProfile.getId(), newProfile);
                refreshProfilesJList(newProfile);
            }
        }

    }

    private class EditPlayerAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            doEditPlayerProfile();
            profilesJList.repaint();
        }

        private void doEditPlayerProfile() {
            final PlayerProfile profile = getSelectedPlayer();
            final String newName = (String)JOptionPane.showInputDialog(
                    getFrame(),
                    "<html><b>Player Name</b><br></html>",
                    "Update Player",
                    JOptionPane.PLAIN_MESSAGE,
                    null, null, profile.getPlayerName());
            if (newName != null && !newName.trim().isEmpty()) {
                profile.setPlayerName(newName.trim());
                profile.save();
                notifyPlayerUpdated(getSelectedPlayer());
            }
        }

    }

    @Override
    protected JList<? extends PlayerProfile> getProfilesJList() {
        return profilesJList;
    }

    @Override
    protected void refreshProfilesJList() {
        refreshProfilesJList(null);
    }
    @Override
    protected void refreshProfilesJList(PlayerProfile playerProfile) {
        profilesJList.setListData(getPlayerProfilesArray());
        setSelectedListItem(playerProfile);
    }

    @Override
    protected HashMap<String, PlayerProfile> getPlayerProfilesMap() {
        return PlayerProfiles.getHumanPlayerProfiles();
    }

}
