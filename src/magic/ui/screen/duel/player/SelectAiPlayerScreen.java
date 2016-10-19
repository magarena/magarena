package magic.ui.screen.duel.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import magic.model.player.AiProfile;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.translate.UiString;
import magic.ui.ScreenController;
import magic.ui.dialog.AiPropertiesDialog;

@SuppressWarnings("serial")
public class SelectAiPlayerScreen extends SelectPlayerScreen {

    // translatable strings
    private static final String _S1 =  "Select AI Player";

    public SelectAiPlayerScreen(final IPlayerProfileListener listener, final PlayerProfile playerProfile) {
        super(UiString.get(_S1), new AiPlayerJList());
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
    protected int getPreferredWidth() {
        return 490;
    }

    @Override
    protected void doNewPlayerAction() {
        final AiPropertiesDialog dialog = new AiPropertiesDialog(ScreenController.getFrame());
        final PlayerProfile newProfile = dialog.getPlayerProfile();
        if (newProfile != null) {
            PlayerProfiles.getPlayerProfiles().put(newProfile.getId(), newProfile);
            refreshProfilesJList(newProfile);
        }
    }

    @Override
    protected void doEditPlayerAction() {
        final AiProfile profile = (AiProfile) getSelectedPlayer();
        new AiPropertiesDialog(ScreenController.getFrame(), profile);
        getJList().repaint();
        notifyPlayerUpdated(profile);
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

}
