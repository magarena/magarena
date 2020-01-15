package magic.model.event;

import java.util.regex.Matcher;

import magic.model.MagicCopyable;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicChoiceFactory;
import magic.model.condition.MagicCondition;
import magic.model.target.MagicTargetPicker;

public class MagicSourceEvent implements MagicCopyable, MagicEventFactory {
    private final MagicRuleEventAction rule;
    private final Matcher matcher;
    private final MagicCondition ifCond;
    private final MagicChoiceFactory choiceFact;
    private final MagicTargetPicker<?> picker;
    private final MagicEventAction action;
    private final String text;

    public MagicSourceEvent(
        final MagicRuleEventAction aRule,
        final Matcher aMatcher,
        final MagicCondition aIfCond,
        final MagicChoiceFactory aChoiceFact,
        final MagicTargetPicker<?> aPicker,
        final MagicEventAction aAction,
        final String aText
    ) {
        rule = aRule;
        matcher = aMatcher;
        ifCond = aIfCond;
        choiceFact = aChoiceFact;
        picker = aPicker;
        action = aAction;
        text = aText;
    }

    @Override
    public MagicEvent getEvent(final MagicSource source, final MagicPlayer player, final MagicCopyable ref) {
        return new MagicEvent(
            source,
            player,
            choiceFact.build(source, player, ref),
            picker,
            ref,
            action,
            text
        );
    }

    // needed for groovy static checking as it does not find default methods
    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return getEvent(source, source.getController(), MagicEvent.NO_REF);
    }

    // needed for groovy static checking as it does not find default methods
    @Override
    public MagicEvent getEvent(final MagicEvent event) {
        return getEvent(event.getSource(), event.getPlayer(), MagicEvent.NO_REF);
    }

    @Override
    public boolean accept(final MagicSource source, final MagicEvent ev) {
        return ifCond.accept(ev);
    }

    public MagicCondition[] getConditions() {
        return rule.getConditions(matcher);
    }

    public MagicTiming getTiming() {
        return rule.getTiming(matcher);
    }

    public String getName() {
        return rule.getName(matcher);
    }

    public boolean isIndependent() {
        return rule.isIndependent();
    }

    public MagicEventAction getAction() {
        return rule.getAction(matcher);
    }

    public MagicChoice getChoice() {
        return rule.getChoice(matcher);
    }

    public MagicChoice getEventChoice() {
        return choiceFact.build(MagicSource.NONE, MagicPlayer.NONE, MagicEvent.NO_REF);
    }

    public MagicTargetPicker<?> getPicker() {
        return rule.getPicker(matcher);
    }

    public MagicEventAction getEventAction() {
        return action;
    }
}
