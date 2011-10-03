package magic.card;

import java.util.Collection;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRegenerateAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicRegenerateTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;

public class Ezuri__Renegade_Leader {
    public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
            new MagicCondition[]{
            		MagicManaCost.GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,false),
            "Regen") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.GREEN)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        	final MagicTargetFilter targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
					MagicTargetFilter.TARGET_ELF,source);
			final MagicTargetChoice targetChoice = new MagicTargetChoice(
					targetFilter,true,MagicTargetHint.Positive,"another target Elf");
            return new MagicEvent(
                    source,
                    source.getController(),
                    targetChoice,
                    MagicRegenerateTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Regenerate another target Elf$.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRegenerateAction(creature));
                }
            });
        }
    };
    
    public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{
            		MagicManaCost.TWO_GREEN_GREEN_GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Pump") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_GREEN_GREEN_GREEN)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source.getController()},
                    this,
                    "Elf creatures you control get +3/+3 and gain trample until end of turn.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
        	final Collection<MagicTarget> targets =
                    game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_ELF_YOU_CONTROL);
    			for (final MagicTarget target : targets) {
    				final MagicPermanent creature = (MagicPermanent)target;
    				game.doAction(new MagicChangeTurnPTAction(creature,3,3));
    				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
    			}
        }
    };
}
