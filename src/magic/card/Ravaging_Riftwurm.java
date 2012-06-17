package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicFadeVanishCounterTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Ravaging_Riftwurm {
    public static final MagicFadeVanishCounterTrigger T = new MagicFadeVanishCounterTrigger("time");
    
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
            return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicManaCost.FOUR,false),
                    new Object[]{cardOnStack,player},
                    this,
                    "$Play " + card + ". If " + card + " was kicked$, " + 
                    "it enters the battlefield with three additional time counters on it.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(cardOnStack);
            action.setKicked(((Integer)choiceResults[1])>0);
            game.doAction(action);
        }
    };
    
    public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {    
            final boolean kicked = permanent.hasState(MagicPermanentState.Kicked);
            final int amount = kicked ? 5 : 2;
            return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent,amount},
                    this,
                    permanent + " enters the battlefield with " +
                            amount + " time counters on it.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                    (MagicPermanent)data[0],
                    MagicCounterType.Charge,
                    (Integer)data[1],
                    true));
        }
        @Override
        public boolean usesStack() {
            return false;
        }
    };
}
