package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.choice.MagicChoice;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetPicker;

public abstract class MagicSpellCardEvent implements MagicCardEvent,MagicEventAction,MagicChangeCardDefinition {

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.setEvent(this);
    }

    public static MagicSpellCardEvent create(final String rule) {
        final String effect = rule.toLowerCase();
        final MagicRuleEventAction ruleAction = MagicRuleEventAction.build(effect);
        final MagicEventAction action  = ruleAction.action;
        final MagicTargetPicker<?> picker = ruleAction.picker;
        final MagicChoice choice = ruleAction.getChoice(effect);

        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    cardOnStack,
                    choice,
                    picker,
                    this,
                    rule + "$"
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                action.executeEvent(game, event);
            }
        };
    }
}
