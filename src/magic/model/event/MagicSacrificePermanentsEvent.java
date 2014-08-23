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
            amt,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicEvent sac = new MagicSacrificePermanentEvent(event.getSource(), targetChoice);
                    for (int i = 0; i < amt; i++) {
                        game.addEvent(sac);
                    }
                }
            },
            ""
        );
        conds = new MagicCondition[]{MagicConditionFactory.YouControlAtLeast(targetChoice.getPermanentFilter(), amt)};
    }

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
