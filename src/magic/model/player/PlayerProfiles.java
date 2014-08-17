package magic.model.player;

import magic.MagicMain;
import magic.ai.MagicAIImpl;
import magic.utility.MagicFileSystem;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class PlayerProfiles {
    private PlayerProfiles() {}

    // default AI avatars.
    private static final Path AVATARS_PATH = Paths.get(MagicMain.getAvatarSetsPath());
    private static final Path AVATAR_LesVegas = AVATARS_PATH.resolve("tux").resolve("tux25.png");
    private static final Path AVATAR_MontyCarlo = AVATARS_PATH.resolve("default").resolve("face09.png");
    private static final Path AVATAR_MiniMax = AVATARS_PATH.resolve("default").resolve("face18.png");

    private static final Path profilesPath = Paths.get(MagicMain.getPlayerProfilesPath());
    private static final HashMap<String, PlayerProfile> profilesMap = new HashMap<String, PlayerProfile>();

    public static void refreshMap() {
        setProfilesMap();
    }

    private static void setProfilesMap() {
        profilesMap.clear();
        // Humans
        for (Path path : getProfilePaths("human")) {
            final String profileId = path.getFileName().toString();
            final HumanPlayer player = new HumanPlayer(profileId);
            profilesMap.put(profileId, player);
        }
        // AIs
        for (Path path : getProfilePaths("ai")) {
            final String profileId = path.getFileName().toString();
            final AiPlayer player = new AiPlayer(profileId);
            profilesMap.put(profileId, player);
        }
    }

    private static List<Path> getProfilePaths(final String playerType) {
        final Path playersPath = profilesPath.resolve(playerType);
        List<Path> profilePaths = getDirectoryPaths(playersPath);
        if (profilePaths.size() == 0) {
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
        final List<Path> paths = new ArrayList<Path>();
        if (Files.exists(rootPath)) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(rootPath, new DirectoriesFilter())) {
                for (Path p : ds) {
                    paths.add(p.getFileName());
                }
            } catch (IOException e) {
                e.printStackTrace();
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
        final HumanPlayer profile = new HumanPlayer();
        profile.setPlayerName(getDefaultPlayerProfileName());
        profile.save();
    }

    private static String getDefaultPlayerProfileName() {
        final String systemUserName = System.getProperty("user.name");
        return systemUserName == null ? "Player" : systemUserName;

    }

    private static void createDefaultAiPlayerProfiles() throws IOException {
        // Les Vegas
        AiPlayer profile = new AiPlayer();
        profile.setPlayerName("Les Vegas");
        profile.setAiType(MagicAIImpl.VEGAS);
        profile.setAiLevel(6);
        profile.save();
        setPlayerAvatar(profile, PlayerProfiles.AVATAR_LesVegas);
        // Mini Max
        profile = new AiPlayer();
        profile.setPlayerName("Mini Max");
        profile.setAiType(MagicAIImpl.MMAB);
        profile.setAiLevel(6);
        profile.save();
        setPlayerAvatar(profile, PlayerProfiles.AVATAR_MiniMax);
        // Monty Carlo
        profile = new AiPlayer();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static PlayerProfile getDefaultHumanPlayer() {
        return getHumanPlayerProfiles().values().iterator().next();
    }

    public static PlayerProfile getDefaultAiPlayer() {
        return getAiPlayerProfiles().values().iterator().next();
    }

    public static HashMap<String, PlayerProfile> getHumanPlayerProfiles() {
        final HashMap<String, PlayerProfile> filteredProfiles = new HashMap<String, PlayerProfile>();
        final Iterator<PlayerProfile> itr = profilesMap.values().iterator();
        while (itr.hasNext()) {
            final PlayerProfile profile = itr.next();
            if (profile instanceof HumanPlayer) {
                filteredProfiles.put(profile.getId(), profile);
            }
        }
        return filteredProfiles;
    }

    public static HashMap<String, PlayerProfile> getAiPlayerProfiles() {
        final HashMap<String, PlayerProfile> filteredProfiles = new HashMap<String, PlayerProfile>();
        final Iterator<PlayerProfile> itr = profilesMap.values().iterator();
        while (itr.hasNext()) {
            final PlayerProfile profile = itr.next();
            if (profile instanceof AiPlayer) {
                filteredProfiles.put(profile.getId(), profile);
            }
        }
        return filteredProfiles;
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
        return Paths.get(MagicMain.getPlayerProfilesPath())
               .resolve(playerProfile.getPlayerType())
               .resolve(playerProfile.getId());
    }



}
