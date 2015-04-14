package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.MagicPermanent;
import magic.model.choice.MagicMayChoice;
import magic.model.action.TapAction;
import magic.model.action.MagicUntapAction;

public class MagicTapOrUntapEvent extends MagicEvent {

    public MagicTapOrUntapEvent(final MagicSource source, final MagicPermanent it) {
        super(
            source,
            new MagicMayChoice((it.isTapped() ? "Untap " : "Tap ") + it + "?"),
            it,
            EVENT_ACTION,
            "PN may$ " + (it.isTapped() ? "untap" : "tap") + " RN."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getRefPermanent();
            if (event.isYes()) {
                if (permanent.isTapped()) {
                    game.doAction(new MagicUntapAction(permanent));
                } else {
                    game.doAction(new TapAction(permanent));
                }
            }
        }
    };
}
