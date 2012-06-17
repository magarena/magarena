package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Benalish_Lancer {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
            return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicManaCost.TWO_WHITE,false),
                    new Object[]{cardOnStack,player},
                    this,
                    "$Play " + card + ". If " + card + " was kicked$, " +
                    "it enters the battlefield with two +1/+1 counters " +
                    "on it and with first strike.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final int kickerCount = (Integer)choiceResults[1];
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(cardOnStack);
            game.doAction(action);
            if (kickerCount > 0) {
                final MagicPermanent permanent = action.getPermanent();
                game.doAction(new MagicChangeCountersAction(
                        permanent,
                        MagicCounterType.PlusOne,
                        2,
                        true));
                game.doAction(new MagicSetAbilityAction(
                        permanent,
                        MagicAbility.FirstStrike));
            }
        }
    };
}
