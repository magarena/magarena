package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.condition.MagicCondition;

public class MagicGraveyardActivation extends MagicCardActivation {

    protected MagicGraveyardActivation(final MagicCondition[] conditions, final MagicActivationHints hints, final String txt) {
        super(conditions, hints, txt);
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addGraveyardAct(this);
    }
}
