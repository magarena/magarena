package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

import java.util.List;

public class MagicDiscardCardsAction extends MagicAction {

    private final MagicPlayer player;
    private final List<MagicCard> cards;
    private final MagicLocationType toLocation;
    
    public MagicDiscardCardsAction(final MagicPlayer aPlayer,final List<MagicCard> aCards) {
        this(aPlayer, aCards, MagicLocationType.Graveyard);
    }

    public MagicDiscardCardsAction(final MagicPlayer aPlayer,final List<MagicCard> aCards, final MagicLocationType aToLocation) {
        player = aPlayer;
        cards = aCards;
        toLocation = aToLocation;
    }

    @Override
    public void doAction(final MagicGame game) {
        for (final MagicCard card : cards) {
            game.doAction(new MagicDiscardCardAction(player,card,toLocation));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
    }
}
