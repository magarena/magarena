package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicChangeCardDestinationAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;

public class Pulse_of_the_Forge {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new MagicDamageTargetPicker(4),
                    this,
                    cardOnStack + " deals 4 damage to target player$." +
                    "Then if that player has more life than you, " +
                    "return " + cardOnStack + " to its owner's hand.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer targetPlayer) {
                    final MagicDamage damage = new MagicDamage(event.getSource(),targetPlayer,4,false);
                    game.doAction(new MagicDealDamageAction(damage));
                    final boolean more = targetPlayer.getLife() > event.getPlayer().getLife();
                    if (more) {
                        game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.OwnersHand));
                    }
                }
            });
            
            
        }
    };
}
