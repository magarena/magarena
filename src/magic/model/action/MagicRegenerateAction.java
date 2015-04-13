package magic.model.action;

import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

public class MagicRegenerateAction extends ChangeStateAction {
    public MagicRegenerateAction(final MagicPermanent permanent) {
        super(permanent,MagicPermanentState.Regenerated,true);
    }
}
