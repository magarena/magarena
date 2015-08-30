package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.SacrificeAction;

public class MagicSacrificeEvent extends MagicEvent {

    public MagicSacrificeEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Sacrifice SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new SacrificeAction(event.getPermanent()));
        }
    };
}
