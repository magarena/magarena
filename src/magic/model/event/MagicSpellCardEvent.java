package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDefaultTargetPicker;
import magic.model.target.MagicTargetPicker;

public abstract class MagicSpellCardEvent implements MagicCardEvent,MagicEventAction,MagicChangeCardDefinition {

    public MagicSpellCardEvent() {}

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.setEvent(this);
    }

    //first part is action/picker, rest is target
    public static MagicSpellCardEvent create(final String effect) {
        final String[] args = effect.toLowerCase().split(" ", 2);
        final MagicEventAction action = MagicEventActionFactory.build(args[0]);
        final MagicTargetPicker picker = MagicTargetPicker.build(args[0]); 
        final MagicTargetChoice choice = MagicTargetChoice.build(MagicEventActionFactory.hint(args[0]) + args[1]);

        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    cardOnStack,
                    choice,
                    (picker != null ? picker : MagicDefaultTargetPicker.create()),
                    this,
                    effect + "$."
                );
            }
            @Override
            public void executeEvent(
                    final MagicGame game, 
                    final MagicEvent event, 
                    final Object[] data,
                    final Object[] choiceResults) {
                action.executeEvent(game, event, data, choiceResults);
            }
        };
    }
}
