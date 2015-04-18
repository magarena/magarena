package magic.model.event;

import magic.model.MagicGame;

public interface MagicEventAction {
    void executeEvent(final MagicGame game, final MagicEvent event);
    
    public static final MagicEventAction NONE = new MagicEventAction() {
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            //do nothing
        }
    };
}
