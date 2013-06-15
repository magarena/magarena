package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicSacrificePermanentEvent extends MagicEvent {

    final MagicCondition[] conds = new MagicCondition[1];

    public MagicSacrificePermanentEvent(
            final MagicSource source,
            final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }

    public MagicSacrificePermanentEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            MagicSacrificeTargetPicker.create(),
            EVENT_ACTION,
            "Choose "+targetChoice.getTargetDescription()+"$."
        );
        conds[0] = MagicConditionFactory.HasOptions(targetChoice);
    }
    
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicSacrificeAction(permanent));
                }
            });
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
