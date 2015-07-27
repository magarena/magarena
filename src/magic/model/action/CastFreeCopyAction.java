package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicObject;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPutCardOnStackEvent;

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
        final MagicCard source = MagicCard.createTokenCard(cdef,player);
        for (final MagicEvent event : cdef.getAdditionalCostEvent(source)) {
            if (event.isSatisfied() == false) {
                game.logAppendMessage(player, "Casting failed as " + player + " is unable to pay additional casting costs.");
                return;
            }
        }
        for (final MagicEvent event : cdef.getAdditionalCostEvent(source)) {
            game.addEvent(event);
        }
        game.addEvent(new MagicPutCardOnStackEvent(source, player, MagicLocationType.Exile));
    }

    @Override
    public void undoAction(final MagicGame game) {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" (" +player + ',' + cdef +')';
    }
}
