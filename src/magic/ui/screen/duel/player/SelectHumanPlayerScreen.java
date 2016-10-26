package magic.ui.screen.duel.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import magic.model.player.HumanProfile;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.translate.MText;
import magic.ui.ScreenController;

@SuppressWarnings("serial")
public class SelectHumanPlayerScreen extends SelectPlayerScreen {

    // translatable strings
    private static final String _S1 = "Select Player";
    private static final String _S2 = "Player Name";
    private static final String _S3 = "New Player";
    private static final String _S4 = "Update Player";

    public SelectHumanPlayerScreen(final IPlayerProfileListener listener, final PlayerProfile playerProfile) {
        super(MText.get(_S1), new HumanPlayerJList());
        addListener(listener);
        refreshProfilesJList(playerProfile);
    }

    private HumanProfile[] getPlayerProfilesArray() {
        final List<PlayerProfile> sortedPlayersList = getSortedPlayersList();
        return sortedPlayersList.toArray(new HumanProfile[0]);
    }

    @Override
    protected void createDefaultPlayerProfiles() throws IOException {
        PlayerProfiles.createDefaultHumanPlayerProfiles();
    }

    @Override
    protected int getPreferredWidth() {
        return 490;
    }

    @Override
    protected void doNewPlayerAction() {
        final String newName = (String) JOptionPane.showInputDialog(ScreenController.getFrame(),
            String.format("<html><b>%s</b><br></html>", MText.get(_S2)),
            MText.get(_S3),
            JOptionPane.PLAIN_MESSAGE,
            null, null, null);
        if (newName != null && !newName.trim().isEmpty()) {
            final PlayerProfile newProfile = HumanProfile.create(newName);
            newProfile.save();
            PlayerProfiles.getPlayerProfiles().put(newProfile.getId(), newProfile);
            refreshProfilesJList(newProfile);
        }
    }

    @Override
    protected void doEditPlayerAction() {
        final PlayerProfile profile = getSelectedPlayer();
        final String newName = (String) JOptionPane.showInputDialog(ScreenController.getFrame(),
            String.format("<html><b>%s</b><br></html>", MText.get(_S2)),
            MText.get(_S4),
            JOptionPane.PLAIN_MESSAGE,
            null, null, profile.getPlayerName());
        if (newName != null && !newName.trim().isEmpty()) {
            profile.setPlayerName(newName.trim());
            profile.save();
            notifyPlayerUpdated(getSelectedPlayer());
        }
        getJList().repaint();
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
}
