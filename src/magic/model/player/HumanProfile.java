package magic.model.player;

import java.util.Properties;

public class HumanProfile extends PlayerProfile {

    private static final String PLAYER_TYPE = "human";

    public HumanProfile() { }
    
    public HumanProfile(final String profileId) {
        super(profileId);
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
        
}
