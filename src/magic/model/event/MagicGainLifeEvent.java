package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.ChangeLifeAction;

public class MagicGainLifeEvent extends MagicEvent {
    public MagicGainLifeEvent(final MagicPermanent permanent, final MagicPlayer player, final int amt) {
        super(
            permanent,
            player,
            amt,
            EVENT_ACTION,
            "PN gains RN life."
        );
    }

    public MagicGainLifeEvent(final MagicPermanent permanent, final int amt) {
        this(permanent, permanent.getController(), amt);
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(
                event.getPlayer(),
                event.getRefInt()
            ));
        }
    };
}
