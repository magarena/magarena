package magic.game.state;

import java.util.List;
import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;
import magic.test.TestGameBuilder;

public final class GameLoader {
    private GameLoader() {}

    public static MagicGame loadSavedGame(final String gameFile) {
        final GameState gameState = GameStateFileReader.loadGameStateFromFile(gameFile);
        final MagicDuel duel = getDuelState(gameState);
        final MagicGame game = getGameState(gameState, duel);
        return game;
    }

    private static MagicGame getGameState(final GameState gameState, final MagicDuel duel) {
        final MagicGame game = duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        setPlayerGameState(game.getPlayer(0), gameState.getPlayer(0), game);
        setPlayerGameState(game.getPlayer(1), gameState.getPlayer(1), game);
        return game;
    }

    private static MagicDuel getDuelState(final GameState gameState) {
        final MagicDuel duel = new MagicDuel();
        duel.setDifficulty(gameState.getDifficulty());
        final MagicDeckProfile deckProfile1 = new MagicDeckProfile(gameState.getPlayer(0).getDeckProfileColors());
        final MagicPlayerDefinition playerDef1 =
                new MagicPlayerDefinition(
                        gameState.getPlayer(0).getName(),
                        gameState.getPlayer(0).isAi(),
                        deckProfile1);
        final MagicDeckProfile deckProfile2 = new MagicDeckProfile(gameState.getPlayer(1).getDeckProfileColors());
        final MagicPlayerDefinition playerDef2 =
                new MagicPlayerDefinition(
                        gameState.getPlayer(1).getName(),
                        gameState.getPlayer(1).isAi(),
                        deckProfile2);
        duel.setPlayers(new MagicPlayerDefinition[]{playerDef1, playerDef2});
        duel.setStartPlayer(gameState.getStartPlayerIndex());
        // AI
        MagicAI ai1 = null;
        if (gameState.getPlayer(0).isAi()) {
            ai1 = MagicAIImpl.valueOf(gameState.getPlayer(0).getAiType()).getAI();
        }
        MagicAI ai2 = null;
        if (gameState.getPlayer(1).isAi()) {
            ai2 = MagicAIImpl.valueOf(gameState.getPlayer(1).getAiType()).getAI();
        }
        duel.setAIs(new MagicAI[]{ai1, ai2});
        return duel;
    }

    private static void setPlayerGameState(
            final MagicPlayer player,
            final GamePlayerState playerState,
            final MagicGame game) {

        player.setLife(playerState.getLife());

        // Library
        final List<GameCardState> library = playerState.getLibrary();
        for (GameCardState card : library) {
            TestGameBuilder.addToLibrary(player, card.getCardName(), card.getQuantity());
        }

        // Battlefield
        final List<GameCardState> permanents = playerState.getPermanents();
        for (GameCardState permanent : permanents) {
            TestGameBuilder.createPermanent(game, player, permanent.getCardName(), permanent.isTapped(), permanent.getQuantity());
        }

        // Hand
        final List<GameCardState> hand = playerState.getHand();
        for (GameCardState card : hand) {
            TestGameBuilder.addToHand(player, card.getCardName(), card.getQuantity());
        }

        // Graveyard
        final List<GameCardState> graveyard = playerState.getGraveyard();
        for (GameCardState card : graveyard) {
            TestGameBuilder.addToGraveyard(player, card.getCardName(), card.getQuantity());
        }

        // Exiled
        final List<GameCardState> exiled = playerState.getExiled();
        for (GameCardState card : exiled) {
            TestGameBuilder.addToExile(player, card.getCardName(), card.getQuantity());
        }
        
    }

}
