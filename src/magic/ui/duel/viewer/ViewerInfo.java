package magic.ui.duel.viewer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicMessage;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerZone;
import magic.model.stack.MagicItemOnStack;

public class ViewerInfo {

    private final PlayerViewerInfo playerInfo;
    private final PlayerViewerInfo opponentInfo;
    private final List<StackViewerInfo> stack = new ArrayList<>();
    private final List<MagicMessage> log = new ArrayList<>(MAX_LOG);
        
    private static final int MAX_LOG = 50;

    public ViewerInfo(final MagicGame game) {
        
        final MagicPlayer player = game.getVisiblePlayer();
        playerInfo = new PlayerViewerInfo(game, player);
        opponentInfo = new PlayerViewerInfo(game, player.getOpponent());

        setStackViewerInfo(game);
        setLogBookViewerInfo(game);        
    }
    
    private void setStackViewerInfo(final MagicGame game) {
        for (final MagicItemOnStack itemOnStack : game.getStack()) {
            stack.add(new StackViewerInfo(game,itemOnStack));
        }        
    }

    /**
     * make a copy of the last MAX_LOG messages.
     */
    private void setLogBookViewerInfo(final MagicGame game) {
        int n = game.getLogBook().size();
        final Iterator<MagicMessage> iter = game.getLogBook().listIterator(Math.max(0, n - MAX_LOG));
        while (iter.hasNext()) {
            log.add(iter.next());
        }
    }

    public PlayerViewerInfo getPlayerInfo(final boolean opponent) {
        return opponent?opponentInfo:playerInfo;
    }

    public PlayerViewerInfo getAttackingPlayerInfo() {
        return playerInfo.turn?playerInfo:opponentInfo;
    }

    public PlayerViewerInfo getDefendingPlayerInfo() {
        return playerInfo.turn?opponentInfo:playerInfo;
    }

    public boolean isVisiblePlayer(final MagicPlayer player) {
        return playerInfo.player==player;
    }

    public List<StackViewerInfo> getStack() {
        return stack;
    }
    
    public List<MagicMessage> getLog() {
        return log;
    }

    public MagicCard getMagicCard(long magicCardId) {

        final PlayerViewerInfo[] players = new PlayerViewerInfo[] {playerInfo, opponentInfo};

        // first check permanents...
        MagicCard card = searchForCardInPermanents(magicCardId, players);
        if (card != MagicCard.NONE) {
            return card;
        }

        // ... then check stack...
        card = searchForCardOnStack(magicCardId);
        if (card != MagicCard.NONE) {
            return card;
        }

        // ... otherwise search through player zones in following order
        final MagicPlayerZone[] zones = new MagicPlayerZone[]{
            MagicPlayerZone.GRAVEYARD,
            MagicPlayerZone.EXILE,
            MagicPlayerZone.HAND,
            MagicPlayerZone.LIBRARY
        };

        for (MagicPlayerZone aZone : zones) {
            if (card == MagicCard.NONE) {
                card = searchForCardInZone(magicCardId, aZone, players);
            } else {
                break;
            }
        }

        return card;
    }

    private MagicCard searchForCardOnStack(long magicCardId) {
        for (StackViewerInfo item : stack) {
            if (item.itemOnStack.getSource() instanceof MagicCard) {
                final MagicCard card = (MagicCard) item.itemOnStack.getSource();
                if (card.getId() == magicCardId) {
                    return card;
                }
            }
        }
        return MagicCard.NONE;
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
    
    private MagicCard searchForCardInZone(long magicCardId, MagicPlayerZone zone, PlayerViewerInfo[] players) {
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

    private MagicCard searchForCardInPermanents(long magicCardId, PlayerViewerInfo[] players) {
        for (final PlayerViewerInfo player : players) {
            for (final PermanentViewerInfo info : player.permanents) {
                if (info.magicCardId == magicCardId) {
                    return info.permanent.getCard();
                }
            }
        }
        return MagicCard.NONE;
    }

}
