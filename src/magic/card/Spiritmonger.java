package magic.card;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicPlayAbilityAction;
import magic.model.choice.MagicColorChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Spiritmonger {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,false,1),
            "Color") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostEvent(
                    source,source.getController(),
                    MagicManaCost.GREEN
                ),
                new MagicPlayAbilityEvent((MagicPermanent)source)
            };
        }
        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    MagicColorChoice.BLUE_RED_WHITE_INSTANCE,
                    this,
                    "SN becomes the color$ of your choice until end of turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPermanent permanent=event.getPermanent();
            game.doAction(new MagicAddStaticAction(permanent, 
                new MagicStatic(MagicLayer.SwitchPT,MagicStatic.UntilEOT) {
                @Override
                public int getColorFlags(
                        final MagicPermanent permanent,
                        final int flags) {
                    return ((MagicColor)choiceResults[0]).getMask();
                }   
            }));
            game.doAction(new MagicPlayAbilityAction(permanent));
        }
    };
}
