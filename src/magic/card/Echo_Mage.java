package magic.card;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicCopyCardOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicLevelUpActivation;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.stack.MagicCardOnStack;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Echo_Mage {
	
    private static final MagicLocalVariable LV = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int charges=permanent.getCounters(MagicCounterType.Charge);
			if (charges>=4) {
				pt.power=2;
				pt.toughness=5;
			} else if (charges>=2) {
				pt.power=2;
				pt.toughness=4;
			}
		}		
	};

	public static final MagicPermanentActivation A = new MagicLevelUpActivation(MagicManaCost.ONE_BLUE,4);
	
	public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
			new MagicCondition[]{
                MagicCondition.TWO_CHARGE_COUNTERS_CONDITION,
                MagicCondition.CAN_TAP_CONDITION,MagicManaCost.BLUE_BLUE.getCondition()},
			new MagicActivationHints(MagicTiming.Spell),
            "Copy") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.BLUE_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			final int amount=source.getCounters(MagicCounterType.Charge)>=4?2:1;
			final String description = amount == 2 ?
					"Copy target instant or sorcery spell$ twice. You may choose new targets for the copies.":
					"Copy target instant or sorcery spell$. You may choose new targets for the copy.";
			return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_SPELL,
                    new Object[]{player,amount},
                    this,
                    description);
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
            event.processTargetCardOnStack(game,choiceResults,0,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    final MagicPlayer player=(MagicPlayer)data[0];
                    final int amount=(Integer)data[1];
                    for (int count=amount;count>0;count--) {
                        game.doAction(new MagicCopyCardOnStackAction(player,targetSpell));
                    }
                }
			});
		}
	};
	
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
            cdef.addLocalVariable(LV);	
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.setVariablePT();
        }
    };
}
