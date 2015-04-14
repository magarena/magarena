package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.PlayAbilityAction;
import magic.model.condition.MagicCondition;

public class MagicPlayAbilityEvent extends MagicEvent {

    private final MagicCondition[] conds;
    
    public MagicPlayAbilityEvent(final MagicPermanent source) {
        this(source, MagicCondition.ABILITY_ONCE_CONDITION);
    }

    public MagicPlayAbilityEvent(final MagicPermanent source, final MagicCondition cond) {
        super(
            source,
            EVENT_ACTION,
            ""
        );
        conds = new MagicCondition[]{cond};
    }
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayAbilityAction(event.getPermanent()));
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
