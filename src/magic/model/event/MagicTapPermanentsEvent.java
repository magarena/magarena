package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.target.MagicTapTargetPicker;

public class MagicTapPermanentsEvent extends MagicEvent {
    
    private final MagicCondition[] conds;

    public MagicTapPermanentsEvent(final MagicSource source, final MagicTargetChoice targetChoice, final int amt) {
        super(
            source,
            amt,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicEvent tap = new MagicTapPermanentEvent(event.getSource(), targetChoice);
                    for (int i = 0; i < amt; i++) {
                        game.addEvent(tap);
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
