package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Marsh_Casualties {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player=cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new MagicKickerChoice(MagicTargetChoice.NEG_TARGET_PLAYER,MagicManaCost.THREE,false),
                    new Object[]{cardOnStack},
                    this,
                    "Creatures target player$ controls get -1/-1 until end of turn. " +
                    "If " + card + " was kicked$, those creatures get -2/-2 until end of turn instead.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
            game.doAction(new MagicMoveCardAction(cardOnStack));
            final MagicPlayer player=(MagicPlayer)choiceResults[0];
            final int amount=(Integer)choiceResults[1]>0?-2:-1;
            final Collection<MagicTarget> targets=
                game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,amount,amount));
            }
        }
    };
}
