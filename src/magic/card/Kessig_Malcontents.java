package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;


public class Kessig_Malcontents {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            final int amount = player.getNrOfPermanentsWithSubType(MagicSubType.Human);
            return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new MagicDamageTargetPicker(amount),
                    MagicEvent.NO_DATA,
                    this,
                    permanent + " deals damage to target player$ " +
                    "equal to the number of Humans " + player + " controls.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final int amount = event.getPlayer().getNrOfPermanentsWithSubType(MagicSubType.Human);
                    final MagicDamage damage = new MagicDamage(
                            event.getSource(),
                            player,
                            amount,
                            false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    };
}
