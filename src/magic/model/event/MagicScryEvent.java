package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.ScryAction;
import magic.model.choice.MagicScryChoice;
import magic.model.trigger.MagicTriggerType;

public class MagicScryEvent extends MagicEvent {
    
    public MagicScryEvent(final MagicEvent event, boolean aScry) {
        this(event.getSource(), event.getPlayer(), aScry);
    }
    
    public MagicScryEvent(final MagicEvent event) {
        this(event.getSource(), event.getPlayer(), true);
    }

    public MagicScryEvent(final MagicSource source, final MagicPlayer player, final Boolean aScry) {
        super(
            source,
            player,
            new MagicScryChoice(),
            EventAction(aScry),
            ""
        );
    }
    
    private static final MagicEventAction EventAction(final Boolean scry) {
        return new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicPlayer p = event.getPlayer();
                if (event.isYes()) {
                    game.logAppendMessage(p, p + " looks at the card on the top of his or her library and moves it to the bottom.");
                    game.doAction(new ScryAction(p));
                } else {
                    game.logAppendMessage(p, p + " looks at the card on the top of his or her library and puts it back on top.");
                }
                //Scry triggers even if the card is not moved. Only once regardless of amount of cards scryed
                if (scry) {
                    game.executeTrigger(MagicTriggerType.WhenScry,p);
                }
            }
        };
    };
}
