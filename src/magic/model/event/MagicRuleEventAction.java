package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicDamage;
import magic.model.MagicCounterType;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicTargetAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicTapAction;
import magic.model.action.MagicUntapAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTargetPicker;
import magic.model.target.MagicDefaultTargetPicker;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.target.MagicWeakenTargetPicker;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.target.MagicTapTargetPicker;
import magic.model.choice.MagicTargetChoice;
import magic.model.choice.MagicChoice;
import magic.data.TokenCardDefinitions;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public enum MagicRuleEventAction {
    Destroy(
        "destroy (?<choice>[^\\.]*).", 
        MagicTargetHint.Negative, 
        new MagicDestroyTargetPicker(false), 
        MagicTiming.Removal,
        "Destroy",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicDestroyAction(creature));
                    }
                });
            }
        }
    ),
    DestroyNoRegen(
        "destroy (?<choice>[^\\.]*). it can't be regenerated.", 
        MagicTargetHint.Negative, 
        new MagicDestroyTargetPicker(true), 
        MagicTiming.Removal,
        "Destroy",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(MagicChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
                        game.doAction(new MagicDestroyAction(creature));
                    }
                });
            }
        }
    ),
    Counter(
        "counter (?<choice>[^\\.]*).", 
        MagicTargetHint.Negative, 
        MagicDefaultTargetPicker.create(), 
        MagicTiming.Counter,
        "Counter",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                    public void doAction(final MagicCardOnStack targetSpell) {
                        game.doAction(new MagicCounterItemOnStackAction(targetSpell));
                    }
                });
            }
        }
    ),
    Exile(
        "exile (?<choice>[^\\.]*).", 
        MagicTargetHint.Negative, 
        MagicExileTargetPicker.create(), 
        MagicTiming.Removal,
        "Exile",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent perm) {
                        game.doAction(new MagicRemoveFromPlayAction(perm,MagicLocationType.Exile));
                    }
                });
            }
        }
    ),
    Deals(
        "sn deals (?<amount>[0-9]+) damage to (?<choice>[^\\.]*).",
        MagicTargetHint.Negative, 
        new MagicDamageTargetPicker(1), 
        MagicTiming.Removal,
        "Damage"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTarget(game,new MagicTargetAction() {
                        public void doAction(final MagicTarget target) {
                            final MagicDamage damage=new MagicDamage(event.getSource(),target,amount);
                            game.doAction(new MagicDealDamageAction(damage));
                        }
                    });
                }
            };
        }
    },
    Draw(
        "(pn )?draw(s)? (?<amount>[a-z]+) card(s)?.", 
        MagicTiming.Draw, 
        "Draw"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = MagicRuleEventAction.englishToInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicDrawAction(event.getPlayer(), amount));
                }
            };
        }
    },
    GainLife(
        "(pn )?gain(s)? (?<amount>[0-9]+) life.", 
        MagicTiming.Removal, 
        "+Life"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(), amount));
                }
            };
        }
    },
    PumpSelf(
        "sn gets (?<pt>[0-9+]+/[0-9+]+) until end of turn.", 
        MagicTiming.Pump, 
        "Pump"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),power,toughness));
                }
            };
        }
    },
    Pump(
        "(?<choice>[^\\.]*) gets (?<pt>[0-9+]+/[0-9+]+) until end of turn.", 
        MagicTargetHint.Positive, 
        MagicPumpTargetPicker.create(), 
        MagicTiming.Pump, 
        "Pump"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicChangeTurnPTAction(creature,power,toughness));
                        }
                    });
                }
            };
        }
    },
    GrowSelf(
        "put a (?<type>[^\\.]*) counter on sn.", 
        MagicTiming.Pump, 
        "Pump"
    ) {
        public MagicEventAction getAction(final String rule) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Matcher matcher = matched(rule);
                    final MagicCounterType counterType = englishToCounter(matcher.group("type"));
                    game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        counterType,
                        1,
                        true
                    ));
                }
            };
        }
    },
    Weaken(
        "(?<choice>[^\\.]*) gets (?<pt>[0-9-]+/[0-9-]+) until end of turn.", 
        MagicTargetHint.Negative, 
        MagicTiming.Removal, 
        "Weaken"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicChangeTurnPTAction(creature,power,toughness));
                        }
                    });
                }
            };
        }
        public MagicTargetPicker<?> getPicker(final String rule) {
            final Matcher matcher = matched(rule);
            final String[] args = matcher.group("pt").replace('+','0').split("/");
            final int p = -Integer.parseInt(args[0]);
            final int t = -Integer.parseInt(args[1]);
            return new MagicWeakenTargetPicker(p, t);
        }
    },
    BounceSelf(
        "return sn to its owner's hand.",
        MagicTiming.Removal,
        "Bounce",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicRemoveFromPlayAction(event.getPermanent(),MagicLocationType.OwnersHand));
            }
        }
    ),
    Bounce(
        "return (?<choice>[^\\.]*) to its owner's hand.",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent permanent) {
                        game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.OwnersHand));
                    }
                });
            }
        }
    ),
    Tap(
        "tap (?<choice>[^\\.]*).",
        MagicTargetHint.Negative,
        MagicTapTargetPicker.Tap,
        MagicTiming.Tapping,
        "Tap",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicTapAction(creature,true));
                    }
                });
            }
        }
    ),
    Untap(
        "untap (?<choice>[^\\.]*).",
        MagicTargetHint.Positive,
        MagicTapTargetPicker.Untap,
        MagicTiming.Tapping,
        "Untap",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent gate) {
                        game.doAction(new MagicUntapAction(gate));
                    }
                });
            }
        }
    ),
    Token(
        "pn puts a(n)? (?<name>[^\\.]*) onto the battlefield.",
        MagicTiming.Token,
        "Token"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicPlayTokenAction(
                        event.getPlayer(),
                        TokenCardDefinitions.get(matcher.group("name"))
                    ));
                }
            };
        }
    },
    ;

    private final Pattern pattern;
    private final MagicTargetHint hint;
    private final MagicEventAction action;
    private final MagicTargetPicker<?> picker;
    
    public final MagicTiming timing;
    public final String description;
    
    private MagicRuleEventAction(
            final String aPattern, 
            final MagicTiming aTiming, 
            final String aDescription) {
        this(aPattern, MagicTargetHint.None, MagicDefaultTargetPicker.create(), aTiming, aDescription, MagicEvent.NO_ACTION);
    }
    
    private MagicRuleEventAction(
            final String aPattern, 
            final MagicTiming aTiming, 
            final String aDescription, 
            final MagicEventAction aAction) {
        this(aPattern, MagicTargetHint.None, MagicDefaultTargetPicker.create(), aTiming, aDescription, aAction);
    }
    
    private MagicRuleEventAction(
            final String aPattern, 
            final MagicTargetHint aHint, 
            final MagicTargetPicker<?> aPicker, 
            final MagicTiming aTiming, 
            final String aDescription) {
        this(aPattern, aHint, aPicker, aTiming, aDescription, MagicEvent.NO_ACTION);
    }
    
    private MagicRuleEventAction(
            final String aPattern, 
            final MagicTargetHint aHint, 
            final MagicTiming aTiming, 
            final String aDescription) {
        this(aPattern, aHint, MagicDefaultTargetPicker.create(), aTiming, aDescription, MagicEvent.NO_ACTION);
    }


    private MagicRuleEventAction(
            final String aPattern, 
            final MagicTargetHint aHint, 
            final MagicTargetPicker<?> aPicker, 
            final MagicTiming aTiming, 
            final String aDescription, 
            final MagicEventAction aAction) {
        pattern = Pattern.compile(aPattern);
        hint = aHint;
        picker = aPicker;
        timing = aTiming;
        description = aDescription;
        action = aAction;
    }

    public boolean matches(final String rule) {
        return pattern.matcher(rule).matches();
    }
    
    public MagicTargetPicker<?> getPicker(final String rule) {
        return picker;
    }
    
    public MagicEventAction getAction(final String rule) {
        return action;
    }

    public MagicChoice getChoice(final String rule) {
        final Matcher matcher = matched(rule);
        try {
            return new MagicTargetChoice(hint, matcher.group("choice"));
        } catch (IllegalArgumentException e) {
            return MagicChoice.NONE;
        }
    }

    public static MagicRuleEventAction build(final String rule) {
        for (final MagicRuleEventAction ruleAction : MagicRuleEventAction.values()) {
            if (ruleAction.matches(rule)) {
                return ruleAction;
            }
        }
        throw new RuntimeException("unknown rule: " + rule);
    }

    protected Matcher matched(final String rule) {
        final Matcher matcher = pattern.matcher(rule);
        final boolean matches = matcher.matches();
        if (!matches) {
            throw new RuntimeException("unknown rule: " + rule);
        }
        return matcher;
    }
    
    public static MagicCounterType englishToCounter(String counter) {
        switch (counter) {
            case "+1/+1": return MagicCounterType.PlusOne;
            case "-1/-1": return MagicCounterType.MinusOne;
            case "charge": return MagicCounterType.Charge;
            default: throw new RuntimeException("Unknown type of counter: " + counter);
        }
    }

    public static int englishToInt(String num) {
        switch (num) {
            case "a": return 1;
            case "two": return 2;
            case "three" : return 3;
            case "four" : return 4;
            case "five" : return 5;
            case "six" : return 6;
            case "seven" : return 7;
            default: throw new RuntimeException("Unknown count: " + num);
        }
    }
}
