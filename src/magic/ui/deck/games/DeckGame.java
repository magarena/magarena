package magic.ui.deck.games;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import magic.data.stats.GameStatsInfo;
import magic.model.MagicDeck;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.translate.MText;

class DeckGame {

    // translatable UI text (prefix with _S).
    private static final String _S2 = "<br>level: %d, +life: %d";
    private static final String _S3 = "life: %d • hand: %d";
    private static final String _S4 = "won in";
    private static final String _S5 = "lost in";
    private static final String _S6 = "conceded";
    private static final String _S7 = "%d years ago";
    private static final String _S8 = "%d year ago";
    private static final String _S9 = "%d months ago";
    private static final String _S10 = "%d month ago";
    private static final String _S11 = "%d weeks ago";
    private static final String _S12 = "%d week ago";
    private static final String _S13 = "%d days ago";
    private static final String _S14 = "%d day ago";
    private static final String _S15 = "%d hours ago";
    private static final String _S16 = "%d hour ago";
    private static final String _S17 = "%d minutes ago";
    private static final String _S18 = "%d minute ago";
    private static final String _S19 = "a few seconds ago";

    private final GameStatsInfo dto;
    private final MagicDeck deck;

    DeckGame(MagicDeck deck, GameStatsInfo dto) {
        this.deck = deck;
        this.dto = dto;
    }

    private LocalDateTime getLocalTimeFromEpoch(Long epochMilli) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochMilli),
            ZoneId.systemDefault()
        );
    }

    String getGamePeriod() {

        LocalDateTime timeStart = getLocalTimeFromEpoch(dto.timeStart);
        LocalDateTime timeEnd = LocalDateTime.now();

        long years = ChronoUnit.YEARS.between(timeStart, timeEnd);
        if (years > 0) {
            return years > 1 ? MText.get(_S7, years): MText.get(_S8, years);
        }
        long months = ChronoUnit.MONTHS.between(timeStart, timeEnd);
        if (months > 0) {
            return months > 1 ? MText.get(_S9, months): MText.get(_S10, months);
        }
        long weeks = ChronoUnit.WEEKS.between(timeStart, timeEnd);
        if (weeks > 0) {
            return weeks > 1 ? MText.get(_S11, weeks): MText.get(_S12, weeks);
        }
        long days = ChronoUnit.DAYS.between(timeStart, timeEnd);
        if (days > 0) {
            return days > 1 ? MText.get(_S13, days): MText.get(_S14, days);
        }
        long hours = ChronoUnit.HOURS.between(timeStart, timeEnd);
        if (hours > 0) {
            return hours > 1 ? MText.get(_S15, hours): MText.get(_S16, hours);
        }
        long minutes = ChronoUnit.MINUTES.between(timeStart, timeEnd);
        if (minutes > 0) {
            return minutes > 1 ? MText.get(_S17, minutes): MText.get(_S18, minutes);
        }
        return _S19;
    }

    String getOpponentInfo() {
        boolean isPlayerDeck = dto.player1DeckName.equals(deck.getName());
        String playerProfileId = isPlayerDeck ? dto.player2ProfileId : dto.player1ProfileId;
        String aiType = isPlayerDeck ? dto.player2AiType : dto.player1AiType;
        int aiLevel = isPlayerDeck ? dto.player2AiLevel : dto.player1AiLevel;
        int aiXLife = isPlayerDeck ? dto.player2AiXtraLife : dto.player1AiXtraLife;
        String aiInfo = aiType != null
            ? aiType + MText.get(_S2, aiLevel, aiXLife)
            : "";
        PlayerProfile player = PlayerProfiles.getPlayerProfiles().get(playerProfileId);
        player = player != null && player.isArtificial() ? null : player;
        boolean isTempPlayer = player == null;
        return isTempPlayer
            ? aiInfo
            : player.getPlayerName() + "<br>" + (!aiInfo.isEmpty() ? "(" + aiInfo + ")" : "<br>");
    }

    DeckInfo getOpponentDeckInfo() {
        return dto.player1DeckName.equals(deck.getName())
            ? new DeckInfo(dto.player2DeckName, dto.player2DeckColor)
            : new DeckInfo(dto.player1DeckName, dto.player1DeckColor);
    }

    String getPlayerInfo() {
        boolean isPlayerDeck = dto.player1DeckName.equals(deck.getName());
        String playerProfileId = isPlayerDeck ? dto.player1ProfileId : dto.player2ProfileId;
        String aiType = isPlayerDeck ? dto.player1AiType : dto.player2AiType;
        int aiLevel = isPlayerDeck ? dto.player1AiLevel : dto.player2AiLevel;
        int aiXLife = isPlayerDeck ? dto.player1AiXtraLife : dto.player2AiXtraLife;
        String aiInfo = aiType != null
            ? aiType + MText.get(_S2, aiLevel, aiXLife)
            : "";
        PlayerProfile player = PlayerProfiles.getPlayerProfiles().get(playerProfileId);
        player = player != null && player.isArtificial() ? null : player;
        boolean isTempPlayer = player == null;
        return isTempPlayer
            ? aiInfo
            : player.getPlayerName() + "<br>" + (!aiInfo.isEmpty() ? "(" + aiInfo + ")" : "<br>");
    }

    String getResultInfo() {
        int playerNum = dto.player1DeckName.equals(deck.getName()) ? 1 : 2;
        int winningPlayer = Integer.parseInt(dto.winningPlayerProfile);
        return String.format("%s %d %s",
            winningPlayer == playerNum ? MText.get(_S4) : MText.get(_S5),
            dto.turns,
            dto.isConceded ? "(" + MText.get(_S6) + ")" : ""
        ).trim();
    }

    String getConfigInfo() {
        return MText.get(_S3, dto.startLife, dto.startHandSize);
    }

}
