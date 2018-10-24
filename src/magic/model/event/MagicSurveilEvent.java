package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.SurveilAction;
import magic.model.choice.MagicSurveilChoice;
import magic.model.trigger.MagicTriggerType;

public class MagicSurveilEvent extends MagicEvent {

    public MagicSurveilEvent(final MagicEvent event) {
        this(event.getSource(), event.getPlayer());
    }

    private MagicSurveilEvent(final MagicSource source, final MagicPlayer player) {
        super(
                source,
                player,
                new MagicSurveilChoice(),
                EventAction,
                ""
        );
    }

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        final MagicPlayer p = event.getPlayer();
        if (event.isYes()) {
            game.logAppendMessage(p,
                    p + " looks at the top card of his or her library and puts it to his or her graveyard.");
            game.doAction(new SurveilAction(p));
        } else {
            game.logAppendMessage(p, p + " looks at the top card of his or her library and puts it back on top.");
        }
        // Surveil triggers even if the card is not moved or library is empty.
        // Only once regardless of amount of cards surveiled
        game.executeTrigger(MagicTriggerType.WhenSurveil, p);
    };
}
