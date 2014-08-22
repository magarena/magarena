package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicUntapPermanentsEvent extends MagicEvent {
    
    private final MagicCondition[] conds;

    public MagicUntapPermanentsEvent(final MagicSource source, final MagicTargetChoice targetChoice, final int amt) {
        super(
            source,
            amt,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicEvent untap = new MagicUntapPermanentEvent(event.getSource(), targetChoice);
                    for (int i = 0; i < amt; i++) {
                        game.addEvent(untap);
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
