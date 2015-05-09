package magic.model.event;

import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.MagicCopyable;
import magic.model.condition.MagicCondition;
import magic.model.choice.MagicChoice;
import magic.model.target.MagicTargetPicker;

import java.util.regex.Matcher;

public abstract class MagicSourceEvent {
    private final MagicRuleEventAction rule;
    private final Matcher matcher;

    public MagicSourceEvent(final MagicRuleEventAction aRule, final Matcher aMatcher) {
        rule = aRule;
        matcher = aMatcher;
    }
    
    public abstract MagicEvent getEvent(final MagicSource source, final MagicPlayer player, final MagicCopyable ref);
    
    public MagicEvent getEvent(final MagicSource source, final MagicCopyable ref) {
        return getEvent(source, source.getController(), ref);
    }
    
    public MagicEvent getEvent(final MagicSource source) {
        return getEvent(source, source.getController(), MagicEvent.NO_REF);
    }
    
    public MagicEvent getEvent(final MagicEvent event) {
        return getEvent(event.getSource(), event.getPlayer(), MagicEvent.NO_REF);
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
    
    public MagicTargetPicker<?> getPicker() {
        return rule.getPicker(matcher);
    }
}
