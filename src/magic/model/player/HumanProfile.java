package magic.model.player;

import java.util.Properties;

public class HumanProfile extends PlayerProfile {

    private static final String PLAYER_TYPE = "human";

    public HumanProfile(final String profileId) {
        super(profileId);
        loadProperties();
    }

    public HumanProfile() {
        loadProperties();
    }

    public static HumanProfile create(final String name) {
        final HumanProfile hp = new HumanProfile();
        hp.setPlayerName(name);
        return hp;
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
