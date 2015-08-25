package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.MagicCounterType;
import magic.model.choice.MagicTargetChoice;
import magic.model.action.ChangeCountersAction;
import magic.model.action.MagicPermanentAction;

public class MagicAwakenEvent extends MagicEvent {
    final private static MagicTargetChoice tchoice = new MagicTargetChoice("target land you control");
    public MagicAwakenEvent(final MagicSource source, final MagicPlayer player, final int n) {
        super(
            source,
            player,
            tchoice,
            n,
            EVENT_ACTION,
            "PN puts RN +1/+1 counters on target land$ you control and " + 
            "it becomes a 0/0 Elemental creature with haste. It's still a land."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent it) {
                    game.doAction(new ChangeCountersAction(
                        it,
                        MagicCounterType.PlusOne,
                        event.getRefInt()
                    ));
                    //TODO it becomes a 0/0 Elemental creature with haste. It's still a land.
                }
            });
        }
    };
}
