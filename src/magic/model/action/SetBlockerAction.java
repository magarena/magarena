package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.trigger.MagicTriggerType;

public class SetBlockerAction extends MagicAction {

    private final MagicPermanent attacker;
    private final MagicPermanent blocker;
    private boolean unblocked;

    public SetBlockerAction(final MagicPermanent attacker,final MagicPermanent blocker) {
        this.attacker=attacker;
        this.blocker=blocker;
    }

    @Override
    public void doAction(final MagicGame game) {
        attacker.addBlockingCreature(blocker);
        blocker.setBlockedCreature(attacker);
        game.doAction(ChangeStateAction.Set(blocker,MagicPermanentState.Blocking));
        unblocked=!attacker.hasState(MagicPermanentState.Blocked);
        if (unblocked) {
            game.doAction(ChangeStateAction.Set(attacker, MagicPermanentState.Blocked));
            game.executeTrigger(MagicTriggerType.WhenBecomesBlocked, attacker);
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        attacker.removeBlockingCreature(blocker);
        blocker.setBlockedCreature(MagicPermanent.NONE);
        game.doAction(ChangeStateAction.Clear(blocker, MagicPermanentState.Blocking));
        if (unblocked) {
            game.doAction(ChangeStateAction.Clear(attacker, MagicPermanentState.Blocked));
        }
    }
}

