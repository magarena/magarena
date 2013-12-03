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

public class MagicSorceryConditionEvent extends MagicEvent {

    private final MagicCondition[] conds = new MagicCondition[]{
        MagicCondition.SORCERY_CONDITION
    };

    public MagicSorceryConditionEvent(final MagicSource source) {
        super(
            source,
            MagicEvent.NO_ACTION,
            ""
        );
    }

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
