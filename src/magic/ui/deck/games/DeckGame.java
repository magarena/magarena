package magic.ui.deck.games;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import magic.data.DeckType;
import magic.data.stats.GameStatsInfo;
import magic.model.MagicDeck;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.translate.MText;

class DeckGame {

    // translatable UI text (prefix with _S).
    private static final String _S3 = "life: %d • hand: %d";
    private static final String _S4 = "won in";
    private static final String _S5 = "lost in";
    private static final String _S6 = "conceded";
    private static final String _S7 = "%d years ago";
    private static final String _S8 = "a year ago";
    private static final String _S9 = "%d months ago";
    private static final String _S10 = "a month ago";
    private static final String _S11 = "%d weeks ago";
    private static final String _S12 = "a week ago";
    private static final String _S13 = "%d days ago";
    private static final String _S14 = "a day ago";
    private static final String _S15 = "%d hours ago";
    private static final String _S16 = "an hour ago";
    private static final String _S17 = "%d minutes ago";
    private static final String _S18 = "a minute ago";
    private static final String _S19 = "a few seconds ago";

    private final GameStatsInfo gameInfo;
    private final MagicDeck deck;

    DeckGame(MagicDeck deck, GameStatsInfo dto) {
        this.deck = deck;
        this.gameInfo = dto;
    }

    private LocalDateTime getLocalTimeFromEpoch(Long epochMilli) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochMilli),
            ZoneId.systemDefault()
        );
    }

    String getGamePeriod() {

        LocalDateTime timeStart = getLocalTimeFromEpoch(gameInfo.timeStart);
        LocalDateTime timeEnd = LocalDateTime.now();

        long years = ChronoUnit.YEARS.between(timeStart, timeEnd);
        if (years > 0) {
            return years > 1 ? MText.get(_S7, years): MText.get(_S8);
        }
        long months = ChronoUnit.MONTHS.between(timeStart, timeEnd);
        if (months > 0) {
            return months > 1 ? MText.get(_S9, months): MText.get(_S10);
        }
        long weeks = ChronoUnit.WEEKS.between(timeStart, timeEnd);
        if (weeks > 0) {
            return weeks > 1 ? MText.get(_S11, weeks): MText.get(_S12);
        }
        long days = ChronoUnit.DAYS.between(timeStart, timeEnd);
        if (days > 0) {
            return days > 1 ? MText.get(_S13, days): MText.get(_S14);
        }
        long hours = ChronoUnit.HOURS.between(timeStart, timeEnd);
        if (hours > 0) {
            return hours > 1 ? MText.get(_S15, hours): MText.get(_S16);
        }
        long minutes = ChronoUnit.MINUTES.between(timeStart, timeEnd);
        if (minutes > 0) {
            return minutes > 1 ? MText.get(_S17, minutes): MText.get(_S18);
        }
        return _S19;
    }

    private boolean isPlayer1Deck() {
        return gameInfo.player1DeckName.equals(deck.getName())
            && DeckType.valueOf(gameInfo.player1DeckType) == deck.getDeckType();
    }

    /**
     * if this deck belongs to player 1 then the opponent is player 2 otherwise
     * the deck belongs to player 2 in which case the opponent is player1.
     */
    PlayerInfo getOpponentInfo() {
        PlayerInfo playerInfo = new PlayerInfo();
        boolean isPlayer1Deck = isPlayer1Deck();
        playerInfo.setAiType(isPlayer1Deck ? gameInfo.player2AiType : gameInfo.player1AiType);
        playerInfo.setAiLevel(isPlayer1Deck ? gameInfo.player2AiLevel : gameInfo.player1AiLevel);
        playerInfo.setAiXtraLife(isPlayer1Deck ? gameInfo.player2AiXtraLife : gameInfo.player1AiXtraLife);
        String playerProfileId = isPlayer1Deck ? gameInfo.player2ProfileId : gameInfo.player1ProfileId;
        PlayerProfile player = PlayerProfiles.getPlayerProfiles().get(playerProfileId);
        playerInfo.setHumanPlayerProfile(player != null && player.isArtificial() ? null : player);
        return playerInfo;
    }

    /**
     * get the player of this deck. Could be player 1 or player 2.
     */
    PlayerInfo getDeckPlayerInfo() {
        PlayerInfo playerInfo = new PlayerInfo();
        boolean isPlayer1Deck = isPlayer1Deck();
        playerInfo.setAiType(isPlayer1Deck ? gameInfo.player1AiType : gameInfo.player2AiType);
        playerInfo.setAiLevel(isPlayer1Deck ? gameInfo.player1AiLevel : gameInfo.player2AiLevel);
        playerInfo.setAiXtraLife(isPlayer1Deck ? gameInfo.player1AiXtraLife : gameInfo.player2AiXtraLife);
        String playerProfileId = isPlayer1Deck ? gameInfo.player1ProfileId : gameInfo.player2ProfileId;
        PlayerProfile player = PlayerProfiles.getPlayerProfiles().get(playerProfileId);
        playerInfo.setHumanPlayerProfile(player != null && player.isArtificial() ? null : player);
        return playerInfo;
    }

    DeckInfo getOpponentDeckInfo() {
        return gameInfo.player1DeckName.equals(deck.getName())
            ? new DeckInfo(gameInfo.player2DeckName, gameInfo.player2DeckColor)
            : new DeckInfo(gameInfo.player1DeckName, gameInfo.player1DeckColor);
    }

    String getResultInfo() {
        int playerNum = gameInfo.player1DeckName.equals(deck.getName()) ? 1 : 2;
        int winningPlayer = Integer.parseInt(gameInfo.winningPlayerProfile);
        return String.format("%s %d %s",
            winningPlayer == playerNum ? MText.get(_S4) : MText.get(_S5),
            gameInfo.turns,
            gameInfo.isConceded ? "(" + MText.get(_S6) + ")" : ""
        ).trim();
    }

    String getConfigInfo() {
        return MText.get(_S3, gameInfo.startLife, gameInfo.startHandSize);
    }

}
