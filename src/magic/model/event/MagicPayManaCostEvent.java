package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.condition.MagicCondition;

public class MagicPayManaCostEvent extends MagicEvent {

    final MagicCondition[] conds = new MagicCondition[1];

    public MagicPayManaCostEvent(final MagicSource source, final MagicManaCost cost) {
        this(source, source.getController(), cost);
    }

    public MagicPayManaCostEvent(final MagicSource source, final String cost) {
        this(source, source.getController(), MagicManaCost.create(cost));
    }

    private MagicPayManaCostEvent(final MagicSource source,final MagicPlayer player,final MagicManaCost cost) {
        super(
            source,
            player,
            new MagicPayManaCostChoice(cost),
            EVENT_ACTION,
            "Pay "+cost.getText()+"$."
        );
        conds[0] = cost.getCondition();
    }

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
    
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.payManaCost(game,event.getPlayer());
        }
    };
}
