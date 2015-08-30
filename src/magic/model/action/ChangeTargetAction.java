package magic.model.action;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetNone;

public class ChangeTargetAction extends MagicAction {

    private final MagicItemOnStack item;
    private final MagicTarget target;
    private Object oldTarget = MagicTargetNone.getInstance();

    public ChangeTargetAction(final MagicItemOnStack aItem,final MagicTarget aTarget) {
        item = aItem;
        target = aTarget;
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicEvent event = item.getEvent();
        if (game.isLegalTarget(event.getPlayer(), event.getSource(), event.getTargetChoice(), target)) {
            final int idx = event.getTargetChoiceResultIndex();
            oldTarget = item.getChoiceResults()[idx];
            item.getChoiceResults()[idx] = target;
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (oldTarget != MagicTargetNone.getInstance()) {
            final int idx = item.getEvent().getTargetChoiceResultIndex();
            item.getChoiceResults()[idx] = oldTarget;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" ("+item.getName()+','+target.getName()+')';
    }
}
