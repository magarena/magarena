package magic.ui.screen.deck.editor.stats;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import magic.model.MagicDeck;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.data.stats.GameStatsInfo;


class DeckGameStats {

    private final GameStatsInfo dto;
    private final MagicDeck deck;

    DeckGameStats(MagicDeck deck, GameStatsInfo dto) {
        this.deck = deck;
        this.dto = dto;
    }

    private LocalDateTime getLocalTimeFromEpoch(Long epochMilli) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochMilli),
            ZoneId.systemDefault()
        );
    }

    String getGameInfo() {
        LocalDateTime ldt = getLocalTimeFromEpoch(dto.timeStart);
        String start = ldt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
        return start + "<br>version " + dto.magarenaVersion;
    }

    String getOpponentInfo() {
        boolean isPlayerDeck = dto.player1DeckName.equals(deck.getName());
        String playerProfileId = isPlayerDeck ? dto.player2ProfileId : dto.player1ProfileId;
        String aiType = isPlayerDeck ? dto.player2AiType : dto.player1AiType;
        int aiLevel = isPlayerDeck ? dto.player2AiLevel : dto.player1AiLevel;
        int aiXLife = isPlayerDeck ? dto.player2AiXtraLife : dto.player1AiXtraLife;
        String aiInfo = aiType != null ? aiType + "<br>" + "level: " + aiLevel + ", +life: " + aiXLife : "";
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
        String aiInfo = aiType != null ? aiType + "<br>" + "level: " + aiLevel + ", +life: " + aiXLife : "";
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
        return (winningPlayer == playerNum ? "won in " : "lost in ")
            + dto.turns + (dto.isConceded ? " (conceded)" : "");
    }

    String getConfigInfo() {
        return "life: " + dto.startLife + "<br>hand :" + dto.startHandSize;
    }

}
