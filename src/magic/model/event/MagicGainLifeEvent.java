package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;

public class MagicGainLifeEvent extends MagicEvent {
    public MagicGainLifeEvent(final MagicPermanent permanent, final int amt) {
        super(
            permanent,
            amt,
            EVENT_ACTION,
            "PN gains RN life."
        );
    }
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(
                event.getPlayer(),
                event.getRefInt()
            ));
        }
    };
}
