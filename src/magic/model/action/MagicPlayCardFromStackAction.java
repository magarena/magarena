package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.stack.MagicCardOnStack;

public class MagicPlayCardFromStackAction extends MagicPutIntoPlayAction {

    private final MagicCardOnStack cardOnStack;
    private boolean kicked=false;
    
    public MagicPlayCardFromStackAction(final MagicCardOnStack cardOnStack) {
        this.cardOnStack=cardOnStack;
    }
    
    public MagicPlayCardFromStackAction(final MagicCardOnStack cardOnStack,final MagicPermanent enchantedPermanent) {
        this.cardOnStack=cardOnStack;
        setEnchantedPermanent(enchantedPermanent);
    }
    
    public void setKicked(final boolean kicked) {
        this.kicked=kicked;
    }

    @Override
    protected MagicPermanent createPermanent(final MagicGame game) {
        final MagicPermanent permanent=game.createPermanent(cardOnStack.getCard(),cardOnStack.getController());
        if (kicked) {
            permanent.setState(MagicPermanentState.Kicked);
        }
        return permanent;
    }    
}
