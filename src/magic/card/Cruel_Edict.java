package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Cruel_Edict {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player=cardOnStack.getController();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.TARGET_OPPONENT,
                    new Object[]{cardOnStack},
                    this,
                    "Target opponent$ sacrifices a creature.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
            game.doAction(new MagicMoveCardAction(cardOnStack));
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer opponent) {
                    if (opponent.controlsPermanentWithType(MagicType.Creature)) {
                        game.addEvent(new MagicSacrificePermanentEvent(
                            cardOnStack.getCard(),
                            opponent,
                            MagicTargetChoice.SACRIFICE_CREATURE));
                    }
                }
            });
        }
    };
}
