package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTriggerType;

public class ChangeControlAction extends MagicAction {

    private final MagicPlayer curr;
    private final MagicPermanent perm;
    private final int score;

    public ChangeControlAction(final MagicPlayer curr, final MagicPermanent perm, final int score) {
        this.curr = curr;
        this.perm = perm;
        this.score = score;
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicPlayer prev = curr.getOpponent();

        // Execute trigger here so that full permanent state is preserved.
        game.executeTrigger(MagicTriggerType.WhenLoseControl, perm);

        prev.removePermanent(perm);
        curr.addPermanent(perm);

        perm.setState(MagicPermanentState.Summoned);
        game.doAction(new RemoveFromCombatAction(perm));
        game.doAction(ChangeStateAction.Clear(perm,MagicPermanentState.ExcludeFromCombat));

        if (perm.getPairedCreature().isValid()) {
            game.doAction(new SoulbondAction(perm,perm.getPairedCreature(),false));
        }

        setScore(curr, score + perm.getScore());

        game.checkUniquenessRule(perm);
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        final MagicPlayer prev = curr.getOpponent();
        curr.removePermanent(perm);
        prev.addPermanent(perm);
        perm.clearState(MagicPermanentState.Summoned);
    }

    @Override
    public String toString() {
        return super.toString() + " (" + curr + "," + perm + ')';
    }
}
