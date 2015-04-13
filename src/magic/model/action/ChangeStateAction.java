package magic.model.action;

import magic.model.MagicGame;
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

    public static ChangeStateAction Clear(final MagicPermanent permanent,final MagicPermanentState state) {
        return new ChangeStateAction(permanent, state, false);
    }

    protected ChangeStateAction(final MagicPermanent aPermanent,final MagicPermanentState aState,final boolean aSet) {
        permanent = aPermanent;
        state = aState;
        set = aSet;
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
