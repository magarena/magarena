package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.stack.MagicCardOnStack;

public class MagicPlayCardFromStackAction extends MagicPutIntoPlayAction {

    private final MagicCardOnStack cardOnStack;
    private final int kicked;
    
    public MagicPlayCardFromStackAction(final MagicCardOnStack aCardOnStack, final int aKicked) {
        cardOnStack = aCardOnStack;
        kicked = aKicked;
    }
    
    public MagicPlayCardFromStackAction(final MagicCardOnStack cardOnStack) {
        this(cardOnStack, 0);
    }
    
    public MagicPlayCardFromStackAction(final MagicCardOnStack cardOnStack,final MagicPermanent enchantedPermanent) {
        this(cardOnStack, 0);
        setEnchantedPermanent(enchantedPermanent);
    }

    @Override
    protected MagicPermanent createPermanent(final MagicGame game) {
        final MagicPermanent permanent=game.createPermanent(cardOnStack.getCard(),cardOnStack.getController());
        permanent.setKicked(kicked);
        return permanent;
    }    
}
