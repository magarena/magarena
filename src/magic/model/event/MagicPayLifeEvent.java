package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.ChangeLifeAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicPayLifeEvent extends MagicEvent {

    private final MagicCondition[] conds;

    public MagicPayLifeEvent(final MagicSource source,final int amount) {
        this(source, source.getController(), amount);
    }

    public MagicPayLifeEvent(final MagicSource source,final MagicPlayer player,final int amount) {
        super(
            source,
            player,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new ChangeLifeAction(event.getPlayer(),-amount));
                }
            },
            "Pay "+amount+" life."
        );
        conds = new MagicCondition[] {
            MagicConditionFactory.YouLifeAtLeast(amount)
        };
    }

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
