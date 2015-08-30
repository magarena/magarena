package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class DiscardCardAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicCard card;
    private final MagicLocationType toLocation;
    private int index;
    
    public DiscardCardAction(final MagicPlayer aPlayer,final MagicCard aCard) {
        this(aPlayer, aCard, MagicLocationType.Graveyard);
    }

    public DiscardCardAction(final MagicPlayer aPlayer,final MagicCard aCard, final MagicLocationType aToLocation) {
        player = aPlayer;
        card = aCard;
        toLocation = aToLocation;
    }

    @Override
    public void doAction(final MagicGame game) {
        index = player.removeCardFromHand(card);
        if (index >= 0) {
            setScore(player,-ArtificialScoringSystem.getCardScore(card));
            game.doAction(new MoveCardAction(card,MagicLocationType.OwnersHand,toLocation));
            game.setStateCheckRequired();
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (index >= 0) {
            player.addCardToHand(card,index);
        }
    }
}
