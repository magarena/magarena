package magic.ui.player;

import magic.model.player.PlayerProfile;

@SuppressWarnings("serial")
public class HumanPlayerJList extends PlayersJList {

    @Override
    protected String getPlayerSettingsLabelText(PlayerProfile aProfile) {
        return "";
    }

}
