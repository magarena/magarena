package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

public class MagicCleanupPermanentAction extends MagicAction {

    private final MagicPermanent permanent;
    private int oldAbilityPlayedThisTurn;
    private int oldDamage;
    private int oldPreventDamage;
    private int oldStateFlags;

    public MagicCleanupPermanentAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        oldAbilityPlayedThisTurn=permanent.getAbilityPlayedThisTurn();
        permanent.setAbilityPlayedThisTurn(0);
        oldStateFlags=permanent.getStateFlags();
        permanent.setStateFlags(oldStateFlags & MagicPermanentState.CLEANUP_MASK);
        oldDamage=permanent.getDamage();
        permanent.setDamage(0);
        oldPreventDamage=permanent.getPreventDamage();
        permanent.setPreventDamage(0);
    }

    @Override
    public void undoAction(final MagicGame game) {
        permanent.setAbilityPlayedThisTurn(oldAbilityPlayedThisTurn);
        permanent.setDamage(oldDamage);
        permanent.setPreventDamage(oldPreventDamage);
        permanent.setStateFlags(oldStateFlags);
    }
}
