package magic.model.player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import magic.data.FileIO;
import magic.ui.IconImages;
import magic.ui.theme.PlayerAvatar;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public abstract class PlayerProfile {

    private static String lastId = "";

    private Path profilePath = null;
    private String playerName = "";
    private PlayerStatistics stats;
    private PlayerAvatar avatar;

    abstract protected void loadProperties();
    abstract public void save();
    abstract protected String getPlayerType();

    protected PlayerProfile(final String profileId) {
        setProfilePath(profileId == null ? PlayerProfile.getNewPlayerProfileId() : profileId);
        loadStats();
        if (java.awt.GraphicsEnvironment.isHeadless() == false) {
            loadAvatar();
        }
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
        final Properties properties = propertiesFile.exists() ? FileIO.toProp(propertiesFile) : new Properties();
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

    public PlayerAvatar getAvatar() {
        return avatar;
    }

    public void loadAvatar() {
        final File file = new File(profilePath.resolve("player.avatar").toString());
        if (file.exists()) {
            avatar = new PlayerAvatar(FileIO.toImg(file, IconImages.MISSING));
        } else {
            avatar = new PlayerAvatar(IconImages.MISSING);
        }
    }

    private void loadStats() {
        stats = new PlayerStatistics(this);
    }

    public static String getNewPlayerProfileId() {
        String id = Long.toHexString(System.currentTimeMillis()).toUpperCase();
        while (id.equals(lastId)) {
            // wait a bit in order to generate a unique id based on current time.
            // required because currentTimeMillis() granularity dependent on OS.
            sleep(100);
            id = Long.toHexString(System.currentTimeMillis()).toUpperCase();
        }
        lastId = id;
        return id;
    }

    private static void sleep(final long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static PlayerProfile getHumanPlayer(final String playerId) {
        if (playerId != null && PlayerProfiles.getPlayerProfile(playerId) != null) {
            return new HumanPlayer(playerId);
        } else {
            return PlayerProfiles.getDefaultHumanPlayer();
        }
    }

    public static PlayerProfile getAiPlayer(final String playerId) {
        if (playerId != null && PlayerProfiles.getPlayerProfile(playerId) != null) {
            return new AiPlayer(playerId);
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

    public static boolean isAiPlayer(final PlayerProfile player) {
        return player instanceof AiPlayer;
    }
}
