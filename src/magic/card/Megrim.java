package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDiscardedTrigger;

public class Megrim {
    public static final MagicWhenDiscardedTrigger T = new MagicWhenDiscardedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard data) {
            final MagicPlayer otherController = data.getOwner();
            final MagicPlayer player = permanent.getController();
            return (otherController != player) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,otherController},
                        this,
                        permanent + " deals 2 damage to " + otherController + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(
                    (MagicPermanent)data[0],
                    (MagicPlayer)data[1],
                    2,
                    false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}
