package magic.ui.screen.duel.player;

import magic.model.player.PlayerProfile;

@SuppressWarnings("serial")
class HumanPlayerJList extends PlayersJList {

    @Override
    protected String getPlayerSettingsLabelText(PlayerProfile aProfile) {
        return "";
    }

}
