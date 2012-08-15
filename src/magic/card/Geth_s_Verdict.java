package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;

public class Geth_s_Verdict {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new Object[]{cardOnStack},
                    this,
                    "Target player$ sacrifices a creature and loses 1 life."
                );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                            event.getSource(),
                            player,
                            MagicTargetChoice.SACRIFICE_CREATURE));
                    game.doAction(new MagicChangeLifeAction(player,-1));
                }
            });
        }
    };
}
