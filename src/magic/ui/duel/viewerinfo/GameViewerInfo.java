package magic.ui.duel.viewerinfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicMessage;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerZone;
import magic.model.phase.MagicPhaseType;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;

public class GameViewerInfo {

    private static final int MAX_LOG = 50;

    private final List<PlayerViewerInfo> players = new ArrayList<>();
    private final List<StackItemViewerInfo> stack = new ArrayList<>();
    private final List<MessageViewerInfo> log = new ArrayList<>(MAX_LOG);
    private final int turn;
    private final int gamesRequiredToWin;
    private final int gameNumber;
    private final int maxGames;
    private final MagicPhaseType phaseType;
    private final int undoPoints;

    public GameViewerInfo(final MagicGame game) {

        for (MagicPlayer mplayer : game.getPlayers()) {
            players.add(new PlayerViewerInfo(game, mplayer));
        }

        // TODO: MagicPlayer should be responsible for keeping track of games won.
        players.get(0).setGamesWon(game.getDuel().getGamesWon());
        players.get(1).setGamesWon(game.getDuel().getGamesPlayed() - game.getDuel().getGamesWon());

        turn = game.getTurn();
        gamesRequiredToWin = game.getDuel().getConfiguration().getGamesRequiredToWinDuel();
        gameNumber =  game.getDuel().getGameNr();
        maxGames = game.getDuel().getGamesTotal();
        phaseType = game.getPhase().getType();
        undoPoints = game.getNrOfUndoPoints();

        setStackViewerInfo(game);
        setLogBookViewerInfo(game);

    }

    public List<PlayerViewerInfo> getPlayers() {
        return players;
    }

    private void setStackViewerInfo(final MagicGame game) {
        for (final MagicItemOnStack itemOnStack : game.getStack()) {
            stack.add(new StackItemViewerInfo(game,itemOnStack));
        }
    }

    /**
     * make a copy of the last MAX_LOG messages.
     */
    private void setLogBookViewerInfo(final MagicGame game) {
        int n = game.getLogBook().size();
        final Iterator<MagicMessage> iter = game.getLogBook().listIterator(Math.max(0, n - MAX_LOG));
        while (iter.hasNext()) {
            log.add(new MessageViewerInfo(iter.next()));
        }
    }

    /**
     * This is the player (usually human but can be AI) whose cards
     * are shown by default in {@code PlayerZoneViewer}.
     */
    public PlayerViewerInfo getMainPlayer() {
        return players.get(0);
    }

    /**
     * The other player (usually AI) challenging the main player.
     */
    public PlayerViewerInfo getOpponent() {
        return players.stream()
            .filter(p -> p != getMainPlayer())
            .findFirst().get();
    }

    /**
     * The player whose turn it is.
     */
    public PlayerViewerInfo getTurnPlayer() {
        return players.stream()
            .filter(p -> p.isPlayerTurn())
            .findFirst().get();
    }

    /**
     * The player whose turn it is not.
     */
    public PlayerViewerInfo getNonTurnPlayer() {
        return players.stream()
            .filter(p -> p.isPlayerTurn() == false)
            .findFirst().get();
    }

    /**
     * The player who has priority.
     */
    public PlayerViewerInfo getPriorityPlayer() {
        return players.stream()
            .filter(p -> p.hasPriority())
            .findFirst().get();
    }

    public List<StackItemViewerInfo> getStack() {
        return stack;
    }

    public List<MessageViewerInfo> getLog() {
        return log;
    }

    public CardViewerInfo getCardViewerInfo(long magicCardId) {

        // first check permanents...
        final MagicPermanent perm = searchForCardInPermanents(magicCardId);
        if (perm != null) {
            return new CardViewerInfo(perm);
        }

        // ... then check stack...
        final MagicCardOnStack item = searchForCardOnStack(magicCardId);
        if (item != null) {
            return new CardViewerInfo(item);
        }

        // ... otherwise search through player zones in following order
        final MagicPlayerZone[] zones = new MagicPlayerZone[]{
            MagicPlayerZone.GRAVEYARD,
            MagicPlayerZone.EXILE,
            MagicPlayerZone.HAND,
            MagicPlayerZone.LIBRARY
        };

        MagicCard card = MagicCard.NONE;

        for (MagicPlayerZone aZone : zones) {
            if (card == MagicCard.NONE) {
                card = searchForCardInZone(magicCardId, aZone);
            } else {
                break;
            }
        }

        return new CardViewerInfo(card);
    }

    public CardViewerInfo getCardViewerInfo(MagicCard aCard) {
        CardViewerInfo info = getCardViewerInfo(aCard.getId());
        if (info.isEmpty()) {
            // A land card that has been played but not yet placed cannot be found
            // by its Id so so need to set specifically.
            info = new CardViewerInfo(aCard);
        }
        return info;
    }

    private MagicCardOnStack searchForCardOnStack(long magicCardId) {
        for (StackItemViewerInfo item : stack) {
            if (item.isMagicCard(magicCardId)) {
                return (MagicCardOnStack) item.itemOnStack;
            }
        }
        return null;
    }

    private MagicCardList getMagicCardList(MagicPlayerZone aZone, PlayerViewerInfo aPlayer) {
        switch (aZone) {
            case GRAVEYARD: return aPlayer.graveyard;
            case EXILE: return aPlayer.exile;
            case HAND: return aPlayer.hand;
            case LIBRARY: return aPlayer.library;
        }
        throw new RuntimeException("Invalid MagicPlayerZone : " + aZone);
    }

    private MagicCard searchForCardInZone(long magicCardId, MagicPlayerZone zone) {
        for (final PlayerViewerInfo player : players) {
            final MagicCardList cards = getMagicCardList(zone, player);
            for (final MagicCard card : cards) {
                if (card.getId() == magicCardId) {
                    return card;
                }
            }
        }
        return MagicCard.NONE;
    }

    private MagicPermanent searchForCardInPermanents(long magicCardId) {
        for (final PlayerViewerInfo player : players) {
            for (final PermanentViewerInfo info : player.permanents) {
                if (info.magicCardId == magicCardId) {
                    return info.permanent;
                }
            }
        }
        return null;
    }

    public int getTurn() {
        return turn;
    }

    public int getGamesRequiredToWinDuel() {
        return this.gamesRequiredToWin;
    }

    public int getGameNumber() {
        return this.gameNumber;
    }

    public int getMaxGames() {
        return this.maxGames;
    }

    public MagicPhaseType getPhaseType() {
        return this.phaseType;
    }

    public int getUndoPoints() {
        return this.undoPoints;
    }

    public PlayerViewerInfo getWinningPlayer() {
        return players.stream()
            .filter(p -> p.isWinner())
            .findFirst().get();
    }

}
