package magic.ui.screen.duel.player;

import magic.model.player.AiProfile;
import magic.model.player.PlayerProfile;
import magic.translate.StringContext;
import magic.translate.MText;

@SuppressWarnings("serial")
class AiPlayerJList extends PlayersJList {

    // translatable strings
    @StringContext(eg="this is the AI level.")
    private static final String _S2 = "Level";
    private static final String _S3 = "Extra Life";

    @Override
    protected String getPlayerSettingsLabelText(PlayerProfile aProfile) {
        final AiProfile profile = (AiProfile) aProfile;
        return String.format("<html>%s<br>%s: %d, %s: %d</html>",
                profile.getAiType(),
                MText.get(_S2), profile.getAiLevel(),
                MText.get(_S3), profile.getExtraLife());
    }

}
