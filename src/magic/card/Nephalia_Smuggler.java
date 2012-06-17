package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;

public class Nephalia_Smuggler {
    public static final MagicPermanentActivation A = new MagicPermanentActivation( 
            new MagicCondition[] {
                    MagicManaCost.THREE_BLUE.getCondition(),
                    MagicCondition.CAN_TAP_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Main),
            "Card") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{
                    new MagicTapEvent((MagicPermanent)source),
                    new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE_BLUE)};
        }
        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            final MagicTargetFilter targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,source);
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                    targetFilter,true,MagicTargetHint.None,"another creature to exile");
            return new MagicEvent(
                    source,
                    source.getController(),
                    targetChoice,
                    MagicBounceTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Exile another target creature you control$, then " +
                    "return that card to the battlefield under your control.");
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.Exile));
                    game.doAction(new MagicRemoveCardAction(creature.getCard(),MagicLocationType.Exile));
                    game.doAction(new MagicPlayCardAction(creature.getCard(),event.getPlayer(),MagicPlayCardAction.NONE));
                }
            });
        }
    };
}
