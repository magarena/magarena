package magic.game.state;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.DuelPlayerConfig;

public final class GameStateSnapshot {
    private GameStateSnapshot() {}

    public static GameState getGameState(final MagicGame game) {

        final GameState gameState = new GameState();

        gameState.setDifficulty(game.getPlayer(1).getAiProfile().getAiLevel());
        // will always be 0 since it is not possible to save when AI has priority.
        gameState.setStartPlayerIndex(game.getPriorityPlayer().getIndex());

        // Save each player's state.
        for (int i = 0; i < game.getPlayers().length; i++) {
            saveGamePlayerState(i, gameState, game);
        }

        return gameState;
    }

    private static void saveGamePlayerState(final int playerIndex, final GameState gameState, final MagicGame game) {
        final MagicDuel duel = game.getDuel();
        final DuelPlayerConfig playerDef = duel.getPlayer(playerIndex);
        final GamePlayerState gamePlayerState = gameState.getPlayer(playerIndex);
        final MagicPlayer player = game.getPlayer(playerIndex);
        gamePlayerState.setName(playerDef.getName());
//        gamePlayerState.setFace(playerDef.getAvatar().getFace());
        gamePlayerState.setDeckProfileColors(playerDef.getDeckProfile().getColorText());
        if (player.isArtificial()) {
            gamePlayerState.setAiType(player.getAiProfile().getAiType().name());
        }
        gamePlayerState.setLife(player.getLife());
        savePlayerLibraryState(player, gamePlayerState);
        savePlayerHandState(player, gamePlayerState);
        savePlayerPermanentsState(player, gamePlayerState);
        savePlayerGraveyardState(player, gamePlayerState);
        savePlayerExiledState(player, gamePlayerState);
    }

    private static void savePlayerPermanentsState(final MagicPlayer player, final GamePlayerState gamePlayerState) {
        final Map<GameCardState, Integer> cards = new HashMap<>();
        for (final MagicPermanent card : player.getPermanents()) {
            final GameCardState tsCard = new GameCardState(card.getCardDefinition().getDistinctName(), 0, card.isTapped());
            updateCardCount2(tsCard, cards);
        }
        for (final GameCardState card : cards.keySet()) {
            gamePlayerState.addToPermanents(card.getCardName(), card.isTapped(), cards.get(card));
        }
    }

    private static void savePlayerGraveyardState(final MagicPlayer player, final GamePlayerState gamePlayerState) {
        final Map<MagicCardDefinition, Integer> cards = getZoneCardDefs(player.getGraveyard());
        for (MagicCardDefinition cardDef : cards.keySet()) {
            gamePlayerState.addToGraveyard(cardDef.getName(), cards.get(cardDef));
        }
    }

    private static void savePlayerHandState(final MagicPlayer player, final GamePlayerState gamePlayerState) {
        final Map<MagicCardDefinition, Integer> cards = getZoneCardDefs(player.getHand());
        for (MagicCardDefinition cardDef : cards.keySet()) {
            gamePlayerState.addToHand(cardDef.getName(), cards.get(cardDef));
        }
    }

    private static void savePlayerLibraryState(final MagicPlayer player, final GamePlayerState gamePlayerState) {
        final Map<MagicCardDefinition, Integer> cards = getZoneCardDefs(player.getLibrary());
        for (MagicCardDefinition cardDef : cards.keySet()) {
            gamePlayerState.addToLibrary(cardDef.getName(), cards.get(cardDef));
        }
    }

    private static void savePlayerExiledState(final MagicPlayer player, final GamePlayerState gamePlayerState) {
        final Map<MagicCardDefinition, Integer> cards = getZoneCardDefs(player.getExile());
        for (MagicCardDefinition cardDef : cards.keySet()) {
            gamePlayerState.addToExiled(cardDef.getName(), cards.get(cardDef));
        }
    }

    private static Map<MagicCardDefinition, Integer> getZoneCardDefs(final List<MagicCard> cards) {
        final Map<MagicCardDefinition, Integer> cardDefs = new HashMap<>();
        for (MagicCard card : cards) {
            updateCardCount(card.getCardDefinition(), cardDefs);
        }
        return cardDefs;
    }

    private static void updateCardCount(final MagicCardDefinition cardDef, final Map<MagicCardDefinition, Integer> cardDefs) {
        if (cardDefs.containsKey(cardDef)) {
            int count = cardDefs.get(cardDef);
            cardDefs.remove(cardDef);
            count++;
            cardDefs.put(cardDef, count);
        } else {
            cardDefs.put(cardDef, 1);
        }
    }
    private static void updateCardCount2(final GameCardState c, final Map<GameCardState, Integer> cards) {
        if (cards.containsKey(c)) {
            int count = cards.get(c);
            cards.remove(c);
            count++;
            cards.put(c, count);
        } else {
            cards.put(c, 1);
        }
    }

}
