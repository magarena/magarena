package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;

public class Taste_of_Blood {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
            return new MagicEvent(
                    card,
                    player,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new MagicDamageTargetPicker(1),
                    new Object[]{cardOnStack},
                    this,
                    card + " deals 1 damage to target player$ and " +
                    player + " gains 1 life.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            game.doAction(new MagicMoveCardAction(cardOnStack));            
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final MagicDamage damage = new MagicDamage(cardOnStack.getCard(),player,1,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
        }
    };
}
