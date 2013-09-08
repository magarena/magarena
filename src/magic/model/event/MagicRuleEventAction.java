package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicDamage;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicTargetAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDrawAction;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetPicker;
import magic.model.target.MagicDefaultTargetPicker;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.choice.MagicTargetChoice;
import magic.model.choice.MagicChoice;

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
    }),
    Deals1("sn deals 1 damage to ([^\\.]*).", "neg", new MagicDamageTargetPicker(1), new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,1);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }),
    Deals2("sn deals 2 damage to ([^\\.]*).", "neg", new MagicDamageTargetPicker(2), new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,2);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }),
    Deals3("sn deals 3 damage to ([^\\.]*).", "neg", new MagicDamageTargetPicker(3), new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,3);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }),
    Deals4("sn deals 4 damage to ([^\\.]*).", "neg", new MagicDamageTargetPicker(4), new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,4);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }),
    Deals5("sn deals 5 damage to ([^\\.]*).", "neg", new MagicDamageTargetPicker(5), new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,5);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }),
    DrawACard("pn draws a card.", new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(), 1));
        }
    }),
    DrawTwoCards("pn draws two cards.", new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(), 2));
        }
    }),
    DrawThreeCards("pn draws three cards.", new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(), 3));
        }
    }),
    DrawFourCards("pn draws four cards.", new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(), 4));
        }
    }),
    ;

    private final Pattern pattern;
    private final String hint;
    public final MagicEventAction action;
    public final MagicTargetPicker picker;
    
    private MagicRuleEventAction(final String aPattern, final MagicEventAction aAction) {
        this(aPattern, "", MagicDefaultTargetPicker.create(), aAction);
    }

    private MagicRuleEventAction(final String aPattern, final String aHint, final MagicTargetPicker aPicker, final MagicEventAction aAction) {
        pattern = Pattern.compile(aPattern);
        hint = aHint;
        picker = aPicker;
        action = aAction;
    }

    public boolean matches(final String rule) {
        return pattern.matcher(rule).matches();
    }

    public MagicChoice getChoice(final String rule) {
        final Matcher matcher = pattern.matcher(rule);
        final boolean matches = matcher.matches();
        if (!matches) {
            throw new RuntimeException("unknown rule: " + rule);
        }
        return (matcher.groupCount() > 0) ?
            MagicTargetChoice.build(hint + " " + matcher.group(1)) :
            MagicChoice.NONE;
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
