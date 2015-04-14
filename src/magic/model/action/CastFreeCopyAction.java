package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicObject;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;

public class CastFreeCopyAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicCardDefinition cdef;

    public CastFreeCopyAction(final MagicPlayer aPlayer,final MagicObject obj) {
        this(aPlayer, obj.getCardDefinition());
    }
    
    public CastFreeCopyAction(final MagicPlayer aPlayer,final MagicCardDefinition aCdef) {
        player = aPlayer;
        cdef = aCdef;
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardOnStack cardOnStack = new MagicCardOnStack(
            MagicCard.createTokenCard(cdef,player),
            player,
            MagicPayedCost.NO_COST
        );
        cardOnStack.setFromLocation(MagicLocationType.Exile);
        game.doAction(new PutItemOnStackAction(cardOnStack));
    }

    @Override
    public void undoAction(final MagicGame game) {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" (" +player + ',' + cdef +')';
    }
}
