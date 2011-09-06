package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Retaliator_Griffin {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final int amount=damage.getDealtAmount();
			final MagicPlayer player=permanent.getController();
            return (amount > 0 && 
                    damage.getTarget() == player &&
                    damage.getSource().getController()!=player) ?
                new MagicEvent(
                    permanent,
                    player,
                    MagicEvent.NO_DATA,
                    new MagicEventAction() {
                    @Override
                    public void executeEvent(
                            final MagicGame game,
                            final MagicEvent event,
                            final Object data[],
                            final Object[] choiceResults) {
                        game.doAction(new MagicChangeCountersAction(
                                    permanent.map(game),
                                    MagicCounterType.PlusOne,
                                    amount,true));
                    }},
                    "Put "+amount+" +1/+1 counters on " + permanent + "."):
                MagicEvent.NONE;
		}
    };
}
