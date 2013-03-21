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
                MagicTargetChoice.NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals damage to target player$ " +
                "equal to the number of Humans PN controls."
            );
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final int amount = event.getPlayer().getNrOfPermanentsWithSubType(MagicSubType.Human);
                    final MagicDamage damage = new MagicDamage(
                        event.getSource(),
                        player,
                        amount
                    );
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    };
}
