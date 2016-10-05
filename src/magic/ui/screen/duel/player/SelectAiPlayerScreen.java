package magic.ui.screen.duel.player;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.model.player.AiProfile;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.translate.UiString;
import magic.ui.dialog.AiPropertiesDialog;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.interfaces.IStatusBar;

@SuppressWarnings("serial")
public class SelectAiPlayerScreen
    extends SelectPlayerScreen
    implements IStatusBar {

    // translatable strings
    private static final String _S1 =  "Select AI Player";

    public SelectAiPlayerScreen(final IPlayerProfileListener listener, final PlayerProfile playerProfile) {
        super(new AiPlayerJList());
        addListener(listener);
        refreshProfilesJList(playerProfile);
    }

    private AiProfile[] getPlayerProfilesArray() {
        final List<PlayerProfile> sortedPlayersList = getSortedPlayersList();
        return sortedPlayersList.toArray(new AiProfile[0]);
    }

    @Override
    protected void createDefaultPlayerProfiles() throws IOException {
        PlayerProfiles.createDefaultAiPlayerProfiles();
    }

    @Override
    public String getScreenCaption() {
        return UiString.get(_S1);
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    @Override
    public JPanel getStatusPanel() {
        return null;
    }

    @Override
    protected int getPreferredWidth() {
        return 490;
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

    @Override
    protected AbstractAction getNewPlayerAction() {
        return new NewPlayerAction();
    }

    @Override
    protected AbstractAction getEditPlayerAction() {
        return new EditPlayerAction();
    }

}
