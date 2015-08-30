package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.ChangeCountersAction;

public class MagicAddCounterEvent extends MagicEvent {

    public MagicAddCounterEvent(final MagicPermanent permanent,final MagicCounterType counterType,final int amount) {
        super(
            permanent,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new ChangeCountersAction(
                        event.getPermanent(),
                        counterType,
                        amount
                    ));
                }
            },
            genDescription(permanent,counterType,amount)
        );
    }

    private static String genDescription(final MagicPermanent permanent,final MagicCounterType counterType,final int amount) {
        final StringBuilder description=new StringBuilder("Put ");
        if (amount==1) {
            description.append("a ").append(counterType.getName()).append(" counter");
        } else {
            description.append(amount).append(' ').append(counterType.getName()).append(" counters");
        }
        description.append(" on ").append(permanent.getName()).append('.');
        return description.toString();
    }
}
