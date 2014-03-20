package magic.model.player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import magic.MagicMain;
import magic.data.FileIO;
import magic.data.IconImages;
import magic.model.MagicGameReport;
import magic.ui.theme.PlayerAvatar;

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
        loadAvatar();
    }

    public String getId() {
        return profilePath.getFileName().toString();
    }

    private void setProfilePath(final String profileId) {
        profilePath = Paths.get(MagicMain.getPlayerProfilesPath()).resolve(getPlayerType());
        verifyProfilePath();
        profilePath = profilePath.resolve(profileId);
    }

    private void verifyProfilePath() {
        if (!Files.exists(profilePath)) {
            try {
                Files.createDirectory(profilePath);
            } catch (IOException e) {
                MagicGameReport.reportException(Thread.currentThread(), e);
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
            MagicGameReport.reportException(Thread.currentThread(), e);
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

    private void loadAvatar() {
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

    public static boolean isAiPlayer(final PlayerProfile player) {
        return player instanceof AiPlayer;
    }
}
