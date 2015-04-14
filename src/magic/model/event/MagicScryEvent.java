package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.ScryAction;
import magic.model.choice.MagicScryChoice;

public class MagicScryEvent extends MagicEvent {
    
    public MagicScryEvent(final MagicEvent event) {
        this(event.getSource(), event.getPlayer());
    }

    public MagicScryEvent(final MagicSource source, final MagicPlayer player) {
        super(
            source,
            player,
            new MagicScryChoice(),
            EventAction,
            ""
        );
    }
    
    private static final MagicEventAction EventAction = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer p = event.getPlayer();
            if (event.isYes()) {
                game.logAppendMessage(p, p + " looks at the card on the top of his or her library and moves it to the bottom.");
                game.doAction(new ScryAction(p));
            } else {
                game.logAppendMessage(p, p + " looks at the card on the top of his or her library and puts it back on top.");
            }
        }
    };
}
