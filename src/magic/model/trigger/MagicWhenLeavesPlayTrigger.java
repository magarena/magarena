package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicLocationType;
import magic.model.target.MagicTargetPicker;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicRuleEventAction;
import magic.model.action.MagicRemoveFromPlayAction;

public abstract class MagicWhenLeavesPlayTrigger extends MagicTrigger<MagicRemoveFromPlayAction> {
    public MagicWhenLeavesPlayTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenLeavesPlayTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLeavesPlay;
    }

    // replacement effect has priority 1
    public static final MagicWhenLeavesPlayTrigger IfDieExileInstead = new MagicWhenLeavesPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            if (act.isPermanent(permanent) && act.getToLocation() == MagicLocationType.Graveyard) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    };
    
    public static MagicWhenLeavesPlayTrigger createMay(final String rule) {
        final String effect = rule.toLowerCase();
        final MagicRuleEventAction ruleAction = MagicRuleEventAction.build(effect);
        final MagicEventAction action  = ruleAction.action;
        final MagicTargetPicker<?> picker = ruleAction.picker;
        final MagicChoice choice = ruleAction.getChoice(effect);

        return new MagicWhenLeavesPlayTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
                return new MagicEvent(
                    permanent,
                    new MagicMayChoice(choice),
                    picker,
                    this,
                    "PN may$ " + rule + "$"
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                if (event.isYes()) {
                    action.executeEvent(game, event);
                }
            }
        };
    }
    
    public static MagicWhenLeavesPlayTrigger create(final String rule) {
        final String effect = rule.toLowerCase();
        final MagicRuleEventAction ruleAction = MagicRuleEventAction.build(effect);
        final MagicEventAction action  = ruleAction.action;
        final MagicTargetPicker<?> picker = ruleAction.picker;
        final MagicChoice choice = ruleAction.getChoice(effect);

        return new MagicWhenLeavesPlayTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
                return new MagicEvent(
                    permanent,
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
