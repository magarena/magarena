package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.TapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTapTargetPicker;

public class MagicTapPermanentEvent extends MagicEvent {

    public MagicTapPermanentEvent(final MagicSource source, final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }

    public MagicTapPermanentEvent(final MagicSource source, final MagicPlayer player, final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            MagicTapTargetPicker.Tap,
            EVENT_ACTION,
            "Tap "+targetChoice.getTargetDescription()+"$."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new TapAction(permanent));
                }
            });
        }
    };
}
