package magic.ui.screen.duel.player;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import magic.model.player.HumanProfile;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.translate.UiString;
import magic.ui.screen.interfaces.IStatusBar;

@SuppressWarnings("serial")
public class SelectHumanPlayerScreen
    extends SelectPlayerScreen
    implements IStatusBar {

    // translatable strings
    private static final String _S1 = "Select Player";
    private static final String _S2 = "Player Name";
    private static final String _S3 = "New Player";
    private static final String _S4 = "Update Player";

    public SelectHumanPlayerScreen(final IPlayerProfileListener listener, final PlayerProfile playerProfile) {
        super(new HumanPlayerJList());
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
    public String getScreenCaption() {
        return UiString.get(_S1);
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
            doNewPlayerProfile();
        }

        private void doNewPlayerProfile() {
            final String newName = (String)JOptionPane.showInputDialog(
                    getFrame(),
                    String.format("<html><b>%s</b><br></html>", UiString.get(_S2)),
                    UiString.get(_S3),
                    JOptionPane.PLAIN_MESSAGE,
                    null, null, null);
            if (newName != null && !newName.trim().isEmpty()) {
                final PlayerProfile newProfile = HumanProfile.create(newName);
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
                    String.format("<html><b>%s</b><br></html>", UiString.get(_S2)),
                    UiString.get(_S4),
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

    @Override
    protected AbstractAction getNewPlayerAction() {
        return new NewPlayerAction();
    }

    @Override
    protected AbstractAction getEditPlayerAction() {
        return new EditPlayerAction();
    }

}
