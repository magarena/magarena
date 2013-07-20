package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.condition.MagicCondition;

public class MagicPayManaCostSacrificeEvent extends MagicEvent {

    private final MagicCondition[] conds;

    public MagicPayManaCostSacrificeEvent(final MagicSource source, final String cost) {
        this(source, source.getController(), MagicManaCost.create(cost));
    }

    public MagicPayManaCostSacrificeEvent(final MagicSource source,final MagicPlayer player,final MagicManaCost cost) {
        super(
            source,
            player,
            new MagicPayManaCostChoice(cost),
            EVENT_ACTION,
            "Pay "+cost.getText()+"$. Sacrifice SN."
        );
        conds = new MagicCondition[]{cost.getCondition()};
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.payManaCost(game);
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
