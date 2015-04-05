package magic.ui.screen;

import magic.model.player.HumanProfile;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.player.HumanPlayerJList;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("serial")
public class SelectHumanPlayerScreen
    extends SelectPlayerScreen
    implements IStatusBar {

    // CTR
    public SelectHumanPlayerScreen(final IPlayerProfileListener listener, final PlayerProfile playerProfile) {
        super(new HumanPlayerJList());
        addListener(listener);
        refreshProfilesJList(playerProfile);
    }

    private HumanProfile[] getPlayerProfilesArray() {
        final List<PlayerProfile> sortedPlayersList = getSortedPlayersList();
        return sortedPlayersList.toArray(new HumanProfile[sortedPlayersList.size()]);
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.SelectPlayerAbstractScreen#createDefaultPlayerProfiles()
     */
    @Override
    protected void createDefaultPlayerProfiles() throws IOException {
        final HumanProfile profile = new HumanProfile();
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
                final PlayerProfile newProfile = new HumanProfile();
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
            getJList().repaint();
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
    protected void refreshProfilesJList() {
        refreshProfilesJList(null);
    }
    @Override
    protected void refreshProfilesJList(PlayerProfile playerProfile) {
        ((HumanPlayerJList)getJList()).setListData(getPlayerProfilesArray());
        setSelectedListItem(playerProfile);
    }

    @Override
    protected HashMap<String, PlayerProfile> getPlayerProfilesMap() {
        return PlayerProfiles.getHumanPlayerProfiles();
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.SelectPlayerScreen#getNewPlayerAction()
     */
    @Override
    protected AbstractAction getNewPlayerAction() {
        return new NewPlayerAction();
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.SelectPlayerScreen#getEditPlayerAction()
     */
    @Override
    protected AbstractAction getEditPlayerAction() {
        return new EditPlayerAction();
    }

}
