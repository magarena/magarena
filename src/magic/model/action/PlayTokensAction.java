package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicObject;
import magic.model.MagicPlayer;

public class PlayTokensAction extends MagicAction {

    private final MagicCard card;
    private final int count;

    public PlayTokensAction(final MagicPlayer player, final MagicCardDefinition cardDefinition, final int aCount) {
        card  = MagicCard.createTokenCard(cardDefinition,player);
        count = aCount;
    }
    
    public PlayTokensAction(final MagicPlayer player, final MagicObject obj, final int aCount) {
        this(player, obj.getCardDefinition(), aCount);
    }

    @Override
    public void doAction(final MagicGame game) {
        for (int i = 0; i < count; i++) {
            game.doAction(new PlayTokenAction(card));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        //empty
    }
}
