package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetPicker;

public class MagicPlayAuraEvent extends MagicSpellCardEvent {
	
	private final MagicTargetChoice targetChoice;
	private final MagicTargetPicker targetPicker;
	
    public MagicPlayAuraEvent(
            final MagicTargetChoice targetChoice,
            final MagicTargetPicker targetPicker) {
		this.targetChoice=targetChoice;
		this.targetPicker=targetPicker;
	}
	
	@Override
	public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
		return new MagicEvent(
                cardOnStack.getCard(),
                cardOnStack.getController(),
                targetChoice,
                targetPicker,
                new Object[]{cardOnStack},
                this,
                "Enchant "+targetChoice.getTargetDescription()+"$ with "+cardOnStack.getName()+".");
	}

	@Override
	public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
		final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
		final MagicPermanent creature=event.getTarget(game,choiceResults,0);
		if (creature!=null) {
			game.doAction(new MagicPlayCardFromStackAction(cardOnStack,creature));
		} else {
			game.doAction(new MagicMoveCardAction(cardOnStack));
		}
	}
}
