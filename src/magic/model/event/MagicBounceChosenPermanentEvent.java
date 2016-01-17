package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.RemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.target.MagicBounceTargetPicker;

public class MagicBounceChosenPermanentEvent extends MagicEvent {

    public MagicBounceChosenPermanentEvent(final MagicSource source, final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }

    public MagicBounceChosenPermanentEvent(final MagicSource source, final MagicPlayer player, final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            MagicBounceTargetPicker.create(),
            EVENT_ACTION,
            "Return "+targetChoice.getTargetDescription()+"$ to its owner's hand."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        event.processTargetPermanent(game, (final MagicPermanent permanent) -> {
            game.doAction(new RemoveFromPlayAction(
                permanent,
                MagicLocationType.OwnersHand
            ));
        });
    };
}
