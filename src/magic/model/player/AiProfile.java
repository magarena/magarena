package magic.model.player;

import java.util.Properties;
import magic.ai.MagicAIImpl;
import magic.translate.StringContext;
import magic.translate.UiString;
import magic.utility.SortedProperties;

public class AiProfile extends PlayerProfile {

    // translatable strings
    @StringContext(eg = "AI: minimax")
    private static final String _S1 = "AI : %s";
    private static final String _S2 = "Level: %d  Extra Life: %d";
    @StringContext(eg = "Mini Max, level 6 AI (MMAB)")
    private static final String _S3 = "%s, level %d AI (%s)";

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

    public AiProfile(final String profileId) {
        super(profileId);
        loadProperties();
    }

    public AiProfile() {
        loadProperties();
    }

    public static AiProfile create(final MagicAIImpl aiImpl, final int level) {
        return create(aiImpl.name(), aiImpl, level);
    }

    public static AiProfile create(final String name, final MagicAIImpl aiImpl, final int level) {
        final AiProfile ap = new AiProfile();
        ap.setPlayerName(name);
        ap.setAiType(aiImpl);
        ap.setAiLevel(level);
        return ap;
    }

    public int getExtraLife() {
        return extraLife;
    }
    public void setExtraLife(final int value) {
        extraLife = value;
    }

    public int getAiLevel() {
        return aiLevel;
    }
    public void setAiLevel(final int value) {
        aiLevel = value;
    }

    public MagicAIImpl getAiType() {
        return aiType;
    }
    public void setAiType(final MagicAIImpl value) {
        aiType = value;
    }

    @Override
    public void save() {
        final Properties properties = new SortedProperties();
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

    @Override
    protected String getPlayerType() {
        return PLAYER_TYPE;
    }

    @Override
    public String getPlayerTypeLabel() {
        return UiString.get(_S1, getAiType());
    }

    @Override
    public String getPlayerAttributeLabel() {
        return UiString.get(_S2, getAiLevel(), getExtraLife());
    }

    @Override
    public String getPlayerLabel() {
        return UiString.get(_S3, getPlayerName(), getAiLevel(), getAiType().name());
    }
}
