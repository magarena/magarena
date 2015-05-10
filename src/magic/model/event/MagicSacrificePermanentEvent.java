package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.SacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.target.MagicSacrificeTargetPicker;

public class MagicSacrificePermanentEvent extends MagicEvent {

    public MagicSacrificePermanentEvent(
            final MagicSource source,
            final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }
    
    public MagicSacrificePermanentEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicTargetChoice targetChoice) {
        this(source, player, targetChoice, EVENT_ACTION);
    }

    public MagicSacrificePermanentEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicTargetChoice targetChoice,
            final MagicEventAction action) {
        super(
            source,
            player,
            targetChoice,
            MagicSacrificeTargetPicker.create(),
            action,
            "Choose "+targetChoice.getTargetDescription()+"$."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new SacrificeAction(permanent));
                }
            });
        }
    };
}
