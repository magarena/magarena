package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.condition.MagicCondition;

public class MagicPayManaCostTapEvent extends MagicEvent {

    private final MagicCondition[] conds;

    public MagicPayManaCostTapEvent(final MagicSource source, final String cost) {
        this(source, source.getController(), MagicManaCost.create(cost));
    }

    private MagicPayManaCostTapEvent(final MagicSource source,final MagicPlayer player,final MagicManaCost cost) {
        super(
            source,
            player,
            new MagicPayManaCostChoice(cost),
            EVENT_ACTION,
            "Pay "+cost.getText()+"$. Tap SN."
        );
        conds = new MagicCondition[] {
            cost.getCondition(),
            MagicCondition.CAN_TAP_CONDITION
        };
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.payManaCost(game);
            game.doAction(new MagicTapAction(event.getPermanent(),true));
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
