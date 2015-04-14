package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.UntapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.target.MagicTapTargetPicker;

public class MagicUntapPermanentEvent extends MagicEvent {
    
    private final MagicCondition[] conds;

    public MagicUntapPermanentEvent(
            final MagicSource source,
            final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }

    public MagicUntapPermanentEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            MagicTapTargetPicker.Untap,
            EVENT_ACTION,
            "Choose "+targetChoice.getTargetDescription()+"$."
        );
        conds = new MagicCondition[]{MagicConditionFactory.HasOptions(player, targetChoice)};
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new UntapAction(permanent));
                }
            });
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
