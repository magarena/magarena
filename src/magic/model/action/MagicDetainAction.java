package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicSetAbilityAction;
import magic.model.action.MagicChangeStateAction;

import java.util.Set;

public class MagicDetainAction extends MagicAction {

    private final MagicPermanent permanent;
    
    public MagicDetainAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }
        
    @Override
    public void doAction(final MagicGame game) {
        game.doAction(new MagicSetAbilityAction(permanent, MagicAbility.CannotAttackOrBlock));
        game.doAction(new MagicChangeStateAction(permanent, MagicPermanentState.LosesActivatedAbilities, true));
    }

    @Override
    public void undoAction(final MagicGame game) {
    }
}
