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
import magic.model.action.MagicAddStaticAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

public class Kavu_Titan {
                    
    private static MagicStatic Trample = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(
            final MagicPermanent source,
            final MagicPermanent permanent,
            final long flags) {
            return flags|MagicAbility.Trample.getMask();
        }
    };

    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player=cardOnStack.getController();
            final MagicCard card=cardOnStack.getCard();
            return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicManaCost.TWO_GREEN,false),
                    new Object[]{cardOnStack,player},
                    this,
                    "$Play " + card + ". If " + card + " was kicked$, " + 
                    "it enters the battlefield with three +1/+1 counters on it and has trample.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final boolean kicked=((Integer)choiceResults[1])>0;
            final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
            final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction(cardOnStack);
            game.doAction(action);
            final MagicPermanent permanent=action.getPermanent();
            if (kicked) {
                game.doAction(new MagicChangeCountersAction(
                        permanent,
                        MagicCounterType.PlusOne,
                        3,
                        true));
                game.doAction(new MagicAddStaticAction(permanent, Trample));
            }
        }
    };
}
