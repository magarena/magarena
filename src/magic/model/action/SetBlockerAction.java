package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

public class SetBlockerAction extends MagicAction {

    private final MagicPermanent attacker;
    private final MagicPermanent blocker;

    public SetBlockerAction(final MagicPermanent attacker,final MagicPermanent blocker) {
        this.attacker=attacker;
        this.blocker=blocker;
    }

    @Override
    public boolean isLegal(final MagicGame game) {
        return attacker.isValid() && blocker.isValid();
    }

    @Override
    public void doAction(final MagicGame game) {
        attacker.addBlockingCreature(blocker);
        blocker.setBlockedCreature(attacker);
        blocker.setState(MagicPermanentState.Blocking);
        if (!attacker.hasState(MagicPermanentState.Blocked)) {
            game.doAction(ChangeStateAction.Set(attacker, MagicPermanentState.Blocked));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        attacker.removeBlockingCreature(blocker);
        blocker.setBlockedCreature(MagicPermanent.NONE);
        blocker.clearState(MagicPermanentState.Blocking);
    }
}

