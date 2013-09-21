package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetPicker;
import magic.model.target.MagicTargetHint;

public class MagicPlayAuraEvent extends MagicSpellCardEvent {

    private final MagicTargetChoice targetChoice;
    private final MagicTargetPicker<?> targetPicker;

    private MagicPlayAuraEvent(
            final MagicTargetChoice targetChoice,
            final MagicTargetPicker<?> targetPicker) {
        this.targetChoice=targetChoice;
        this.targetPicker=targetPicker;
    }

    public MagicTargetChoice getTargetChoice() {
        return targetChoice;
    }

    @Override
    public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
        return new MagicEvent(
            cardOnStack,
            payedCost == MagicPayedCost.NOT_SPELL ?
                new MagicTargetChoice(targetChoice, false):
                new MagicTargetChoice(targetChoice, true),
            targetPicker,
            this,
            "Enchant "+targetChoice.getTargetDescription()+"$ with SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game,new MagicPermanentAction() {
            public void doAction(final MagicPermanent creature) {
                game.doAction(new MagicPlayCardFromStackAction(event.getCardOnStack(),creature));
            }
        });
    }

    public static MagicPlayAuraEvent create(final String script) {
        final String[] token = script.split(",");
        final MagicTargetPicker<?> targetPicker = MagicTargetPicker.build(token[0]);
        final MagicTargetChoice targetChoice = new MagicTargetChoice(
            MagicTargetHint.getHint(token[1]),
            MagicTargetHint.removeHint(token[1])
        );
        assert targetPicker != null : "targetPicker is null";
        assert targetChoice != null : "targetChoice is null";
        return new MagicPlayAuraEvent(targetChoice, targetPicker);
    }
}
