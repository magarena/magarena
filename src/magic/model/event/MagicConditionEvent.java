package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicConditionEvent extends MagicEvent {

    private final MagicCondition[] conds;

    public MagicConditionEvent(final MagicSource source, final MagicCondition condition) {
        super(
            source,
            MagicEvent.NO_ACTION,
            ""
        );
        conds = new MagicCondition[]{
            condition
        };
    }

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
