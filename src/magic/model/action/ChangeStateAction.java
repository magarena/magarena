package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.trigger.MagicTriggerType;

public class ChangeStateAction extends MagicAction {

    public final MagicPermanent permanent;
    public final MagicPermanentState state;
    private final boolean set;
    private boolean changed;

    public static ChangeStateAction Set(final MagicPermanent permanent,final MagicPermanentState state) {
        return new ChangeStateAction(permanent, state, true);
    }

    public static ChangeStateAction DoesNotUntapDuringNext(final MagicPermanent permanent, final MagicPlayer player) {
        if (player.getIndex() == 0) {
            return new ChangeStateAction(permanent, MagicPermanentState.DoesNotUntapDuringNext0, true);
        } else {
            return new ChangeStateAction(permanent, MagicPermanentState.DoesNotUntapDuringNext1, true);
        }
    }

    public static ChangeStateAction Clear(final MagicPermanent permanent,final MagicPermanentState state) {
        return new ChangeStateAction(permanent, state, false);
    }

    protected ChangeStateAction(final MagicPermanent aPermanent,final MagicPermanentState aState,final boolean aSet) {
        permanent = aPermanent;
        state = aState;
        set = aSet;
    }

    public static void trigger(final MagicGame game, final MagicPermanent perm, final MagicPermanentState state) {
        game.executeTrigger(
            MagicTriggerType.WhenBecomesState,
            ChangeStateAction.Set(perm, state)
        );
    }

    @Override
    public void doAction(final MagicGame game) {
        changed=permanent.hasState(state)!=set;
        if (changed) {
            if (set) {
                permanent.setState(state);
                game.executeTrigger(MagicTriggerType.WhenBecomesState, this);
            } else {
                permanent.clearState(state);
            }
            game.setStateCheckRequired();
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (changed) {
            if (set) {
                permanent.clearState(state);
            } else {
                permanent.setState(state);
            }
        }
    }
}
