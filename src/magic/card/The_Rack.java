package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicAtUpkeepTrigger;


public class The_Rack {
    public static final MagicAtUpkeepTrigger T1 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            final MagicPlayer player = permanent.getController();
            final MagicTarget target = permanent.getChosenTarget();
            return (upkeepPlayer == target) ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    this,
                    permanent + " deals X damage to " + upkeepPlayer +
                    " where X is 3 minus the number of cards in his or her hand."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            final int amount = 3 - player.getHandSize();
            if (amount > 0) {
                final MagicDamage damage = new MagicDamage(
                        event.getSource(),
                        player,
                        amount,
                        false);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
    
    public static final Object T2 = Black_Vise.T2;
}
