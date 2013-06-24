package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDiscardCardAction;

public class MagicDiscardCardEvent extends MagicEvent {

    public MagicDiscardCardEvent(final MagicSource source,final MagicPlayer player,final MagicCard card) {
        super(
            source,
            player,
            card,
            EVENT_ACTION,
            "PN discards " + card + "."
        );
    }

    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDiscardCardAction(event.getPlayer(),event.getRefCard()));
        }
    };
}
