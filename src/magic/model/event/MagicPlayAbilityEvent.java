package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayAbilityAction;

public class MagicPlayAbilityEvent extends MagicEvent {
    public MagicPlayAbilityEvent(final MagicPermanent source) {
        super(
            source,
            EVENT_ACTION,
            ""
        );
    }
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicPlayAbilityAction(event.getPermanent()));
        }
    };
}
