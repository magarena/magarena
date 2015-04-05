package magic.model.player;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    public static void refreshMap() {
        setProfilesMap();
    }

    private static void setProfilesMap() {
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
            try {
                if (playerType.equals("human")) {
                    createDefaultHumanPlayerProfiles();
                } else {
                    createDefaultAiPlayerProfiles();
                }
                profilePaths = getDirectoryPaths(playersPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
        public boolean accept(Path entry) throws IOException {
            return Files.isDirectory(entry);
        }
    }

    private static void createDefaultHumanPlayerProfiles() throws IOException {
        final HumanProfile profile = new HumanProfile();
        profile.setPlayerName(getDefaultPlayerProfileName());
        profile.save();
    }

    private static String getDefaultPlayerProfileName() {
        final String systemUserName = System.getProperty("user.name");
        return systemUserName == null ? "Player" : systemUserName;

    }

    private static void createDefaultAiPlayerProfiles() throws IOException {
        // Les Vegas
        AiProfile profile = new AiProfile();
        profile.setPlayerName("Les Vegas");
        profile.setAiType(MagicAIImpl.VEGAS);
        profile.setAiLevel(6);
        profile.save();
        setPlayerAvatar(profile, PlayerProfiles.AVATAR_LesVegas);
        // Mini Max
        profile = new AiProfile();
        profile.setPlayerName("Mini Max");
        profile.setAiType(MagicAIImpl.MMAB);
        profile.setAiLevel(6);
        profile.save();
        setPlayerAvatar(profile, PlayerProfiles.AVATAR_MiniMax);
        // Monty Carlo
        profile = new AiProfile();
        profile.setPlayerName("Monty Carlo");
        profile.setAiType(MagicAIImpl.MCTS);
        profile.setAiLevel(6);
        profile.save();
        setPlayerAvatar(profile, PlayerProfiles.AVATAR_MontyCarlo);
    }

    public static void setPlayerAvatar(final PlayerProfile profile, final Path avatarPath) {
        final Path targetPath = profile.getProfilePath().resolve("player.avatar");
        try {
            Files.copy(avatarPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerProfile getDefaultHumanPlayer() {
        return getHumanPlayerProfiles().values().iterator().next();
    }

    public static PlayerProfile getDefaultAiPlayer() {
        return getAiPlayerProfiles().values().iterator().next();
    }

    private static HashMap<String, PlayerProfile> getPlayerProfiles(final Class<? extends PlayerProfile> profileClass) {
        final HashMap<String, PlayerProfile> filteredProfiles = new HashMap<>();
        final Iterator<PlayerProfile> itr = profilesMap.values().iterator();
        while (itr.hasNext()) {
            final PlayerProfile profile = itr.next();
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
