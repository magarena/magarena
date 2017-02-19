package magic.ui.screen.deck.editor.stats;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import magic.data.stats.GameStatsInfo;
import magic.model.MagicDeck;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.translate.MText;


class DeckGameStats {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "version";
    private static final String _S2 = "<br>level: %d, +life: %d";
    private static final String _S3 = "life: %d<br>hand: %d";
    private static final String _S4 = "won in";
    private static final String _S5 = "lost in";
    private static final String _S6 = "conceded";

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
        return start + String.format("<br>%s %s", MText.get(_S1), dto.magarenaVersion);
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
