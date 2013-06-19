package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.stack.MagicCardOnStack;

public class MagicPlayCardFromStackAction extends MagicPutIntoPlayAction {

    private final MagicCardOnStack cardOnStack;
    
    public MagicPlayCardFromStackAction(final MagicCardOnStack aCardOnStack) {
        cardOnStack = aCardOnStack;
        setPayedCost(aCardOnStack.getPayedCost());
    }
    
    public MagicPlayCardFromStackAction(final MagicCardOnStack cardOnStack,final MagicPermanent enchantedPermanent) {
        this(cardOnStack);
        setEnchantedPermanent(enchantedPermanent);
    }

    @Override
    protected MagicPermanent createPermanent(final MagicGame game) {
        cardOnStack.setMoveLocation(MagicLocationType.Play);
        return game.createPermanent(cardOnStack.getCard(),cardOnStack.getController());
    }    
}
