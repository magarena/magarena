package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.MagicLocationType;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

public class MagicGraveyardCastActivation extends MagicHandCastActivation {

    protected MagicGraveyardCastActivation(final MagicCondition[] conditions, final MagicActivationHints hints, final String txt) {
        super(conditions, hints, txt);
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            EVENT_ACTION,
            "Play SN."
        );
    }

    private final MagicEventAction EVENT_ACTION = genPlayEventAction(MagicLocationType.Graveyard);

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addGraveyardAct(this);
    }
}
