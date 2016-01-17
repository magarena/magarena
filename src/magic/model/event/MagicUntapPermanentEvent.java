package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.UntapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTapTargetPicker;

public class MagicUntapPermanentEvent extends MagicEvent {

    public MagicUntapPermanentEvent(final MagicSource source, final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }

    public MagicUntapPermanentEvent(final MagicSource source, final MagicPlayer player, final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            MagicTapTargetPicker.Untap,
            EVENT_ACTION,
            "Untap "+targetChoice.getTargetDescription()+"$."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        event.processTargetPermanent(game, (final MagicPermanent permanent) ->
            game.doAction(new UntapAction(permanent)));
}
