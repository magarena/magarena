package magic.model.event;

import magic.data.EnglishToInt;
import magic.data.TokenCardDefinitions;
import magic.model.MagicAbility;
import magic.model.MagicAbilityList;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.mstatic.MagicStatic;
import magic.model.stack.MagicItemOnStack;
import magic.model.trigger.MagicAtEndOfCombatTrigger;
import magic.model.trigger.MagicAtEndOfTurnTrigger;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;
import magic.model.action.*;
import magic.model.target.*;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MagicRuleEventAction {
    DestroyGroup(
        "destroy all (?<group>[^\\.]*)\\.", 
        MagicTiming.Removal,
        "Destroy"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),filter);
                    game.doAction(new MagicDestroyAction(targets));
                }
            };
        }
    },
    DestroyChosen(
        "destroy (?<choice>[^\\.]*)\\.", 
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
    DestroyNoRegenGroup(
        "destroy all (?<group>[^\\.]*)\\. They can't be regenerated\\.", 
        MagicTiming.Removal,
        "Destroy"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),filter);
                    for (final MagicPermanent target : targets) {
                        game.doAction(MagicChangeStateAction.Set(target,MagicPermanentState.CannotBeRegenerated));
                    }
                    game.doAction(new MagicDestroyAction(targets));
                }
            };
        }
    },
    DestroyNoRegenChosen(
        "destroy (?<choice>[^\\.]*)\\. it can't be regenerated\\.", 
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
        "counter (?<choice>[^\\.]*) unless its controller pays (?<cost>[^\\.]*)\\.", 
        MagicTargetHint.Negative, 
        MagicTiming.Counter,
        "Counter"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicManaCost cost = MagicManaCost.create(matcher.group("cost"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetItemOnStack(game,new MagicItemOnStackAction() {
                        public void doAction(final MagicItemOnStack item) {
                            game.addEvent(new MagicCounterUnlessEvent(
                                event.getSource(),
                                item,
                                cost
                            ));
                        }
                    });
                }
            };
        }
    },
    Counter(
        "counter (?<choice>[^\\.]*)\\.", 
        MagicTargetHint.Negative, 
        MagicDefaultTargetPicker.create(), 
        MagicTiming.Counter,
        "Counter",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetItemOnStack(game,new MagicItemOnStackAction() {
                    public void doAction(final MagicItemOnStack item) {
                        game.doAction(new MagicCounterItemOnStackAction(item));
                    }
                });
            }
        }
    ),
    ExileCard(
        "exile (?<choice>[^\\.]*from[^\\.]*graveyard)\\.", 
        MagicTargetHint.Negative, 
        MagicTiming.Removal,
        "Exile",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicRemoveCardAction(
                            card,
                            MagicLocationType.Graveyard
                        ));
                        game.doAction(new MagicMoveCardAction(
                            card,
                            MagicLocationType.Graveyard,
                            MagicLocationType.Exile
                        ));
                    }
                });
            }
        }
    ) {
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            if (matcher.group("choice").contains("your")) {
                return MagicGraveyardTargetPicker.ExileOwn;
            } else {
                return MagicGraveyardTargetPicker.ExileOpp;
            }
        }
    },
    ExileGroup(
        "exile all (?<group>[^\\.]*)\\.", 
        MagicTiming.Removal,
        "Exile"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),filter);
                    for (final MagicPermanent perm : targets) {
                        if (event.getSource().isPermanent()) {
                            game.doAction(new MagicExileLinkAction(event.getPermanent(), perm));
                        } else {
                            game.doAction(new MagicRemoveFromPlayAction(perm,MagicLocationType.Exile));
                        }
                    }
                }
            };
        }
    },
    ExilePermanent(
        "exile (?<choice>[^\\.]*)\\.", 
        MagicTargetHint.Negative, 
        MagicExileTargetPicker.create(), 
        MagicTiming.Removal,
        "Exile",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent perm) {
                        if (event.getSource().isPermanent()) {
                            game.doAction(new MagicExileLinkAction(event.getPermanent(), perm));
                        } else {
                            game.doAction(new MagicRemoveFromPlayAction(perm,MagicLocationType.Exile));
                        }
                    }
                });
            }
        }
    ),
    DamageTwoGroup(
        "sn deal(s)? (?<amount>[0-9]+) damage to each (?<group1>[^\\.]*) and each (?<group2>[^\\.]*)\\.",
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            final MagicTargetFilter<MagicTarget> filter1 = MagicTargetFilterFactory.singleTarget(matcher.group("group1"));
            final MagicTargetFilter<MagicTarget> filter2 = MagicTargetFilterFactory.singleTarget(matcher.group("group2"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicTarget> targets1 = game.filterTargets(
                        event.getPlayer(),
                        filter1
                    );
                    for (final MagicTarget target : targets1) {
                        final MagicDamage damage=new MagicDamage(event.getSource(),target,amount);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                    final Collection<MagicTarget> targets2 = game.filterTargets(
                        event.getPlayer(),
                        filter2
                    );
                    for (final MagicTarget target : targets2) {
                        final MagicDamage damage=new MagicDamage(event.getSource(),target,amount);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                }
            };
        }
    },
    DamageGroup(
        "sn deal(s)? (?<amount>[0-9]+) damage to each (?<group>[^\\.]*)\\.",
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            final MagicTargetFilter<MagicTarget> filter = MagicTargetFilterFactory.singleTarget(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicTarget> targets = game.filterTargets(
                        event.getPlayer(),
                        filter
                    );
                    for (final MagicTarget target : targets) {
                        final MagicDamage damage=new MagicDamage(event.getSource(),target,amount);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                }
            };
        }
    },
    DamageController(
        "sn deal(s)? (?<amount>[0-9]+) damage to you\\.",
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),event.getPlayer(),amount);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            };
        }
    },
    DamageChosenAndController(
        "sn deal(s)? (?<amount>[0-9]+) damage to (?<choice>[^\\.]*) and (?<amount2>[0-9]+) damage to you\\.",
        MagicTargetHint.Negative, 
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            final int amount2 = Integer.parseInt(matcher.group("amount2"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTarget(game,new MagicTargetAction() {
                        public void doAction(final MagicTarget target) {
                            game.doAction(new MagicDealDamageAction(
                                new MagicDamage(event.getSource(),target,amount)
                            ));
                            game.doAction(new MagicDealDamageAction(
                                new MagicDamage(event.getSource(),event.getPlayer(),amount2)
                            ));
                        }
                    });
                }
            };
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicDamageTargetPicker(amount);
        }
    },
    DamageChosen(
        "sn deal(s)? (?<amount>[0-9]+) damage to (?<choice>[^\\.]*)\\.",
        MagicTargetHint.Negative, 
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
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
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return DamageChosenAndController.getPicker(matcher);
        }
    },
    PreventSelf(
        "prevent the next (?<amount>[0-9]+) damage that would be dealt to sn this turn\\.",
        MagicTiming.Pump,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicPreventDamageAction(event.getPermanent(),amount));
                }
            };
        }
    },
    PreventOwner(
        "prevent the next (?<amount>[0-9]+) damage that would be dealt to you this turn\\.",
        MagicTiming.Pump,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicPreventDamageAction(event.getPlayer(),amount));
                }
            };
        }
    },
    PreventChosen(
        "prevent the next (?<amount>[0-9]+) damage that would be dealt to (?<choice>[^\\.]*) this turn\\.",
        MagicTargetHint.Positive, 
        MagicPreventTargetPicker.create(),
        MagicTiming.Pump,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
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
    PreventAllCombat(
        "prevent all combat damage that would be dealt this turn\\.",
        MagicTiming.Block,
        "Prevent",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicAddTurnTriggerAction(
                    MagicIfDamageWouldBeDealtTrigger.PreventCombatDamage
                ));
            }
        }
    ),
    PreventAllCombatByChosen(
        "prevent all combat damage that would be dealt by (?<choice>[^\\.]*) this turn\\.",
        MagicTargetHint.Negative,
        new MagicNoCombatTargetPicker(true, true, false),
        MagicTiming.Block,
        "Prevent",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicAddTurnTriggerAction(
                            creature,
                            MagicIfDamageWouldBeDealtTrigger.PreventCombatDamageDealtBy
                        ));
                    }
                });
            }
        }
    ),
    DrawLoseSelf(
        "(pn |you )?draw(s)? (?<amount>[a-z]+) card(s)? and (you )?lose(s)? (?<amount2>[0-9]+) life\\.", 
        MagicTiming.Draw, 
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final int amount2 = Integer.parseInt(matcher.group("amount2"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicDrawAction(event.getPlayer(), amount));
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(), -amount2));
                }
            };
        }
    },
    DrawSelf(
        "(pn |you )?draw(s)? (?<amount>[a-z]+) card(s)?\\.", 
        MagicTiming.Draw, 
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicDrawAction(event.getPlayer(), amount));
                }
            };
        }
    },
    DrawSelfNextUpkeep(
        "(pn |you )?draw(s)? a card at the beginning of the next turn's upkeep\\.", 
        MagicTiming.Draw, 
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicAddTriggerAction(
                        MagicAtUpkeepTrigger.YouDraw(
                            event.getSource(), 
                            event.getPlayer()
                        )
                    ));
                }
            };
        }
    },
    EachDraw(
        "Each (?<group>[^\\.]*) draw(s)? (?<amount>[a-z]+) card(s)?\\.", 
        MagicTiming.Draw, 
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicTargetFilter<MagicPlayer> filter = MagicTargetFilterFactory.singlePlayer(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer player : game.filterPlayers(event.getPlayer(), filter)) {
                        game.doAction(new MagicDrawAction(player, amount));
                    }
                }
            };
        }
    },
    EachDiscard(
        "Each (?<group>[^\\.]*) discard(s)? (?<amount>[a-z]+) card(s)?(?<random> at random)?\\.", 
        MagicTiming.Draw, 
        "Discard"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final boolean isRandom = matcher.group("random") != null;
            final MagicTargetFilter<MagicPlayer> filter = MagicTargetFilterFactory.singlePlayer(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer player : game.filterPlayers(event.getPlayer(), filter)) {
                        if (isRandom) {
                            game.addEvent(MagicDiscardEvent.Random(event.getSource(), player, amount));   
                        } else {
                            game.addEvent(new MagicDiscardEvent(event.getSource(), player, amount));
                        }
                    }
                }
            };
        }
    },
    DrawUpkeep(
        "(pn )?draw(s)? a card at the beginning of the next turn's upkeep\\.", 
        MagicTiming.Draw, 
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicAddTriggerAction(
                        MagicAtUpkeepTrigger.YouDraw(
                            event.getSource(), 
                            event.getPlayer()
                        )
                    ));
                }
            };
        }
    },
    DrawChosen(
        "(?<choice>[^\\.]*) draw(s)? (?<amount>[a-z]+) card(s)?\\.",
        MagicTargetHint.Positive, 
        MagicTiming.Draw, 
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
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
    DrawDiscardSelf(
        "draw(s)? (?<amount1>[a-z]+) card(s)?, then discard(s)? (?<amount2>[a-z]+) card(s)?\\.", 
        MagicTiming.Draw, 
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount1 = EnglishToInt.convert(matcher.group("amount1"));
            final int amount2 = EnglishToInt.convert(matcher.group("amount2"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicDrawAction(event.getPlayer(), amount1));
                    game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer(), amount2));
                }
            };
        }
    },
    DrawDiscardChosen(
        "(?<choice>[^\\.]*) draw(s)? (?<amount1>[a-z]+) card(s)?, then discard(s)? (?<amount2>[a-z]+) card(s)?\\.", 
        MagicTiming.Draw, 
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount1 = EnglishToInt.convert(matcher.group("amount1"));
            final int amount2 = EnglishToInt.convert(matcher.group("amount2"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPlayer(game,new MagicPlayerAction() {
                        public void doAction(final MagicPlayer player) {
                            game.doAction(new MagicDrawAction(player, amount1));
                            game.addEvent(new MagicDiscardEvent(event.getSource(), player, amount2));
                        }
                    });
                }
            };
        }
    },
    DiscardChosen(
        "(?<choice>[^\\.]*) discard(s)? (?<amount>[a-z]+) card(s)?(?<random> at random)?\\.", 
        MagicTargetHint.Negative, 
        MagicTiming.Draw, 
        "Discard"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
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
    DiscardSelf(
        "((Y|y)ou)?( )?discard(s)? (?<amount>[a-z]+) card(s)?(?<random> at random)?\\.", 
        MagicTiming.Draw, 
        "Discard"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final boolean isRandom = matcher.group("random") != null;
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    if (isRandom) {
                        game.addEvent(MagicDiscardEvent.Random(event.getSource(), event.getPlayer(), amount));
                    } else {
                        game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer(), amount));
                    }
                }
            };
        }
    },
    LoseGainLifeChosen(
        "(?<choice>[^\\.]*) lose(s)? (?<amount1>[0-9]+) life and you gain (?<amount2>[0-9]+) life\\.", 
        MagicTargetHint.Negative, 
        MagicTiming.Removal, 
        "-Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
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
    GainLife(
        "((Y|y)ou)?( )?gain (?<amount>[0-9]+) life\\.", 
        MagicTiming.Removal, 
        "+Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(), amount));
                }
            };
        }
    },
    GainLifeChosen(
        "(?<choice>[^\\.]*) gains (?<amount>[0-9]+) life\\.", 
        MagicTargetHint.Positive, 
        MagicTiming.Removal, 
        "+Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPlayer(game,new MagicPlayerAction() {
                        public void doAction(final MagicPlayer player) {
                            game.doAction(new MagicChangeLifeAction(player, amount));
                        }
                    });
                }
            };
        }
    },
    LoseLifeSelf(
        "((Y|y)ou)?( )?lose (?<amount>[0-9]+) life\\.", 
        MagicTiming.Removal, 
        "-Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(), -amount));
                }
            };
        }
    },
    EachLoseLife(
        "Each (?<group>[^\\.]*) loses (?<amount>[0-9]+) life\\.", 
        MagicTiming.Removal, 
        "-Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            final MagicTargetFilter<MagicPlayer> filter = MagicTargetFilterFactory.singlePlayer(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer player : game.filterPlayers(event.getPlayer(), filter)) {
                        game.doAction(new MagicChangeLifeAction(player, -amount));
                    }
                }
            };
        }
    },
    LoseLifeChosen(
        "(?<choice>[^\\.]*) lose(s)? (?<amount>[0-9]+) life\\.", 
        MagicTargetHint.Negative, 
        MagicTiming.Removal, 
        "-Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
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
    PumpSelf(
        "sn get(s)? (?<pt>[+-][0-9]+/[+-][0-9]+) until end of turn\\.", 
        MagicTiming.Pump, 
        "Pump"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
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
        "(?<choice>[^\\.]*) gets (?<pt>[0-9+]+/[0-9+]+) until end of turn\\.", 
        MagicTargetHint.Positive, 
        MagicPumpTargetPicker.create(), 
        MagicTiming.Pump, 
        "Pump"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
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
        "(?<group>[^\\.]*) get (?<pt>[0-9+]+/[0-9+]+) until end of turn\\.", 
        MagicTiming.Pump, 
        "Pump"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
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
        "sn get(s)? (?<pt>[+-][0-9]+/[+-][0-9]+) and gain(s)? (?<ability>.+) until end of turn(\\.)?", 
        MagicTiming.Pump
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),power,toughness));
                    game.doAction(new MagicGainAbilityAction(event.getPermanent(),abilityList));
                }
            };
        }
        public String getName(final Matcher matcher) {
            return GainChosen.getName(matcher);
        }
    },
    PumpGainCantSelf(
        "sn get(s)? (?<pt>[+-][0-9]+/[+-][0-9]+) (until end of turn and|and) (?<ability>can't .+) this turn\\.", 
        MagicTiming.Pump
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),power,toughness));
                    game.doAction(new MagicGainAbilityAction(event.getPermanent(),abilityList));
                }
            };
        }
        public String getName(final Matcher matcher) {
            return GainChosen.getName(matcher);
        }
    },
    PumpGainChosen(
        "(?<choice>target [^\\.]*) get(s)? (?<pt>[0-9+]+/[0-9+]+) and (gain(s)?|is) (?<ability>.+) until end of turn\\.", 
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicChangeTurnPTAction(creature,power,toughness));
                            game.doAction(new MagicGainAbilityAction(creature,abilityList));
                        }
                    });
                }
            };
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainChosen.getTiming(matcher);
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return GainChosen.getPicker(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainChosen.getName(matcher);
        }
    },
    PumpGainGroup(
        "(?<group>[^\\.]*) get (?<pt>[0-9+]+/[0-9+]+) and gain (?<ability>.+) until end of turn\\.", 
        MagicTiming.Pump, 
        "Pump"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                        event.getPlayer(),
                        filter
                    );
                    for (final MagicPermanent creature : targets) {
                        game.doAction(new MagicChangeTurnPTAction(creature,power,toughness));
                        game.doAction(new MagicGainAbilityAction(creature,abilityList));
                    }
                }
            };
        }
    },
    WeakenChosen(
        "(?<choice>target [^\\.]*) get(s)? (?<pt>[0-9-]+/[0-9-]+) until end of turn\\.", 
        MagicTargetHint.Negative, 
        MagicTiming.Removal, 
        "Weaken"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
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
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final String[] args = matcher.group("pt").replace('+','0').split("/");
            final int p = -Integer.parseInt(args[0]);
            final int t = -Integer.parseInt(args[1]);
            return new MagicWeakenTargetPicker(p, t);
        }
    },
    WeakenGroup(
        "(?<group>[^\\.]*) get (?<pt>[0-9-]+/[0-9-]+) until end of turn\\.", 
        MagicTiming.Removal, 
        "Weaken"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
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
        "(?<choice>target [^\\.]*) get(s)? (?<pt>[0-9+-]+/[0-9+-]+) until end of turn\\.", 
        MagicTiming.Removal, 
        "Pump"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
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
        "(?<choice>target [^\\.]*) get(s)? (?<pt>[0-9+-]+/[0-9+-]+) and gains (?<ability>.+) until end of turn\\.", 
        MagicTiming.Removal
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicChangeTurnPTAction(creature,power,toughness));
                            game.doAction(new MagicGainAbilityAction(creature,abilityList));
                        }
                    });
                }
            };
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainChosen.getName(matcher);
        }
    },
    ModPTGroup(
        "(?<group>[^\\.]*) get (?<pt>[0-9+-]+/[0-9+-]+) until end of turn\\.", 
        MagicTiming.Removal, 
        "Pump"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
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
    GainSelf(
        "sn gain(s)? (?<ability>.+) until end of turn\\."
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicGainAbilityAction(event.getPermanent(),abilityList));
                }
            };
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainChosen.getTiming(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainChosen.getName(matcher);
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
            return new MagicCondition[]{
                MagicConditionFactory.NoAbility(ability)
            };
        }
    },
    GainSelfCant(
        "sn (?<ability>can't .+) this turn\\."
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicGainAbilityAction(event.getPermanent(),abilityList));
                }
            };
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainChosen.getTiming(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainChosen.getName(matcher);
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
            return new MagicCondition[]{
                MagicConditionFactory.NoAbility(ability)
            };
        }
    },
    GainChosen(
        "(?<choice>target [^\\.]*) gain(s)? (?<ability>.+) until end of turn\\.", 
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicGainAbilityAction(creature,abilityList));
                        }
                    });
                }
            };
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
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
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
            switch (ability) {
                case Deathtouch: 
                    return MagicDeathtouchTargetPicker.create();
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
        @Override
        public String getName(final Matcher matcher) {
            return capitalize(matcher.group("ability"));
        }
    },
    GainChosenCant(
        "(?<choice>target [^\\.]*) (?<ability>can't .+) this turn\\." 
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicGainAbilityAction(creature,abilityList));
                        }
                    });
                }
            };
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
            switch (ability) {
                case CannotAttack:
                    return MagicTiming.MustAttack;
                case CannotBlock:
                case Unblockable:
                    return MagicTiming.Attack;
                default:
                    return MagicTiming.Pump;
            }
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
            switch (ability) {
                case CannotAttack:
                    return new MagicNoCombatTargetPicker(true,false,false);
                case CannotBlock:
                    return new MagicNoCombatTargetPicker(false,true,false);
                case CannotAttackOrBlock:
                    return new MagicNoCombatTargetPicker(true,true,false);
                case Unblockable:
                    return MagicUnblockableTargetPicker.create();
                default:
                    return MagicDefaultTargetPicker.create();
            }
        }
        @Override
        public MagicTargetHint getHint(final Matcher matcher) {
            final MagicAbility ability = MagicAbility.getAbility(matcher.group("ability"));
            if (ability.getName().contains("be blocked")) {
                return MagicTargetHint.Positive;
            } else {
                return MagicTargetHint.Negative;
            }
        }
        @Override
        public String getName(final Matcher matcher) {
            return capitalize(matcher.group("ability"));
        }
    },
    GainGroup(
        "(?<group>[^\\.]*) gain (?<ability>.+) until end of turn\\."
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                        event.getPlayer(),
                        filter
                    );
                    for (final MagicPermanent creature : targets) {
                        game.doAction(new MagicGainAbilityAction(creature,abilityList));
                    }
                }
            };
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainChosen.getTiming(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainChosen.getName(matcher);
        }
    },
    GainGroupCant(
        "(?<group>[^\\.]*) (?<ability>can't .+) this turn\\."
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return GainGroup.getAction(matcher);
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainChosenCant.getTiming(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainChosenCant.getName(matcher);
        }
    },
    CounterOnSelf(
        "put (?<amount>[a-z]+) (?<type>[^ ]+) counter(s)? on sn\\.",
        MagicTiming.Pump
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
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
        @Override
        public String getName(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            return (amount>1) ? "+Counters" : "+Counter";
        }
    },
    CounterOnGroup(
        "put (?<amount>[a-z]+) (?<type>[^ ]+) counter(s)? on each (?<group>[^\\.]*)\\.",
        MagicTiming.Pump
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.singlePermanent(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                        event.getPlayer(),
                        filter
                    );
                    for (final MagicPermanent target : targets) {
                        game.doAction(new MagicChangeCountersAction(
                            target,
                            counterType,
                            amount,
                            true
                        ));
                    }
                }
            };
        }
        @Override
        public String getName(final Matcher matcher) {
            return CounterOnSelf.getName(matcher);
        }
    },
    CounterFromSelfClockwork(
        "remove a \\+1\\/\\+1 counter from (sn|it) at end of combat\\.",
        MagicTiming.Pump,
        "Remove",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game,final MagicEvent event) {
                game.doAction(new MagicAddTurnTriggerAction(event.getPermanent(), MagicAtEndOfCombatTrigger.Clockwork));
            }
        }
    ),
    CounterFromSelf(
        "remove (?<amount>[a-z]+) (?<type>[^ ]+) counter(s)? from sn\\.",
        MagicTiming.Pump
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        counterType,
                        -amount,
                        true
                    ));
                }
            };
        }
        @Override
        public String getName(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            if (amount>1) {
                final String name = "+Counters";
                return name;
            } else {
                final String name = "+Counter";
                return name;
            }
        }
    },
    CounterOnChosen(
        "put (?<amount>[a-z]+) (?<type>[^ ]+) counter(s)? on (?<choice>[^\\.]*)\\."
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent permanent) {
                            game.doAction(new MagicChangeCountersAction(
                                permanent,
                                counterType,
                                amount,
                                true
                            ));
                        }
                    });
                }
            };
        } 
        @Override
        public MagicTargetHint getHint(final Matcher matcher) {
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            if (counterType.getName().contains("-") || counterType.getScore()<0) {
                return MagicTargetHint.Negative;
            } else {
                return MagicTargetHint.Positive;
            }
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            if (counterType.getName().contains("-")) {
                final String[] pt = counterType.getName().split("/");
                return new MagicWeakenTargetPicker(Integer.parseInt(pt[0]),Integer.parseInt(pt[1]));
            } else if (counterType.getName().contains("+")) {
                return MagicPumpTargetPicker.create();
            } else {
                return MagicDefaultTargetPicker.create();
            }
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            if (counterType.getName().contains("-")) {
                return MagicTiming.Removal;
            } else {
                return MagicTiming.Pump;
            }
        }
        @Override
        public String getName(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            if (amount>1) {
                final String name = "+Counters";
                return name;
            } else {
                final String name = "+Counter";
                return name;
            }
        }
    },
    RecoverSelf(
        "return sn from the graveyard to its owner's hand\\.",
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
    RecoverChosen(
        "return (?<choice>[^\\.]*from your graveyard) to your hand\\.",
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
    BounceSelf(
        "return sn to its owner's hand\\.",
        MagicTiming.Removal,
        "Bounce",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicRemoveFromPlayAction(event.getPermanent(),MagicLocationType.OwnersHand));
            }
        }
    ),
    BounceChosen(
        "return (?<choice>[^\\.]*) to (its owner's|your) hand\\.",
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
    BounceLibTopSelf(
        "put sn on top of its owner's library\\.",
        MagicTiming.Removal,
        "Bounce",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicRemoveFromPlayAction(event.getPermanent(),MagicLocationType.TopOfOwnersLibrary));
            }
        }
    ),
    BounceLibTopChosen(
        "put (?<choice>[^\\.]*) on top of its owner's library\\.",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent permanent) {
                        game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.TopOfOwnersLibrary));
                    }
                });
            }
        }
    ),
    Reclaim(
        "put (?<choice>[^\\.]*from your graveyard) on top of your library\\.",
        MagicTargetHint.None,
        MagicGraveyardTargetPicker.ReturnToHand,
        MagicTiming.Draw,
        "Reclaim",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetCard(game, new MagicCardAction() {
                    public void doAction(final MagicCard targetCard) {
                        game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                        game.doAction(new MagicMoveCardAction(
                            targetCard,
                            MagicLocationType.Graveyard,
                            MagicLocationType.TopOfOwnersLibrary
                        ));
                    }
                });
            }
        }
    ),
    SearchLibraryToHand(
        "search your library for (?<choice>[^\\.]*), reveal (it|that card), (and )?put it into your hand(.|,) (If you do, |(t|T)hen )shuffle your library\\.",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            return MagicChoice.NONE;
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("choice")+" from your library");
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicSearchToLocationEvent(
                        event,
                        choice,
                        MagicLocationType.OwnersHand
                    ));      
                }
            };
        }
    },
    SearchLibraryToTopLibrary(
        "search your library for (?<choice>[^\\.]*)(,| and) reveal (it(,|.)|that card.)( then)? (S|s)huffle your library(, then| and) put (that|the) card on top of it\\.",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            return MagicChoice.NONE;
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("choice")+" from your library");
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicSearchToLocationEvent(
                        event,
                        choice,
                        MagicLocationType.TopOfOwnersLibrary
                    ));      
                }
            };
        }
    },
    SearchLibraryToGraveyard(
        "search your library for (?<choice>[^\\.]*) and put (that card|it) into your graveyard. (If you do,|Then) shuffle your library\\.",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            return MagicChoice.NONE;
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("choice")+" from your library");
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicSearchToLocationEvent(
                        event,
                        choice,
                        MagicLocationType.Graveyard
                    ));      
                }
            };
        }
    },
    SearchLibraryToBattlefield(
        "search your library for (?<choice>[^\\.]*)(,| and) put (it|that card) onto the battlefield(?<tapped> tapped)?(.|,) ((T|t)hen|If you do,) shuffle your library\\.",
        MagicTiming.Pump,
        "Search"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            return MagicChoice.NONE;
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("choice")+" from your library");
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicSearchOntoBattlefieldEvent(
                        event,
                        choice,
                        matcher.group("tapped") != null ? 
                            MagicPlayMod.TAPPED : 
                            MagicPlayMod.NONE
                    ));
                }
            };
        }
    },
    FromHandToBattlefield(
        "put (?<choice>[^\\.]*hand) onto the battlefield(?<tapped> tapped)?\\.",
        MagicTiming.Pump,
        "Put"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            return MagicChoice.NONE;
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("choice"));
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicPutOntoBattlefieldEvent(
                        event,
                        choice,
                        matcher.group("tapped") != null ? 
                            MagicPlayMod.TAPPED : 
                            MagicPlayMod.NONE
                    ));
                }
            };
        }
    },
    Reanimate(
        "return (?<choice>[^\\.]*graveyard) to the battlefield\\.",
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
    Reanimate2(
        "put (?<choice>[^\\.]*graveyard) onto the battlefield under your control\\.",
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
    TapOrUntapChosen(
        "tap or untap (?<choice>[^\\.]*)\\.",
        MagicTargetHint.None,
        MagicTapTargetPicker.TapOrUntap,
        MagicTiming.Tapping,
        "Tap/Untap",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent perm) {
                        game.addEvent(new MagicTapOrUntapEvent(event.getSource(), perm));
                    }
                });
            }
        }
    ),
    TapSelf(
        "tap sn\\.",
        MagicTiming.Tapping,
        "Tap",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicTapAction(event.getPermanent()));
            }
        }
    ),
    TapGroup(
        "tap all (?<group>[^\\.]*)\\.",
        MagicTiming.Tapping,
        "Tap"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),filter);
                    for (final MagicPermanent perm : targets) {
                        game.doAction(new MagicTapAction(perm));
                    }
                }
            };
        }
    },
    TapChosen(
        "tap (?<choice>[^\\.]*)\\.",
        MagicTargetHint.Negative,
        MagicTapTargetPicker.Tap,
        MagicTiming.Tapping,
        "Tap",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicTapAction(creature));
                    }
                });
            }
        }
    ),
    UntapSelf(
        "untap sn\\.", 
        MagicTiming.Tapping, 
        "Untap"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicUntapAction(event.getPermanent()));
                }
            };
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return new MagicCondition[] {
                new MagicArtificialCondition(MagicCondition.TAPPED_CONDITION)
            };
        }
    },
    UntapGroup(
        "untap all (?<group>[^\\.]*)\\.",
        MagicTiming.Tapping,
        "Untap"
    ) {
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),filter);
                    for (final MagicPermanent perm : targets) {
                        game.doAction(new MagicUntapAction(perm));
                    }
                }
            };
        }
    },
    UntapChosen(
        "untap (?<choice>[^\\.]*)\\.",
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
        "put (a|an) (?<name>[^\\.]*) onto the battlefield(?<tapped> tapped)?\\.",
        MagicTiming.Token,
        "Token"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicCardDefinition tokenDef = TokenCardDefinitions.get(matcher.group("name"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicPlayTokenAction(
                        event.getPlayer(),
                        tokenDef,
                        matcher.group("tapped") != null ? 
                            MagicPlayMod.TAPPED : 
                            MagicPlayMod.NONE
                    ));
                }
            };
        }
    },
    TokenMany(
        "put(s)? (?<amount>[a-z]+) (?<name>[^\\.]*tokens[^\\.]*) onto the battlefield\\.",
        MagicTiming.Token,
        "Token"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
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
        "put the top (?<amount>[a-z]+)?( )?card(s)? of your library into your graveyard\\.",
        MagicTiming.Draw, 
        "Mill"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicMillLibraryAction(event.getPlayer(), amount));
                }
            };
        }
    },
    MillChosen(
        "(?<choice>[^\\.]*) put(s)? the top (?<amount>[a-z]+)?( )?card(s)? of his or her library into his or her graveyard\\.", 
        MagicTiming.Draw, 
        "Mill"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
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
        "pay (?<cost>[^\\.]*)\\. If you don't, sacrifice SN\\.", 
        MagicTiming.None, 
        "Sacrifice"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            return new MagicPayManaCostChoice(MagicManaCost.create(matcher.group("cost")));
        }
        @Override
        public MagicEventAction getNoAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicSacrificeAction(event.getPermanent()));
                }
            };
        }
    },
    SacrificeSelf(
        "sacrifice sn\\.",
        MagicTiming.Removal,
        "Sacrifice",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    ),
    SacrificeSelfEndStep(
        "sacrifice (sn|it) at the beginning of the next end step\\.",
        MagicTiming.Removal,
        "Sacrifice",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicAddTriggerAction(event.getPermanent(), MagicAtEndOfTurnTrigger.Sacrifice));
            }
        }
    ),
    SacrificeChosen(
        "sacrifice (?<choice>[^\\.]*)\\.",
        MagicTiming.Removal,
        "Sacrifice"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            return MagicChoice.NONE;
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("choice")+" you control");
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), event.getPlayer(), choice));
                }
            };
        }
    },
    EachSacrificeChosen(
        "Each (?<group>[^\\.]*) sacrifices (?<choice>[^\\.]*)\\.",
        MagicTiming.Removal,
        "Sacrifice"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            return MagicChoice.NONE;
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("choice")+" you control");
            final MagicTargetFilter<MagicPlayer> filter = MagicTargetFilterFactory.singlePlayer(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer player : game.filterPlayers(event.getPlayer(), filter)) {
                        game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), player, choice));
                    }
                }
            };
        }
    },
    ExileSelf(
        "exile sn\\.",
        MagicTiming.Removal,
        "Exile",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicRemoveFromPlayAction(event.getPermanent(),MagicLocationType.Exile));
            }
        }
    ),
    Scry1(
        "(pn )?scry 1\\.",
        MagicTiming.Draw,
        "Scry"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicScryEvent(event));
                }
            };
        }
    },
    /*
    Scry(
        "(pn )?scry (?<amount>[0-9]+)\\.",
        MagicTiming.Draw,
        "Scry"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicScryXEvent(event.getSource(),event.getPlayer(),amount));
                }
            };
        }
    },
    LookHand(
        "look at (?<choice>[^\\.]*)'s hand\\.",
        MagicTargetHint.Negative, 
        MagicTiming.Flash, 
        "Look"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPlayer(game,new MagicPlayerAction() {
                        public void doAction(final MagicPlayer player) {
                            game.doAction(new MagicRevealAction(player.getHand()));
                        }
                    });
                }
            };
        }
    },
    */
    RegenerateSelf(
        "regenerate sn\\.", 
        MagicTiming.Pump, 
        "Regen"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicRegenerateAction(event.getPermanent()));
                }
            };
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return new MagicCondition[] {
                MagicCondition.CAN_REGENERATE_CONDITION,
            };
        }
    },
    RegenerateChosen(
        "regenerate (?<choice>[^\\.]*)\\.", 
        MagicTargetHint.Positive,
        MagicRegenerateTargetPicker.create(),
        MagicTiming.Pump, 
        "Regen"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicRegenerateAction(creature));
                        }
                    });
                }
            };
        }
    },
    SwitchPTSelf(
        "switch sn's power and toughness until end of turn\\.",
        MagicTiming.Pump,
        "Switch"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicAddStaticAction(event.getPermanent(), MagicStatic.SwitchPT));
                }
            };
        }
    },
    SwitchPTChosen(
        "switch (?<choice>[^\\.]*)'s power and toughness until end of turn\\.", 
        MagicTargetHint.None,
        MagicDefaultPermanentTargetPicker.create(),
        MagicTiming.Pump,
        "Switch"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicAddStaticAction(creature, MagicStatic.SwitchPT));
                        }
                    });
                }
            };
        }
    },
    ShuffleSelfPerm(
        "shuffle sn into its owner's library\\.",
        MagicTiming.Removal,
        "Shuffle",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicRemoveFromPlayAction(event.getPermanent(),MagicLocationType.OwnersLibrary));
            }
        }
    ),
    AttachSelf(
        "attach sn to (?<choice>[^\\.]*)\\.",
        MagicTargetHint.Positive,
        MagicPumpTargetPicker.create(),
        MagicTiming.Pump,
        "Attach",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicAttachAction(event.getPermanent(),creature));
                    }
                });
            }
        }
    ),
    TurnSelfFaceDown(
        "turn sn face down\\.",
        MagicTiming.Tapping,
        "Face Down",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicTurnFaceDownAction(event.getPermanent()));
            }
        }
    ),
    TurnChosenFaceDown(
        "turn (?<choice>[^\\.]*) face down\\.",
        MagicTiming.Tapping,
        "Face Down",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicTurnFaceDownAction(creature));
                    }
                });
            }
        }
    ),
    TurnChosenFaceUp(
        "turn (?<choice>[^\\.]*) face up\\.",
        MagicTiming.Tapping,
        "Face Up",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicTurnFaceUpAction(creature));
                    }
                });
            }
        }
    ),
    FlipSelf(
        "flip sn\\.",
        MagicTiming.Pump,
        "Flip",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicFlipAction(event.getPermanent()));
            }
        }
    ),
    TransformSelf(
        "transform sn\\.",
        MagicTiming.Pump,
        "Transform",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicTransformAction(event.getPermanent()));
            }
        }
    ),
    Populate(
        "populate\\.",
        MagicTiming.Token,
        "Populate",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.addEvent(new MagicPopulateEvent(event.getSource()));
            }
        }
    ),
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
            final MagicTiming aTiming, 
            final String aName,
            final MagicEventAction aAction) {
        this(aPattern, aHint, MagicDefaultTargetPicker.create(), aTiming, aName, aAction);
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
    
    public MagicTargetPicker<?> getPicker(final Matcher matcher) {
        return picker;
    }
    
    public MagicEventAction getAction(final Matcher matcher) {
        return action;
    }
    
    public MagicEventAction getNoAction(final Matcher matcher) {
        return MagicEvent.NO_ACTION;
    }
    
    public MagicTiming getTiming(final Matcher matcher) {
        return timing;
    }
    
    public String getName(final Matcher matcher) {
        return name;
    }
    
    public MagicTargetHint getHint(final Matcher matcher) {
        return hint;
    }
    
    public boolean isIndependent() {
        return pattern.toString().contains("sn") == false;
    }

    public MagicChoice getChoice(final Matcher matcher) {
        try {
            return new MagicTargetChoice(getHint(matcher), matcher.group("choice"));
        } catch (IllegalArgumentException e) {
            return MagicChoice.NONE;
        }
    }
    
    public MagicCondition[] getConditions(final Matcher matcher) {
        return MagicActivation.NO_COND;
    }

    public static MagicRuleEventAction build(final String rule) {
        for (final MagicRuleEventAction ruleAction : MagicRuleEventAction.values()) {
            if (ruleAction.matches(rule)) {
                return ruleAction;
            }
        }
        throw new RuntimeException("unknown effect \"" + rule + "\"");
    }

    private static String capitalize(final String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    public Matcher matched(final String rule) {
        final Matcher matcher = pattern.matcher(rule);
        final boolean matches = matcher.matches();
        if (!matches) {
            throw new RuntimeException("unknown effect: \"" + rule + "\"");
        }
        return matcher;
    }

    static final Pattern MAY_PAY = Pattern.compile("^(Y|y)ou may pay (?<cost>[^\\.]+)\\. If you do, .+");
    
    public static MagicEvent create(final MagicSource source, final String rule) {
        return create(rule).getEvent(source);
    }

    private static MagicEventAction computeEventAction(final MagicEventAction main, final String[] part) {
        if (part.length > 1) {
            final MagicEventAction[] acts = new MagicEventAction[part.length];
            acts[0] = main;
            for (int i = 1; i < part.length; i++) {
                final String rider = part[i];
                final MagicRuleEventAction riderAction = MagicRuleEventAction.build(rider);
                final Matcher riderMatcher = riderAction.matched(rider);

                //rider cannot have choices
                if (riderAction.getChoice(riderMatcher).isValid()) {
                    throw new RuntimeException("rider should not have choice: \"" + part[i] + "\"");
                }

                acts[i] = riderAction.getAction(riderMatcher);
            }
            return MagicEventActionFactory.compose(acts);
        } else {
            return main;
        }
    }
    
    public static MagicSourceEvent create(final String text) {
        final String[] part = text.split("~");
        final String rule = part[0];

        final Matcher mayMatcher = MAY_PAY.matcher(rule);
        final boolean mayPay = mayMatcher.matches();
        final MagicManaCost mayCost = mayPay ? MagicManaCost.create(mayMatcher.group("cost")) : MagicManaCost.ZERO;
        final String prefix = mayPay ? "^(Y|y)ou may pay [^\\.]+\\. If you do, " : "^(Y|y)ou may ";

        final String ruleWithoutMay = rule.replaceFirst(prefix, "");
        final String effect = ruleWithoutMay.replaceFirst("^have ", "");

        final MagicRuleEventAction ruleAction = MagicRuleEventAction.build(effect);
        final Matcher matcher = ruleAction.matched(effect);

        // action may be composed from rule and riders
        final MagicEventAction action  = computeEventAction(ruleAction.getAction(matcher), part);

        final MagicEventAction noAction  = ruleAction.getNoAction(matcher);
        final MagicTargetPicker<?> picker = ruleAction.getPicker(matcher);
        final MagicChoice choice = ruleAction.getChoice(matcher);
        final String pnMayChoice = capitalize(ruleWithoutMay).replaceFirst("\\.", "?");

        final String contextRule = ruleWithoutMay.replace("your ","PN's ").replace("you ","PN ").replace("you.", "PN.");
        final String playerRule = text
            .replaceAll("~", " ")
            .replaceAll("(S|s)earch your ", "PN searches PN's ")
            .replaceAll("discard ","discards ")
            .replaceAll("reveal ","reveals ")
            .replaceAll("(S|s)huffle your ","PN shuffles PN's ")
            .replaceAll("(Y|y)ou draw","PN draws")
            .replaceAll("(D|d)raw ","PN draws ")
            .replaceAll("(S|s)acrifice ","PN sacrifices ")
            .replaceAll("(Y|y)ou don't","PN doesn't")
            .replaceAll("(Y|y)ou do","PN does")
            .replaceAll("(Y|y)ou gain ","PN gains ")
            .replaceAll("(Y|y)ou lose ","PN loses ")
            .replaceAll("(Y|y)ou control","PN controls")
            .replaceAll("(Y|y)our ","PN's ")
            .replaceAll("(Y|y)ou ","PN ")
            .replaceAll("you.", "PN.")
            .replaceAll("(P|p)ut ","PN puts ");

        if (mayCost != MagicManaCost.ZERO) {
            return new MagicSourceEvent(ruleAction, matcher) {
                @Override
                public MagicEvent getEvent(final MagicSource source) {
                    return new MagicEvent(
                        source,
                        new MagicMayChoice(
                            new MagicPayManaCostChoice(mayCost),
                            choice
                        ),
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
                        "PN may$ pay " + mayCost + "$. If you do, " + contextRule + "$"
                    );
                }
            };
        } else if (rule.startsWith("You may ") || rule.startsWith("you may ")) {
            return new MagicSourceEvent(ruleAction, matcher) {
                @Override
                public MagicEvent getEvent(final MagicSource source) {
                    return new MagicEvent(
                        source,
                        new MagicMayChoice(
                            pnMayChoice.replaceAll("SN",source.toString()),
                            choice
                        ),
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
                        "PN may$ " + contextRule + "$"
                    );
                }
            };
        } else {
            return new MagicSourceEvent(ruleAction, matcher) {
                @Override
                public MagicEvent getEvent(final MagicSource source) {
                    return new MagicEvent(
                        source,
                        choice,
                        picker,
                        action,
                        capitalize(playerRule) + "$"
                    );
                }
            };
        }
    }
}
