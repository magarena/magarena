package magic.ui.screen;

import magic.ai.MagicAIImpl;
import magic.model.player.AiProfile;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.ui.dialog.AiPropertiesDialog;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.player.AiPlayerJList;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author SPR
 *
 */
@SuppressWarnings("serial")
public class SelectAiPlayerScreen
    extends SelectPlayerScreen
    implements IStatusBar {

    // CTR
    public SelectAiPlayerScreen(final IPlayerProfileListener listener, final PlayerProfile playerProfile) {
        super(new AiPlayerJList());
        addListener(listener);
        refreshProfilesJList(playerProfile);
    }

    private AiProfile[] getPlayerProfilesArray() {
        final List<PlayerProfile> sortedPlayersList = getSortedPlayersList();
        return sortedPlayersList.toArray(new AiProfile[sortedPlayersList.size()]);
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.SelectPlayerAbstractScreen#createDefaultPlayerProfiles()
     */
    @Override
    protected void createDefaultPlayerProfiles() throws IOException {
        // Les Vegas
        AiProfile profile = new AiProfile();
        profile.setPlayerName("Les Vegas");
        profile.setAiType(MagicAIImpl.VEGAS);
        profile.setAiLevel(6);
        profile.save();
        // Mini Max
        profile = new AiProfile();
        profile.setPlayerName("Mini Max");
        profile.setAiType(MagicAIImpl.MMAB);
        profile.setAiLevel(6);
        profile.save();
        // Monty Carlo
        profile = new AiProfile();
        profile.setPlayerName("Monty Carlo");
        profile.setAiType(MagicAIImpl.MCTS);
        profile.setAiLevel(6);
        profile.save();
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "Select AI Player";
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
        return 540;
    }

    private class NewPlayerAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            createNewPlayerProfile();
        }

        private void createNewPlayerProfile() {
            final AiPropertiesDialog dialog = new AiPropertiesDialog(getFrame());
            final PlayerProfile newProfile = dialog.getPlayerProfile();
            if (newProfile != null) {
                PlayerProfiles.getPlayerProfiles().put(newProfile.getId(), newProfile);
                refreshProfilesJList(newProfile);
            }
        }

    }

    private class EditPlayerAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            final AiProfile profile = (AiProfile)getSelectedPlayer();
            new AiPropertiesDialog(getFrame(), profile);
            getJList().repaint();
            notifyPlayerUpdated(profile);
        }
    }

    @Override
    protected void refreshProfilesJList() {
        refreshProfilesJList(null);
    }
    @Override
    protected void refreshProfilesJList(PlayerProfile playerProfile) {
        ((AiPlayerJList)getJList()).setListData(getPlayerProfilesArray());
        setSelectedListItem(playerProfile);
    }

    @Override
    protected HashMap<String, PlayerProfile> getPlayerProfilesMap() {
        return PlayerProfiles.getAiPlayerProfiles();
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
