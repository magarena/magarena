package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicSacrificePermanentsEvent extends MagicEvent {
    
    private final MagicCondition[] conds;

    public MagicSacrificePermanentsEvent(final MagicSource source, final MagicTargetChoice targetChoice, final int amt) {
        super(
            source,
            targetChoice,
            amt - 1,
            EVENT_ACTION,
            ""
        );
        conds = new MagicCondition[]{MagicConditionFactory.YouControlAtLeast(targetChoice.getPermanentFilter(), amt)};
    }
            
    public static MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificePermanentEvent(event.getSource(), event.getTargetChoice());
            sac.executeEvent(game, event.getChosen());
            for (int i = 0; i < event.getRefInt(); i++) {
                game.addFirstEvent(sac);
            }
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
