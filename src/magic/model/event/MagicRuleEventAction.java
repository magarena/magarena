package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetPicker;
import magic.model.target.MagicDefaultTargetPicker;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicExileTargetPicker;
import magic.model.choice.MagicTargetChoice;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public enum MagicRuleEventAction {
    Destroy("destroy ([^\\.]*).", "neg", new MagicDestroyTargetPicker(false), new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDestroyAction(creature));
                }
            });
        }
    }),
    DestroyNoRegen("destroy ([^\\.]*). it can't be regenerated.", "neg", new MagicDestroyTargetPicker(true), new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(MagicChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
                    game.doAction(new MagicDestroyAction(creature));
                }
            });
        }
    }),
    Counter("counter ([^\\.]*).", "neg", MagicDefaultTargetPicker.create(), new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    game.doAction(new MagicCounterItemOnStackAction(targetSpell));
                }
            });
        }
    }),
    Exile("exile ([^\\.]*).", "neg", MagicExileTargetPicker.create(), new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent perm) {
                    game.doAction(new MagicRemoveFromPlayAction(perm,MagicLocationType.Exile));
                }
            });
        }
    });

    private final Pattern pattern;
    private final String hint;
    public final MagicEventAction action;
    public final MagicTargetPicker picker;

    private MagicRuleEventAction(final String aPattern, final String aHint, final MagicTargetPicker aPicker, final MagicEventAction aAction) {
        pattern = Pattern.compile(aPattern);
        hint = aHint;
        picker = aPicker;
        action = aAction;
    }

    public boolean matches(final String rule) {
        return pattern.matcher(rule).matches();
    }

    public MagicTargetChoice getChoice(final String rule) {
        final Matcher matcher = pattern.matcher(rule);
        final boolean matches = matcher.matches();
        if (!matches) {
            throw new RuntimeException("unknown rule: " + rule);
        }
        final String targetClause = matcher.group(1);
        return MagicTargetChoice.build(hint + " " + targetClause);
    }

    public static MagicRuleEventAction build(final String rule) {
        for (final MagicRuleEventAction ruleAction : MagicRuleEventAction.values()) {
            if (ruleAction.matches(rule)) {
                return ruleAction;
            }
        }
        throw new RuntimeException("unknown rule: " + rule);
    }
}
