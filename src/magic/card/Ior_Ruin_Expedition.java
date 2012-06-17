package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Ior_Ruin_Expedition {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
            final MagicPlayer player = permanent.getController();
            return (player == played.getController() && played.isLand()) ?
                new MagicEvent(
                    permanent,
                    player,
                    new MagicSimpleMayChoice(
                            player + " may put a quest counter on " + permanent + ".",
                            MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                            1,
                            MagicSimpleMayChoice.DEFAULT_YES),
                    new Object[]{permanent},
                    this,
                    player + " may$ put a quest counter on " + permanent + ".") :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicChangeCountersAction(
                        (MagicPermanent)data[0],
                        MagicCounterType.Charge,
                        1,
                        true));
            }
        }        
    };
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.THREE_CHARGE_COUNTERS_CONDITION},
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{
                new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.Charge,3),
                new MagicSacrificeEvent((MagicPermanent)source)};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicPlayer player=source.getController();
            return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    player + " draws two cards");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicDrawAction((MagicPlayer)data[0],2));
        }
    };
}
