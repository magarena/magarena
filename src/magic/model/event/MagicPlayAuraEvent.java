package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.PlayCardFromStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTargetPicker;

public class MagicPlayAuraEvent extends MagicETBEvent {

    private final MagicTargetChoice targetChoiceCast;
    private final MagicTargetChoice targetChoiceOther;
    private final MagicTargetPicker<?> targetPicker;

    MagicPlayAuraEvent(final MagicTargetChoice aTargetChoiceCast, final MagicTargetChoice aTargetChoiceOther, final MagicTargetPicker<?> aTargetPicker) {
        targetChoiceCast = aTargetChoiceCast;
        targetChoiceOther = aTargetChoiceOther;
        targetPicker = aTargetPicker;
    }

    public MagicTargetChoice getTargetChoice() {
        return targetChoiceOther;
    }

    public MagicTargetPicker<?> getTargetPicker() {
        return targetPicker;
    }

    @Override
    public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
        final MagicTargetChoice targetChoice = cardOnStack.isCast() ? targetChoiceCast : targetChoiceOther;
        return new MagicEvent(
            cardOnStack,
            targetChoice,
            targetPicker,
            this,
            "Enchant "+targetChoice.getTargetDescription()+"$ with SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game, (final MagicPermanent creature) ->
            game.doAction(new PlayCardFromStackAction(event.getCardOnStack(),creature))
        );
    }

    public static MagicPlayAuraEvent create(final String script) {
        final String[] token = script.split(",");
        final MagicTargetPicker<?> targetPicker = MagicTargetPicker.build(token[0]);
        final MagicTargetChoice targetChoiceCast = new MagicTargetChoice(
            MagicTargetHint.getHint(token[1]),
            "target " + MagicTargetHint.removeHint(token[1])
        );
        final MagicTargetChoice targetChoiceOther = new MagicTargetChoice(
            MagicTargetHint.getHint(token[1]),
            "a " + MagicTargetHint.removeHint(token[1])
        );
        assert targetPicker != null : "targetPicker is null";
        assert targetChoiceCast != null : "targetChoiceCast is null";
        assert targetChoiceOther != null : "targetChoiceOther is null";
        return new MagicPlayAuraEvent(targetChoiceCast, targetChoiceOther, targetPicker);
    }
}
