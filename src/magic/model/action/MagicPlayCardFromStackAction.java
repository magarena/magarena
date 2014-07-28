package magic.model.action;

import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicObject;
import magic.model.MagicPermanent;
import magic.model.stack.MagicCardOnStack;

import java.util.Arrays;

public class MagicPlayCardFromStackAction extends MagicPutIntoPlayAction {

    private final MagicCardOnStack cardOnStack;
    private final MagicCardDefinition cardDef;

    public MagicPlayCardFromStackAction(final MagicCardOnStack aCardOnStack, final MagicCardDefinition aCardDef, final MagicPermanentAction... aModifications) {
        cardOnStack = aCardOnStack;
        cardDef = aCardDef; 
        setPayedCost(aCardOnStack.getPayedCost());
        setModifications(Arrays.asList(aModifications));
    }
    
    public MagicPlayCardFromStackAction(final MagicCardOnStack aCardOnStack, final MagicPermanentAction... aModifications) {
        this(aCardOnStack, aCardOnStack.getCardDefinition(), aModifications);
    }
    
    public MagicPlayCardFromStackAction(final MagicCardOnStack cardOnStack, final MagicPermanent enchantedPermanent, final MagicPermanentAction... aModifications) {
        this(cardOnStack, aModifications);
        setEnchantedPermanent(enchantedPermanent);
    }

    static final public MagicPlayCardFromStackAction EnterAsCopy(final MagicCardOnStack cardOnStack, final MagicObject obj) {
        return new MagicPlayCardFromStackAction(cardOnStack, obj.getCardDefinition());
    }

    @Override
    protected MagicPermanent createPermanent(final MagicGame game) {
        cardOnStack.setMoveLocation(MagicLocationType.Play);
        return game.createPermanent(cardOnStack.getCard(),cardDef,cardOnStack.getController());
    }
}
