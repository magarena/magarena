package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicCardDefinition;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;

public abstract class MagicPermanentActivation extends MagicActivation implements MagicChangeCardDefinition {
	
	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
		@Override
		public final void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicPermanentActivation permanentActivation = (MagicPermanentActivation)data[0];
			final MagicPermanent permanent = (MagicPermanent)data[1];
			final MagicAbilityOnStack abilityOnStack = 
                new MagicAbilityOnStack(permanentActivation,permanent,game.getPayedCost());
			game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
		}
	};

	private static int currentIndex = 1;

    public MagicPermanentActivation(
            final MagicCondition conditions[],
            final MagicActivationHints hints,
            final String txt) {
		super(currentIndex++,conditions,hints,txt);
	}

	@Override
	public final boolean usesStack() {
		return true;
	}

	@Override
	public final MagicEvent getEvent(final MagicSource source) {
		return new MagicEvent(
                source,
                source.getController(),
                new Object[]{this,source},
                EVENT_ACTION,
                "Play activated ability of "+source.getName()+"."
                );
	}

	@Override
	public final MagicTargetChoice getTargetChoice(final MagicSource source) {
		// Not the cleanest way to do this by far...
		final MagicPermanent permanent=(MagicPermanent)source;
		return getPermanentEvent(permanent,MagicPayedCost.NO_COST).getTargetChoice();
	}
	
	public abstract MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost);
    
    @Override
    public void executeEvent(
            final MagicGame game, 
            final MagicEvent event, 
            final Object data[], 
            final Object[] choiceResults) {
        throw new RuntimeException(getClass() + " did not override executeEvent");
    }
    
    @Override
    public void change(MagicCardDefinition cdef) {
        cdef.addAct(this);
        setCardDefinition(cdef);
    }
}
