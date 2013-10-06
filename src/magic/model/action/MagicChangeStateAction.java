package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.trigger.MagicTriggerType;

public class MagicChangeStateAction extends MagicAction {

    public final MagicPermanent permanent;
    public final MagicPermanentState state;
    private final boolean set;
    private boolean changed;

    public static MagicChangeStateAction Set(final MagicPermanent permanent,final MagicPermanentState state) {
        return new MagicChangeStateAction(permanent, state, true);
    }

    public static MagicChangeStateAction Clear(final MagicPermanent permanent,final MagicPermanentState state) {
        return new MagicChangeStateAction(permanent, state, false);
    }

    protected MagicChangeStateAction(final MagicPermanent aPermanent,final MagicPermanentState aState,final boolean aSet) {
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
