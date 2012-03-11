package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetPicker;

public class MagicPlayAuraEvent extends MagicSpellCardEvent {
	
	private final MagicTargetChoice targetChoice;
	private final MagicTargetPicker targetPicker;
	
    private MagicPlayAuraEvent(
            final MagicTargetChoice targetChoice,
            final MagicTargetPicker targetPicker) {
		this.targetChoice=targetChoice;
		this.targetPicker=targetPicker;
	}

    public MagicTargetChoice getTargetChoice() {
        return targetChoice;
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
        final boolean success = event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
            public void doAction(final MagicPermanent creature) {
                game.doAction(new MagicPlayCardFromStackAction(cardOnStack,creature));
            }
        });
		if (!success) {
			game.doAction(new MagicMoveCardAction(cardOnStack));
		}
	}

    public static MagicPlayAuraEvent create(String script) {
        String[] token = script.split(",");
        final MagicTargetPicker targetPicker = MagicTargetPicker.build(token[0]); 
        final MagicTargetChoice targetChoice = MagicTargetChoice.build(token[1]);
        assert targetPicker != null : "targetPicker is null";
        assert targetChoice != null : "targetChoice is null";
        return new MagicPlayAuraEvent(targetChoice, targetPicker);
    }
}
