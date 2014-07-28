package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;

public class MagicMorphEvent extends MagicEvent {

    public MagicMorphEvent(final MagicSource source) {
        super(
            source,
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            //enter as a 2/2 creature with no text, name, subtypes, color or cost 
            //as kicker modifier
        }
    };
}
