package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.ScryAction;
import magic.model.choice.MagicScryChoice;
import magic.model.trigger.MagicTriggerType;

public class MagicScryEvent extends MagicEvent {

    public MagicScryEvent(final MagicEvent event) {
        this(event.getSource(), event.getPlayer(), true);
    }

    public MagicScryEvent(final MagicSource source, final MagicPlayer player) {
        this(source, player, true);
    }

    /**
     * Pseudo-scry event is used for example as part of Clash action.
     * As it is not a true "scry" event, it will not trigger "when scry" actions.
     */
    public static MagicScryEvent Pseudo(final MagicEvent event) {
        return new MagicScryEvent(event.getSource(), event.getPlayer(), false);
    }

    /**
     * Pseudo-scry event is used for example as part of Clash action.
     * As it is not a true "scry" event, it will not trigger "when scry" actions.
     */
    public static MagicScryEvent Pseudo(final MagicSource source, final MagicPlayer player) {
        return new MagicScryEvent(source, player, false);
    }

    private MagicScryEvent(final MagicSource source, final MagicPlayer player, final boolean trigger) {
        super(
            source,
            player,
            new MagicScryChoice(),
            trigger ? 1 : 0,
            EventAction,
            ""
        );
    }

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        final boolean trigger = event.getRefInt() == 1;
        final MagicPlayer p = event.getPlayer();
        if (event.isYes()) {
            game.logAppendMessage(p, p + " looks at the top card of his or her library and moves it to the bottom.");
            game.doAction(new ScryAction(p));
        } else {
            game.logAppendMessage(p, p + " looks at the top card of his or her library and puts it back on top.");
        }
        //Scry triggers even if the card is not moved. Only once regardless of amount of cards scryed
        if (trigger) {
            game.executeTrigger(MagicTriggerType.WhenScry,p);
        }
    };
}
