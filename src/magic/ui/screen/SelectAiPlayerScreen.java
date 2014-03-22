package magic.ui.screen;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import magic.ai.MagicAIImpl;
import magic.model.player.AiPlayer;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.ui.dialog.AiPropertiesDialog;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.player.AiPlayerJList;

/**
 * @author SPR
 *
 */
@SuppressWarnings("serial")
public class SelectAiPlayerScreen
    extends SelectPlayerScreen
    implements IStatusBar, IActionBar {

    // CTR
    public SelectAiPlayerScreen(final IPlayerProfileListener listener, final PlayerProfile playerProfile) {
        super(new AiPlayerJList());
        addListener(listener);
        refreshProfilesJList(playerProfile);
    }

    private AiPlayer[] getPlayerProfilesArray() {
        final List<PlayerProfile> sortedPlayersList = getSortedPlayersList();
        return sortedPlayersList.toArray(new AiPlayer[sortedPlayersList.size()]);
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.SelectPlayerAbstractScreen#createDefaultPlayerProfiles()
     */
    @Override
    protected void createDefaultPlayerProfiles() throws IOException {
        // Les Vegas
        AiPlayer profile = new AiPlayer();
        profile.setPlayerName("Les Vegas");
        profile.setAiType(MagicAIImpl.VEGAS);
        profile.setAiLevel(6);
        profile.save();
        // Mini Max
        profile = new AiPlayer();
        profile.setPlayerName("Mini Max");
        profile.setAiType(MagicAIImpl.MMAB);
        profile.setAiLevel(6);
        profile.save();
        // Monty Carlo
        profile = new AiPlayer();
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
                        "New", "Create a new AI player profile.",
                        new NewPlayerAction()));
        buttons.add(
                new ActionBarButton(
                        "Edit", "Update selected AI player's properties.",
                        new EditPlayerAction()));
        buttons.add(
                new ActionBarButton(
                        "Delete", "Delete selected AI player profile.",
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
            final AiPlayer profile = (AiPlayer)getSelectedPlayer();
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

}
