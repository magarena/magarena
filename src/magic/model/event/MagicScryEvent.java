package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.choice.MagicScryChoice;
import magic.model.action.MagicScryAction;

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
            if (event.isYes()) {
                final MagicPlayer p = event.getPlayer();
                game.logAppendMessage(p, p + " moves a card from top of his or her library to the bottom.");
                game.doAction(new MagicScryAction(p));
            }
        }
    };
}
