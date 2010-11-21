package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;

public abstract class MagicPermanentActivation extends MagicActivation {
	
	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {

		@Override
		public final void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanentActivation permanentActivation=(MagicPermanentActivation)data[0];
			final MagicPermanent permanent=(MagicPermanent)data[1];
			final MagicAbilityOnStack abilityOnStack=new MagicAbilityOnStack(permanentActivation,permanent,game.getPayedCost());
			game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
		}
	};

	private static int currentIndex=1;
	
	public MagicPermanentActivation(final String name,final MagicCondition conditions[],final MagicActivationHints hints) {
		
		super(name,currentIndex++,conditions,hints);
	}
	
	protected MagicPermanentActivation(final MagicCardDefinition cardDefinition,final MagicCondition conditions[],final MagicActivationHints hints) {

		super(cardDefinition,currentIndex++,conditions,hints);
	}
	
	@Override
	public final boolean usesStack() {

		return true;
	}

	@Override
	public final MagicEvent getEvent(final MagicSource source) {

		return new MagicEvent(source,source.getController(),new Object[]{this,source},EVENT_ACTION,"Play activated ability of "+source.getName()+".");
	}

	@Override
	public final MagicTargetChoice getTargetChoice() {

		// Not the cleanest way to do this by far...
		final MagicCard card=new MagicCard(MagicCardDefinition.EMPTY,null,0);
		final MagicPermanent permanent=new MagicPermanent(0,card,null);
		return getPermanentEvent(permanent,MagicPayedCost.NO_COST).getTargetChoice();
	}
	
	public abstract MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost);
}