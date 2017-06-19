package magic.game.state;

import java.io.File;
import java.util.List;
import magic.ai.MagicAIImpl;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;
import magic.model.player.AiProfile;
import magic.model.player.HumanProfile;
import magic.model.player.PlayerProfile;
import magic.test.TestGameBuilder;

public final class GameLoader {
    private GameLoader() {}

    public static MagicGame loadSavedGame(final File gameFile) {
        final GameState gameState = GameStateFileReader.loadGameStateFromFile(gameFile);
        final MagicDuel duel = getDuelState(gameState);
        final MagicGame game = getGameState(gameState, duel);
        return game;
    }

    private static MagicGame getGameState(final GameState gameState, final MagicDuel duel) {
        final MagicGame game = duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        setPlayerGameState(game.getPlayer(0), gameState.getPlayer(0));
        setPlayerGameState(game.getPlayer(1), gameState.getPlayer(1));
        return game;
    }

    private static MagicDuel getDuelState(final GameState gameState) {
        final MagicDuel duel = new MagicDuel();
        final DuelPlayerConfig[] playerDefs = new DuelPlayerConfig[2];

        for (int i = 0; i < playerDefs.length; i++) {
            final PlayerProfile pp = gameState.getPlayer(i).isAi() ?
                AiProfile.create(
                    gameState.getPlayer(i).getName(),
                    MagicAIImpl.valueOf(gameState.getPlayer(i).getAiType()),
                    gameState.getDifficulty()
                ) :
                HumanProfile.create(
                    gameState.getPlayer(i).getName()
                );
            final MagicDeckProfile deckProfile = new MagicDeckProfile(gameState.getPlayer(i).getDeckProfileColors());
            playerDefs[i] = new DuelPlayerConfig(pp, deckProfile);
        }

        duel.setPlayers(playerDefs);
        duel.setStartPlayer(gameState.getStartPlayerIndex());
        return duel;
    }

    private static void setPlayerGameState(final MagicPlayer player, final GamePlayerState playerState) {

        player.setLife(playerState.getLife());

        // Library
        final List<GameCardState> library = playerState.getLibrary();
        for (GameCardState card : library) {
            TestGameBuilder.addToLibrary(player, card.getCardName(), card.getQuantity());
        }

        // Battlefield
        final List<GameCardState> permanents = playerState.getPermanents();
        for (GameCardState permanent : permanents) {
            TestGameBuilder.createPermanent(player, permanent.getCardName(), permanent.isTapped(), permanent.getQuantity());
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
