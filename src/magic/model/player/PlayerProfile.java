package magic.model.player;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.UUID;
import magic.ui.MagicImages;
import magic.ui.theme.PlayerAvatar;
import magic.utility.FileIO;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.SortedProperties;

public abstract class PlayerProfile {

    private Path profilePath = null;
    private String playerName = "";
    private PlayerStatistics stats;
    private PlayerAvatar avatar;

    abstract protected void loadProperties();
    abstract public void save();
    abstract protected String getPlayerType();

    /**
     * Loads an existing saved player profile.
     */
    protected PlayerProfile(final String profileId) {
        setProfilePath(profileId);
        loadStats();
    }

    /**
     * Creates a new player profile with a unique ID.
     * Use subclass {@code save()} method to make permanent.
     */
    protected PlayerProfile() {
        this(PlayerProfile.getNewPlayerProfileId());
    }


    public boolean isArtificial() {
        return this instanceof AiProfile;
    }

    public boolean isHuman() {
        return this instanceof HumanProfile;
    }

    public String getId() {
        return profilePath.getFileName().toString();
    }

    private void setProfilePath(final String profileId) {
        profilePath = MagicFileSystem.getDataPath(DataPath.PLAYERS).resolve(getPlayerType());
        verifyProfilePath();
        profilePath = profilePath.resolve(profileId);
    }

    private void verifyProfilePath() {
        if (!Files.exists(profilePath)) {
            try {
                Files.createDirectory(profilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void saveProperties(final Properties properties) {
        properties.setProperty("playerName", String.valueOf(getPlayerName()));
        final File file = new File(getProfilePath().resolve("player.profile").toString());
        verifyProfilePath();
        try {
            FileIO.toFile(file, properties, "Player profile settings");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Properties loadPlayerProperties() {
        final File propertiesFile = new File(getProfilePath().resolve("player.profile").toString());
        final Properties properties = propertiesFile.exists() ? FileIO.toProp(propertiesFile) : new SortedProperties();
        playerName = properties.getProperty("playerName", "");
        return properties;
    }

    public Path getProfilePath() {
        return profilePath;
    }

    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(final String playerName) {
        this.playerName = playerName == null ? "" : playerName;
    }

    public PlayerStatistics getStats() {
        return stats;
    }

    private void loadStats() {
        stats = new PlayerStatistics(this);
    }

    private static String getNewPlayerProfileId() {
        return UUID.randomUUID().toString();
    }

    public static PlayerProfile getHumanPlayer(final String playerId) {
        if (playerId != null && PlayerProfiles.getPlayerProfile(playerId) != null) {
            return new HumanProfile(playerId);
        } else {
            return PlayerProfiles.getDefaultHumanPlayer();
        }
    }

    public static PlayerProfile getAiPlayer(final String playerId) {
        if (playerId != null && PlayerProfiles.getPlayerProfile(playerId) != null) {
            return new AiProfile(playerId);
        } else {
            return PlayerProfiles.getDefaultAiPlayer();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof PlayerProfile) {
            final PlayerProfile profile = (PlayerProfile)obj;
            return (this.getId().equals(profile.getId()));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    public String getPlayerTypeLabel() {
        return "";
    }

    public String getPlayerAttributeLabel() {
        return "";
    }

    public String getPlayerLabel() {
        return playerName;
    }

    public PlayerAvatar getAvatar() {
        if (avatar == null && !GraphicsEnvironment.isHeadless()) {
            BufferedImage image = MagicImages.getAvatarImage(this);
            avatar = new PlayerAvatar(image);
        }
        return avatar;
    }

    /**
     * Forces re-loading of avatar image the next time it is requested.
     */
    public void clearCachedAvatar() {
        avatar = null;
    }
}
