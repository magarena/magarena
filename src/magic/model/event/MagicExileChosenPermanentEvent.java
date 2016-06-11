package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.RemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicExileTargetPicker;

public class MagicExileChosenPermanentEvent extends MagicEvent {

    public MagicExileChosenPermanentEvent(final MagicSource source, final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }

    public MagicExileChosenPermanentEvent(final MagicSource source, final MagicPlayer player, final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            MagicExileTargetPicker.create(),
            EVENT_ACTION,
            "Exile "+targetChoice.getTargetDescription()+"$."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        event.processTargetPermanent(game, (final MagicPermanent permanent) ->
            game.doAction(new RemoveFromPlayAction(
                permanent,
                MagicLocationType.Exile
            ))
        );
}
