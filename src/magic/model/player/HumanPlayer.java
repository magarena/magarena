package magic.model.player;

import java.util.Properties;
import java.util.Map;

public class HumanPlayer extends PlayerProfile {

    private static final String PLAYER_TYPE = "human";

    public HumanPlayer(final String profileId) {
        super(profileId);
        loadProperties();
    }

    public HumanPlayer() {
        this(null);
    }

    @Override
    public void save() {
        saveProperties(new Properties());
    }

    @Override
    protected void loadProperties() {
        loadPlayerProperties();
    }

    /* (non-Javadoc)
     * @see magic.model.player.PlayerProfile#getPlayerType()
     */
    @Override
    protected String getPlayerType() {
        return PLAYER_TYPE;
    }
    
    @Override
    public Map<String, PlayerProfile> getSimilarPlayerProfiles() {
        return PlayerProfiles.getHumanPlayerProfiles();
    }
    
    @Override
    public boolean isArtificial() {
        return false;
    }
}
