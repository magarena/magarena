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
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;


public class Black_Vise {
    public static final MagicAtUpkeepTrigger T1 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            final MagicTarget target = permanent.getChosenTarget();
            return (data == target) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent,data},
                    this,
                    permanent + " deals X damage to " + data +
                    " where X is the number of cards in his or her hand minus 4."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer opponent = (MagicPlayer)data[1];
            final int amount = opponent.getHandSize() - 4;
            if (amount > 0) {
                final MagicDamage damage = new MagicDamage(
                        (MagicSource)data[0],
                        opponent,
                        amount,
                        false);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
    
    public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            permanent.setChosenTarget(player.getOpponent());
            return MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
        }
    };
}
