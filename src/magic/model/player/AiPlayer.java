package magic.model.player;

import magic.ai.MagicAIImpl;

import java.util.Properties;
import java.util.Map;

public class AiPlayer extends PlayerProfile {

    private static final String PLAYER_TYPE = "ai";

    private static final String KEY_EXTRA_LIFE = "extraLife";
    private static final String KEY_AI_LEVEL = "aiLevel";
    private static final String KEY_AI_TYPE = "aiType";

    private static final int DEFAULT_EXTRA_LIFE = 0;
    private static final int DEFAULT_AI_LEVEL = 6;
    private static final MagicAIImpl DEFAULT_AI_TYPE = MagicAIImpl.MMAB;

    private int extraLife = DEFAULT_EXTRA_LIFE;
    private int aiLevel = DEFAULT_AI_LEVEL;
    private MagicAIImpl aiType = DEFAULT_AI_TYPE;

    public AiPlayer(final String profileId) {
        super(profileId);
        loadProperties();
    }
    public AiPlayer() {
        this(null);
    }

    @Override
    public int getExtraLife() {
        return extraLife;
    }
    public void setExtraLife(final int value) {
        extraLife = value;
    }

    @Override
    public int getAiLevel() {
        return aiLevel;
    }
    public void setAiLevel(final int value) {
        aiLevel = value;
    }

    @Override
    public MagicAIImpl getAiType() {
        return aiType;
    }
    public void setAiType(final MagicAIImpl value) {
        aiType = value;
    }

    @Override
    public void save() {
        final Properties properties = new Properties();
        properties.setProperty(KEY_EXTRA_LIFE, String.valueOf(getExtraLife()));
        properties.setProperty(KEY_AI_LEVEL, String.valueOf(getAiLevel()));
        properties.setProperty(KEY_AI_TYPE, getAiType().name());
        saveProperties(properties);
    }

    @Override
    protected void loadProperties() {
        final Properties properties = loadPlayerProperties();
        extraLife = Integer.parseInt(properties.getProperty(KEY_EXTRA_LIFE, Integer.toString(DEFAULT_EXTRA_LIFE)));
        aiLevel = Integer.parseInt(properties.getProperty(KEY_AI_LEVEL, Integer.toString(DEFAULT_AI_LEVEL)));
        aiType = MagicAIImpl.valueOf(properties.getProperty(KEY_AI_TYPE, DEFAULT_AI_TYPE.name()));
    }
    /* (non-Javadoc)
     * @see magic.model.player.PlayerProfile#getPlayerType()
     */
    @Override
    protected String getPlayerType() {
        return PLAYER_TYPE;
    }
    
    @Override
    public String getPlayerTypeLabel() {
        return "AI : " + getAiType();
    }
    
    @Override
    public String getPlayerAttributeLabel() {
        return "Level: " + getAiLevel() + "  Extra Life: " + getExtraLife();
    }
    
    @Override
    public Map<String, PlayerProfile> getPlayerProfiles() {
        return PlayerProfiles.getAiPlayerProfiles();
    }
    
    @Override
    public boolean isArtificial() {
        return true;
    }
}
