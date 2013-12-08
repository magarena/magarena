package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.MagicPlayer;
import magic.model.MagicPermanentState;
import magic.model.MagicDamage;
import magic.model.MagicCounterType;
import magic.model.MagicAbility;
import magic.model.MagicManaCost;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayerAction;
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
import magic.model.action.MagicPlayTokensAction;
import magic.model.action.MagicPreventDamageAction;
import magic.model.action.MagicGainAbilityAction;
import magic.model.action.MagicMillLibraryAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
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
import magic.model.target.MagicPreventTargetPicker;
import magic.model.target.MagicDeathtouchTargetPicker;
import magic.model.target.MagicLifelinkTargetPicker;
import magic.model.target.MagicFirstStrikeTargetPicker;
import magic.model.target.MagicHasteTargetPicker;
import magic.model.target.MagicIndestructibleTargetPicker;
import magic.model.target.MagicTrampleTargetPicker;
import magic.model.target.MagicFlyingTargetPicker;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.choice.MagicTargetChoice;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.data.TokenCardDefinitions;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Collection;

public enum MagicRuleEventAction {
    Destroy(
        "destroy (?<choice>[^\\.]*).", 
        MagicTargetHint.Negative,
        MagicDestroyTargetPicker.Destroy,
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
        MagicDestroyTargetPicker.DestroyNoRegen,
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
    CounterUnless(
        "counter (?<choice>[^\\.]*) unless its controller pays (?<cost>[^\\.]*).", 
        MagicTargetHint.Negative, 
        MagicTiming.Counter,
        "Counter"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicManaCost cost = MagicManaCost.create(matcher.group("cost"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                        public void doAction(final MagicCardOnStack targetSpell) {
                            game.addEvent(new MagicCounterUnlessEvent(
                                event.getSource(),
                                targetSpell,
                                cost
                            ));
                        }
                    });
                }
            };
        }
    },
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
        "sn deal(s)? (?<amount>[0-9]+) damage to (?<choice>[^\\.]*).",
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
    Prevent(
        "prevent the next (?<amount>[0-9]+) damage that would be dealt to (?<choice>[^\\.]*) this turn.",
        MagicTargetHint.Positive, 
        MagicPreventTargetPicker.getInstance(),
        MagicTiming.Pump,
        "Prevent"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTarget(game,new MagicTargetAction() {
                        public void doAction(final MagicTarget target) {
                            game.doAction(new MagicPreventDamageAction(target,amount));
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
    DrawChosen(
        "(?<choice>[^\\.]*) draws (?<amount>[a-z]+) card(s)?.",
        MagicTargetHint.Positive, 
        MagicTiming.Draw, 
        "Draw"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = MagicRuleEventAction.englishToInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPlayer(game,new MagicPlayerAction() {
                        public void doAction(final MagicPlayer player) {
                            game.doAction(new MagicDrawAction(player,amount));
                        }
                    });
                }
            };
        }
    },
    DrawDiscard(
        "(pn )?draw(s)? (?<amount1>[a-z]+) card(s)?, then discard(s)? (?<amount2>[a-z]+) card(s)?.", 
        MagicTiming.Draw, 
        "Draw"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount1 = MagicRuleEventAction.englishToInt(matcher.group("amount1"));
            final int amount2 = MagicRuleEventAction.englishToInt(matcher.group("amount2"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicDrawAction(event.getPlayer(), amount1));
                    game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer(), amount2));
                }
            };
        }
    },
    DiscardChosen(
        "(?<choice>[^\\.]*) discards (?<amount>[a-z]+) card(s)?(?<random> at random)?.", 
        MagicTargetHint.Negative, 
        MagicTiming.Draw, 
        "Discard"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = MagicRuleEventAction.englishToInt(matcher.group("amount"));
            final boolean isRandom = matcher.group("random") != null;
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPlayer(game,new MagicPlayerAction() {
                        public void doAction(final MagicPlayer player) {
                            if (isRandom) {
                                game.addEvent(MagicDiscardEvent.Random(event.getSource(), player, amount));
                            } else {
                                game.addEvent(new MagicDiscardEvent(event.getSource(), player, amount));
                            }
                        }
                    });
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
    LoseLifeSelf(
        "(pn )?lose(s)? (?<amount>[0-9]+) life.", 
        MagicTiming.Removal, 
        "-Life"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(), -amount));
                }
            };
        }
    },
    LoseLifeChosen(
        "(?<choice>[^\\.]*) loses (?<amount>[0-9]+) life.", 
        MagicTargetHint.Negative, 
        MagicTiming.Removal, 
        "-Life"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPlayer(game,new MagicPlayerAction() {
                        public void doAction(final MagicPlayer player) {
                            game.doAction(new MagicChangeLifeAction(player,-amount));
                        }
                    });
                }
            };
        }
    },
    LoseGainLifeChosen(
        "(?<choice>[^\\.]*) loses (?<amount1>[0-9]+) life and PN gains (?<amount2>[0-9]+) life.", 
        MagicTargetHint.Negative, 
        MagicTiming.Removal, 
        "-Life"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount1 = Integer.parseInt(matcher.group("amount1"));
            final int amount2 = Integer.parseInt(matcher.group("amount2"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPlayer(game,new MagicPlayerAction() {
                        public void doAction(final MagicPlayer player) {
                            game.doAction(new MagicChangeLifeAction(player,-amount1));
                            game.doAction(new MagicChangeLifeAction(event.getPlayer(),amount2));
                        }
                    });
                }
            };
        }
    },
    PumpSelf(
        "sn gets (?<pt>[+-][0-9]+/[+-][0-9]+) until end of turn.", 
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
    PumpChosen(
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
    PumpGroup(
        "(?<group>[^\\.]*) get (?<pt>[0-9+]+/[0-9+]+) until end of turn.", 
        MagicTiming.Pump, 
        "Pump"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.build(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                        event.getPlayer(),
                        filter
                    );
                    for (final MagicPermanent creature : targets) {
                        game.doAction(new MagicChangeTurnPTAction(creature,power,toughness));
                    }
                }
            };
        }
    },
    PumpGainSelf(
        "sn gets (?<pt>[+-][0-9]+/[+-][0-9]+) and gains (?<ability>[^\\.]*) until end of turn.", 
        MagicTiming.Pump
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),power,toughness));
                    game.doAction(new MagicGainAbilityAction(event.getPermanent(),ability));
                }
            };
        }
        public String getName(final String rule) {
            final String ability = matched(rule).group("ability");
            return Character.toUpperCase(ability.charAt(0)) + ability.substring(1);
        }
    },
    PumpGainChosen(
        "(?<choice>[^\\.]*) gets (?<pt>[0-9+]+/[0-9+]+) and gains (?<ability>[^\\.]*) until end of turn.", 
        MagicTargetHint.Positive
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicChangeTurnPTAction(creature,power,toughness));
                            game.doAction(new MagicGainAbilityAction(creature,ability));
                        }
                    });
                }
            };
        }
        public MagicTiming getTiming(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            switch (ability) {
                case Haste:
                case Vigilance:
                    return MagicTiming.FirstMain;
                case FirstStrike:
                case DoubleStrike:
                    return MagicTiming.Block;
                default:
                    return MagicTiming.Pump;
            }
        }
        public MagicTargetPicker<?> getPicker(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            switch (ability) {
                case Deathtouch: 
                    return MagicDeathtouchTargetPicker.getInstance();
                case Lifelink:
                    return MagicLifelinkTargetPicker.create();
                case FirstStrike:
                case DoubleStrike:
                    return MagicFirstStrikeTargetPicker.create();
                case Haste:
                    return MagicHasteTargetPicker.create();
                case Indestructible:
                    return MagicIndestructibleTargetPicker.create();
                case Trample:
                    return MagicTrampleTargetPicker.create();
                case Flying:
                    return MagicFlyingTargetPicker.create();
                default:
                    return MagicPumpTargetPicker.create(); 
            }
        }
        public String getName(final String rule) {
            final String ability = matched(rule).group("ability");
            return Character.toUpperCase(ability.charAt(0)) + ability.substring(1);
        }
    },
    WeakenChosen(
        "(?<choice>target[^\\.]*) get(s)? (?<pt>[0-9-]+/[0-9-]+) until end of turn.", 
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
    WeakenGroup(
        "(?<group>[^\\.]*) get (?<pt>[0-9-]+/[0-9-]+) until end of turn.", 
        MagicTiming.Removal, 
        "Weaken"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.build(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                        event.getPlayer(),
                        filter
                    );
                    for (final MagicPermanent creature : targets) {
                        game.doAction(new MagicChangeTurnPTAction(creature,power,toughness));
                    }
                }
            };
        }
    },
    ModPTChosen(
        "(?<choice>target[^\\.]*) get(s)? (?<pt>[0-9+-]+/[0-9+-]+) until end of turn.", 
        MagicTiming.Removal, 
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
    ModPTGainChosen(
        "(?<choice>target[^\\.]*) get(s)? (?<pt>[0-9+-]+/[0-9+-]+) and gains (?<ability>[^\\.]*) until end of turn.", 
        MagicTiming.Removal
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicChangeTurnPTAction(creature,power,toughness));
                            game.doAction(new MagicGainAbilityAction(creature,ability));
                        }
                    });
                }
            };
        }
        public String getName(final String rule) {
            final String ability = matched(rule).group("ability");
            return Character.toUpperCase(ability.charAt(0)) + ability.substring(1);
        }
    },
    GainSelf(
        "sn gains (?<ability>[^\\.]*) until end of turn."
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicGainAbilityAction(event.getPermanent(),ability));
                }
            };
        }
        public MagicTiming getTiming(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            switch (ability) {
                case Haste:
                case Vigilance:
                    return MagicTiming.FirstMain;
                case FirstStrike:
                case DoubleStrike:
                    return MagicTiming.Block;
                default:
                    return MagicTiming.Pump;
            }
        }
        public String getName(final String rule) {
            final String ability = matched(rule).group("ability");
            return Character.toUpperCase(ability.charAt(0)) + ability.substring(1);
        }
        public MagicCondition[] getConditions(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            return new MagicCondition[]{
                MagicConditionFactory.NoAbility(ability)
            };
        }
    },
    GainChosen(
        "(?<choice>[^\\.]*) gains (?<ability>[^\\.]*) until end of turn.", 
        MagicTargetHint.Positive
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicGainAbilityAction(creature,ability));
                        }
                    });
                }
            };
        }
        public MagicTiming getTiming(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            switch (ability) {
                case Haste:
                case Vigilance:
                    return MagicTiming.FirstMain;
                case FirstStrike:
                case DoubleStrike:
                    return MagicTiming.Block;
                default:
                    return MagicTiming.Pump;
            }
        }
        public MagicTargetPicker<?> getPicker(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            switch (ability) {
                case Deathtouch: 
                    return MagicDeathtouchTargetPicker.getInstance();
                case Lifelink:
                    return MagicLifelinkTargetPicker.create();
                case FirstStrike:
                case DoubleStrike:
                    return MagicFirstStrikeTargetPicker.create();
                case Haste:
                    return MagicHasteTargetPicker.create();
                case Indestructible:
                    return MagicIndestructibleTargetPicker.create();
                case Trample:
                    return MagicTrampleTargetPicker.create();
                case Flying:
                    return MagicFlyingTargetPicker.create();
                default:
                    return MagicPumpTargetPicker.create(); 
            }
        }
    },
    GainGroup(
        "(?<group>[^\\.]*) gain (?<ability>[^\\.]*) until end of turn."
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.build(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                        event.getPlayer(),
                        filter
                    );
                    for (final MagicPermanent creature : targets) {
                        game.doAction(new MagicGainAbilityAction(creature,ability));
                    }
                }
            };
        }
        public MagicTiming getTiming(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            return (ability == MagicAbility.Haste || ability == MagicAbility.Vigilance) ?
                MagicTiming.FirstMain :
                MagicTiming.Pump;
        }
        public String getName(final String rule) {
            final String ability = matched(rule).group("ability");
            return Character.toUpperCase(ability.charAt(0)) + ability.substring(1);
        }
    },
    GrowSelf(
        "put (?<amount>[a-z]+) \\+1/\\+1 counter(s)? on sn.", 
        MagicTiming.Pump, 
        "Pump"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = englishToInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        MagicCounterType.PlusOne,
                        amount,
                        true
                    ));
                }
            };
        }
    },
    GrowChosen(
        "put (?<amount>[a-z]+) \\+1/\\+1 counter(s)? on (?<choice>[^\\.]*).", 
        MagicTargetHint.Positive,
        MagicPumpTargetPicker.create(),
        MagicTiming.Pump, 
        "Pump"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = englishToInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicChangeCountersAction(
                                creature,
                                MagicCounterType.PlusOne,
                                amount,
                                true
                            ));
                        }
                    });
                }
            };
        }
    },
    ShrinkChosen(
        "put (?<amount>[a-z]+) -1/-1 counter(s)? on (?<choice>[^\\.]*).", 
        MagicTargetHint.Negative,
        MagicTiming.Removal, 
        "Weaken"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = englishToInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicChangeCountersAction(
                                creature,
                                MagicCounterType.MinusOne,
                                amount,
                                true
                            ));
                        }
                    });
                }
            };
        }
        public MagicTargetPicker<?> getPicker(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = englishToInt(matcher.group("amount"));
            return new MagicWeakenTargetPicker(amount,amount);
        }
    },
    ChargeSelf(
        "put (?<amount>[a-z]+) (?<type>[^\\.]*) counter(s)? on sn.", 
        MagicTiming.Main, 
        "Charge"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = englishToInt(matcher.group("amount"));
            final MagicCounterType counterType = englishToCounter(matcher.group("type"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        counterType,
                        amount,
                        true
                    ));
                }
            };
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
    RecoverSelf(
        "return sn from the graveyard to its owner's hand.",
        MagicTiming.Draw,
        "Return",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicCard card = event.getPermanent().getCard();
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
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
    Recover(
        "return (?<choice>[^\\.]*from your graveyard) to your hand.",
        MagicTargetHint.None,
        MagicGraveyardTargetPicker.ReturnToHand,
        MagicTiming.Draw,
        "Return",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                        game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                    }
                });
            }
        }
    ),
    Reanimate(
        "return (?<choice>[^\\.]*) to the battlefield.",
        MagicTargetHint.None,
        MagicGraveyardTargetPicker.PutOntoBattlefield,
        MagicTiming.Pump,
        "Return",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicReanimateAction(
                            card,
                            event.getPlayer()
                        ));
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
    UntapSelf(
        "untap sn.", 
        MagicTiming.Tapping, 
        "Untap"
    ) {
        public MagicEventAction getAction(final String rule) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicUntapAction(event.getPermanent()));
                }
            };
        }
    },
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
                    public void doAction(final MagicPermanent perm) {
                        game.doAction(new MagicUntapAction(perm));
                    }
                });
            }
        }
    ),
    TokenSingle(
        "pn puts (a|an) (?<name>[^\\.]*) onto the battlefield.",
        MagicTiming.Token,
        "Token"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final MagicCardDefinition tokenDef = TokenCardDefinitions.get(matcher.group("name"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicPlayTokenAction(
                        event.getPlayer(),
                        tokenDef
                    ));
                }
            };
        }
    },
    TokenMany(
        "pn puts (?<amount>[a-z]+) (?<name>[^\\.]*tokens[^\\.]*) onto the battlefield.",
        MagicTiming.Token,
        "Token"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = englishToInt(matcher.group("amount"));
            final String tokenName = matcher.group("name").replace("tokens", "token");
            final MagicCardDefinition tokenDef = TokenCardDefinitions.get(tokenName);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicPlayTokensAction(
                        event.getPlayer(),
                        tokenDef,
                        amount
                    ));
                }
            };
        }
    },
    MillSelf(
        "PN puts the top (?<amount>[a-z]+) card(s)? of his or her library into his or her graveyard.",
        MagicTiming.Draw, 
        "Mill"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = englishToInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicMillLibraryAction(event.getPlayer(), amount));
                }
            };
        }
    },
    MillChosen(
        "(?<choice>[^\\.]*) puts the top (?<amount>[a-z]+) card(s)? of his or her library into his or her graveyard.", 
        MagicTiming.Draw, 
        "Mill"
    ) {
        public MagicEventAction getAction(final String rule) {
            final Matcher matcher = matched(rule);
            final int amount = englishToInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPlayer(game,new MagicPlayerAction() {
                        public void doAction(final MagicPlayer player) {
                            game.doAction(new MagicMillLibraryAction(player,amount));
                        }
                    });
                }
            };
        }
    },
    SacrificeUnless(
        "pay (?<cost>[^\\.]*). If you don't, sacrifice SN.", 
        MagicTiming.None, 
        "Sacrifice"
    ) {
        public MagicChoice getChoice(final String rule) {
            final Matcher matcher = matched(rule);
            return new MagicPayManaCostChoice(MagicManaCost.create(matcher.group("cost")));
        }
        public MagicEventAction getNoAction(final String rule) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicSacrificeAction(event.getPermanent()));
                }
            };
        }
    },
    ;

    private final Pattern pattern;
    private final MagicTargetHint hint;
    private final MagicEventAction action;
    private final MagicTargetPicker<?> picker;
    private final MagicTiming timing;
    private final String name;
    
    private MagicRuleEventAction(final String aPattern) {
        this(aPattern, MagicTargetHint.None, MagicDefaultTargetPicker.create(), MagicTiming.None, "", MagicEvent.NO_ACTION);
    }
    
    private MagicRuleEventAction(final String aPattern, final MagicTargetHint aHint) {
        this(aPattern, aHint, MagicDefaultTargetPicker.create(), MagicTiming.None, "", MagicEvent.NO_ACTION);
    }
    
    private MagicRuleEventAction(final String aPattern, final MagicTiming timing) {
        this(aPattern, MagicTargetHint.None, MagicDefaultTargetPicker.create(), timing, "", MagicEvent.NO_ACTION);
    }
    
    private MagicRuleEventAction(
            final String aPattern, 
            final MagicTiming aTiming, 
            final String aName) {
        this(aPattern, MagicTargetHint.None, MagicDefaultTargetPicker.create(), aTiming, aName, MagicEvent.NO_ACTION);
    }
    
    private MagicRuleEventAction(
            final String aPattern, 
            final MagicTiming aTiming, 
            final String aName, 
            final MagicEventAction aAction) {
        this(aPattern, MagicTargetHint.None, MagicDefaultTargetPicker.create(), aTiming, aName, aAction);
    }
    
    private MagicRuleEventAction(
            final String aPattern, 
            final MagicTargetHint aHint, 
            final MagicTargetPicker<?> aPicker, 
            final MagicTiming aTiming, 
            final String aName) {
        this(aPattern, aHint, aPicker, aTiming, aName, MagicEvent.NO_ACTION);
    }
    
    private MagicRuleEventAction(
            final String aPattern, 
            final MagicTargetHint aHint, 
            final MagicTiming aTiming, 
            final String aName) {
        this(aPattern, aHint, MagicDefaultTargetPicker.create(), aTiming, aName, MagicEvent.NO_ACTION);
    }


    private MagicRuleEventAction(
            final String aPattern, 
            final MagicTargetHint aHint, 
            final MagicTargetPicker<?> aPicker, 
            final MagicTiming aTiming, 
            final String aName, 
            final MagicEventAction aAction) {
        pattern = Pattern.compile(aPattern, Pattern.CASE_INSENSITIVE);
        hint = aHint;
        picker = aPicker;
        timing = aTiming;
        name = aName;
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
    
    public MagicEventAction getNoAction(final String rule) {
        return MagicEvent.NO_ACTION;
    }
    
    public MagicTiming getTiming(final String rule) {
        return timing;
    }
    
    public String getName(final String rule) {
        return name;
    }

    public boolean isIndependent() {
        return pattern.toString().contains("sn") == false;
    }

    public MagicChoice getChoice(final String rule) {
        final Matcher matcher = matched(rule);
        try {
            return new MagicTargetChoice(hint, matcher.group("choice"));
        } catch (IllegalArgumentException e) {
            return MagicChoice.NONE;
        }
    }
    
    public MagicCondition[] getConditions(final String rule) {
        return MagicActivation.NO_COND;
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
            case "charge": 
            case "strife":
            case "spore":
                return MagicCounterType.Charge;
            default: throw new RuntimeException("Unknown type of counter: " + counter);
        }
    }

    public static int englishToInt(String num) {
        switch (num) {
            case "a": return 1;
            case "an": return 1;
            case "two": return 2;
            case "three" : return 3;
            case "four" : return 4;
            case "five" : return 5;
            case "six" : return 6;
            case "seven" : return 7;
            case "eight" : return 8;
            case "nine" : return 9;
            case "ten" : return 10;
            default: throw new RuntimeException("Unknown count: " + num);
        }
    }

    public static MagicSourceEvent create(final String rule) {
        final String ruleWithoutMay = rule.replaceFirst("^PN may ", "");
        final String effect = ruleWithoutMay.replaceFirst("^have ", "");
        final MagicRuleEventAction ruleAction = MagicRuleEventAction.build(effect);
        final MagicEventAction action  = ruleAction.getAction(effect);
        final MagicEventAction noAction  = ruleAction.getNoAction(effect);
        final MagicTargetPicker<?> picker = ruleAction.getPicker(effect);
        final MagicChoice choice = ruleAction.getChoice(effect);

        return rule.startsWith("PN may ") ?
            new MagicSourceEvent() {
                @Override
                public MagicEvent getEvent(final MagicSource source) {
                    return new MagicEvent(
                        source,
                        new MagicMayChoice(choice),
                        picker,
                        new MagicEventAction() {
                            @Override
                            public void executeEvent(final MagicGame game, final MagicEvent event) {
                                if (event.isYes()) {
                                    action.executeEvent(game, event);
                                } else {
                                    noAction.executeEvent(game, event);
                                }
                            }
                        },
                        "PN may$ " + ruleWithoutMay + "$"
                    );
                }
                @Override
                public MagicRuleEventAction getRule() {
                    return ruleAction;
                }
            }:
            new MagicSourceEvent() {
                @Override
                public MagicEvent getEvent(final MagicSource source) {
                    return new MagicEvent(
                        source,
                        choice,
                        picker,
                        action,
                        rule + "$"
                    );
                }
                @Override
                public MagicRuleEventAction getRule() {
                    return ruleAction;
                }
            };
    }
}
