package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;

public class MagicPlayCardFromStackAction extends MagicPutIntoPlayAction {

    private final MagicCardOnStack cardOnStack;
    private final int kicker;
    
    public MagicPlayCardFromStackAction(final MagicCardOnStack aCardOnStack, final int aKicker) {
        cardOnStack = aCardOnStack;
        kicker = aKicker;
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
        cardOnStack.setMoveLocation(MagicLocationType.Play);
        final MagicPermanent permanent=game.createPermanent(cardOnStack.getCard(),cardOnStack.getController());
        permanent.setKicker(kicker);
        return permanent;
    }    
}
