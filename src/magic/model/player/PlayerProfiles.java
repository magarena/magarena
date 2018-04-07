package magic.model.player;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import magic.ai.MagicAIImpl;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public final class PlayerProfiles {
    private PlayerProfiles() {}

    // default AI avatars.
    private static final Path AVATARS_PATH = MagicFileSystem.getDataPath(DataPath.AVATARS);
    private static final Path AVATAR_LesVegas = AVATARS_PATH.resolve("tux").resolve("tux25.png");
    private static final Path AVATAR_MontyCarlo = AVATARS_PATH.resolve("default").resolve("face09.png");
    private static final Path AVATAR_MiniMax = AVATARS_PATH.resolve("default").resolve("face18.png");

    private static final Path profilesPath = MagicFileSystem.getDataPath(DataPath.PLAYERS);
    private static final HashMap<String, PlayerProfile> profilesMap = new HashMap<>();
    static {
        refreshMap();
    }

    public static void refreshMap() {
        profilesMap.clear();
        // Humans
        for (Path path : getProfilePaths("human")) {
            final String profileId = path.getFileName().toString();
            final HumanProfile player = new HumanProfile(profileId);
            profilesMap.put(profileId, player);
        }
        // AIs
        for (Path path : getProfilePaths("ai")) {
            final String profileId = path.getFileName().toString();
            final AiProfile player = new AiProfile(profileId);
            profilesMap.put(profileId, player);
        }
    }

    private static List<Path> getProfilePaths(final String playerType) {
        final Path playersPath = profilesPath.resolve(playerType);
        List<Path> profilePaths = getDirectoryPaths(playersPath);
        if (profilePaths.isEmpty()) {
            if ("human".equals(playerType)) {
                createDefaultHumanPlayerProfiles();
            } else {
                createDefaultAiPlayerProfiles();
            }
            profilePaths = getDirectoryPaths(playersPath);
        }
        return profilePaths;
    }

    private static List<Path> getDirectoryPaths(final Path rootPath) {
        final List<Path> paths = new ArrayList<>();
        if (Files.exists(rootPath)) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(rootPath, new DirectoriesFilter())) {
                for (Path p : ds) {
                    paths.add(p.getFileName());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return paths;
    }
    private static class DirectoriesFilter implements Filter<Path> {
        @Override
        public boolean accept(Path entry) {
            return Files.isDirectory(entry);
        }
    }

    public static void createDefaultHumanPlayerProfiles() {
        final HumanProfile profile = HumanProfile.create(getDefaultPlayerProfileName());
        profile.save();
    }

    private static String getDefaultPlayerProfileName() {
        final String systemUserName = System.getProperty("user.name");
        return systemUserName == null ? "Player" : systemUserName;

    }

    public static void createDefaultAiPlayerProfiles() {
        createAiPlayerProfile("Les Vegas", MagicAIImpl.VEGAS, 6, PlayerProfiles.AVATAR_LesVegas);
        createAiPlayerProfile("Mini Max", MagicAIImpl.MMAB, 6, PlayerProfiles.AVATAR_MiniMax);
        createAiPlayerProfile("Monty Carlo", MagicAIImpl.MCTS, 6, PlayerProfiles.AVATAR_MontyCarlo);
    }

    private static void createAiPlayerProfile(
            final String name,
            final MagicAIImpl aiImpl,
            final int level,
            final Path avatarPath) {
        final AiProfile profile = AiProfile.create(name, aiImpl, level);
        profile.save();
        setPlayerAvatar(profile, avatarPath);
    }

    public static void setPlayerAvatar(final PlayerProfile profile, final Path avatarPath) {
        final Path targetPath = profile.getProfilePath().resolve("player.avatar");
        try {
            Files.copy(avatarPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (NoSuchFileException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static PlayerProfile getDefaultHumanPlayer() {
        return getHumanPlayerProfiles().values().iterator().next();
    }

    public static PlayerProfile getDefaultAiPlayer() {
        for (PlayerProfile profile : getAiPlayerProfiles().values()) {
            final AiProfile aiProfile = (AiProfile) profile;
            if (aiProfile.getAiType() == MagicAIImpl.MCTS) {
                return profile;
            }
        }
        // No MCTS profile exists which can happen when importing
        // previous set of players and default AI profiles have
        // been deleted or changed by user. (see github issue #329).
        return getAiPlayerProfiles().values().iterator().next();
    }

    private static HashMap<String, PlayerProfile> getPlayerProfiles(final Class<? extends PlayerProfile> profileClass) {
        final HashMap<String, PlayerProfile> filteredProfiles = new HashMap<>();
        for (PlayerProfile profile : profilesMap.values()) {
            if (profile.getClass().equals(profileClass)) {
                filteredProfiles.put(profile.getId(), profile);
            }
        }
        return filteredProfiles;
    }

    public static HashMap<String, PlayerProfile> getHumanPlayerProfiles() {
        return getPlayerProfiles(HumanProfile.class);
    }

    public static HashMap<String, PlayerProfile> getAiPlayerProfiles() {
        return getPlayerProfiles(AiProfile.class);
    }

    /**
     * @param duelConfigId
     * @return
     */
    public static PlayerProfile getPlayerProfile(String duelConfigId) {
        return profilesMap.get(duelConfigId);
    }

    public static HashMap<String, PlayerProfile> getPlayerProfiles() {
        return profilesMap;
    }

    public static void deletePlayer(final PlayerProfile playerProfile) {
        MagicFileSystem.deleteDirectory(getPlayerProfileDirectory(playerProfile));
        profilesMap.remove(playerProfile.getId());
    }

    private static Path getPlayerProfileDirectory(final PlayerProfile playerProfile) {
        return profilesPath.resolve(playerProfile.getPlayerType()).resolve(playerProfile.getId());
    }

    public static boolean canDeleteProfile(final PlayerProfile playerProfile) {
        return getPlayerProfiles(playerProfile.getClass()).size() > 1;
    }

}
