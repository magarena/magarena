package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicExcludeChoice;

public class MagicExcludeEvent extends MagicEvent {

    public MagicExcludeEvent(final MagicPlayer player) {
        super(
            MagicSource.NONE,
            player,
            MagicExcludeChoice.getInstance(),
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.getExclude().exclude(game);
        }
    };
}
