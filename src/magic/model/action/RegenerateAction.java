package magic.model.action;

import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

public class RegenerateAction extends ChangeStateAction {
    public RegenerateAction(final MagicPermanent permanent) {
        super(permanent,MagicPermanentState.Regenerated,true);
    }
}
