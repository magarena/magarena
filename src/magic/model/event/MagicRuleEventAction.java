package magic.model.event;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magic.data.EnglishToInt;
import magic.data.TokenCardDefinitions;
import magic.model.ARG;
import magic.model.MagicRandom;
import magic.model.MagicAbility;
import magic.model.MagicAbilityList;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCopyable;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.*;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicFromCardFilterChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicOrChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.condition.MagicConditionParser;
import magic.model.mstatic.MagicStatic;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.*;
import magic.model.trigger.MagicAtEndOfCombatTrigger;
import magic.model.trigger.MagicAtEndOfTurnTrigger;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;
import magic.model.trigger.MagicReboundTrigger;

public enum MagicRuleEventAction {
    DestroyGroup(
        "destroy all (?<group>[^\\.]*)\\.(?<noregen> They can't be regenerated\\.)?", 
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
                    if (matcher.group("noregen") != null) {
                        for (final MagicPermanent it : targets) {
                            game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                        }
                    }
                    game.doAction(new MagicDestroyAction(targets));
                }
            };
        }
    },
    DestroyIt(
        "destroy " + ARG.IT + "\\.(?<noregen> it can't be regenerated\\.)?", 
        MagicTiming.Removal,
        "Destroy"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicPermanent it = ARG.itPermanent(event, matcher);
                    if (matcher.group("noregen") != null) {
                        game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                    }
                    game.doAction(new MagicDestroyAction(it));
                }
            };
        }
    },
    DestroyItEndOfCombat(
        "destroy " + ARG.IT + " at end of combat\\.", 
        MagicTiming.Removal,
        "Destroy"
    ) { 
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicPermanent it = ARG.itPermanent(event, matcher);
                    if (it.isValid()) {
                        game.doAction(new AddTurnTriggerAction(
                            it,
                            MagicAtEndOfCombatTrigger.Destroy
                        ));
                    }
                }
            };
        }
    },
    DestroyChosen(
        "destroy (?<choice>[^\\.]*)\\.(?<noregen> it can't be regenerated\\.)?", 
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Destroy"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent it) {
                            if (matcher.group("noregen") != null) {
                                game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                            }
                            game.doAction(new MagicDestroyAction(it));
                        }
                    });
                }
            };
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            if (matcher.group("noregen") != null) {
                return MagicDestroyTargetPicker.DestroyNoRegen;
            } else {
                return MagicDestroyTargetPicker.Destroy;
            }
        }
    },
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
    CounterSpellToExile(
        "counter (?<choice>[^\\.]*)\\. if that spell is countered this way, exile it instead of putting it into its owner's graveyard.", 
        MagicTargetHint.Negative, 
        MagicDefaultTargetPicker.create(), 
        MagicTiming.Counter,
        "Counter",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetItemOnStack(game,new MagicItemOnStackAction() {
                    public void doAction(final MagicItemOnStack item) {
                        game.doAction(new MagicCounterItemOnStackAction(item, MagicLocationType.Exile));
                    }
                });
            }
        }
    ),
    CounterSpell(
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
    BlinkSelf(
        "exile sn, then return it to the battlefield under your control\\.",
        MagicTiming.Removal,
        "Flicker",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicPermanent it = event.getPermanent();
                game.doAction(new MagicRemoveFromPlayAction(
                    it,
                    MagicLocationType.Exile
                ));
                final MagicRemoveCardAction removeCard = new MagicRemoveCardAction(it.getCard(), MagicLocationType.Exile);
                game.doAction(removeCard);
                if (removeCard.isValid()) {
                    game.doAction(new MagicPlayCardAction(
                        it.getCard(),
                        event.getPlayer()
                    ));
                }
            }
        }
    ),
    BlinkChosen(
        "exile (?<choice>[^\\.]*), then return (it|that card) to the battlefield under your control\\.",
        MagicTargetHint.Positive,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Flicker",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game, new MagicPermanentAction() {
                    public void doAction(final MagicPermanent it) {
                        game.doAction(new MagicRemoveFromPlayAction(
                            it,
                            MagicLocationType.Exile
                        ));
                        final MagicRemoveCardAction removeCard = new MagicRemoveCardAction(it.getCard(), MagicLocationType.Exile);
                        game.doAction(removeCard);
                        if (removeCard.isValid()) {
                            game.doAction(new MagicPlayCardAction(
                                it.getCard(),
                                event.getPlayer()
                            ));
                        }
                    }
                });
            }
        }
    ),
    BlinkFlickerSelf(
        "exile sn, then return it to the battlefield under its owner's control\\.",
        MagicTiming.Removal,
        "Flicker",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicPermanent it = event.getPermanent();
                game.doAction(new MagicRemoveFromPlayAction(
                    it,
                    MagicLocationType.Exile
                ));
                final MagicRemoveCardAction removeCard = new MagicRemoveCardAction(it.getCard(), MagicLocationType.Exile);
                game.doAction(removeCard);
                if (removeCard.isValid()) {
                    game.doAction(new MagicPlayCardAction(
                        it.getCard(),
                        it.getOwner()
                    ));
                }
            }
        }
    ),
    BlinkFlickerChosen(
        "exile (?<choice>[^\\.]*), then return (it|that card) to the battlefield under its owner's control\\.",
        MagicTargetHint.Positive,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Flicker",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game, new MagicPermanentAction() {
                    public void doAction(final MagicPermanent it) {
                        game.doAction(new MagicRemoveFromPlayAction(
                            it,
                            MagicLocationType.Exile
                        ));
                        final MagicRemoveCardAction removeCard = new MagicRemoveCardAction(it.getCard(), MagicLocationType.Exile);
                        game.doAction(removeCard);
                        if (removeCard.isValid()) {
                            game.doAction(new MagicPlayCardAction(
                                it.getCard(),
                                it.getOwner()
                            ));
                        }
                    }
                });
            }
        }
    ),
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
        ARG.IT + " deal(s)? (?<amount>[0-9]+) damage to each (?<group1>[^\\.]*) and each (?<group2>[^\\.]*)\\.",
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
                    final MagicSource source = ARG.itSource(event, matcher);
                    final Collection<MagicTarget> targets1 = game.filterTargets(
                        event.getPlayer(),
                        filter1
                    );
                    for (final MagicTarget target : targets1) {
                        game.doAction(new MagicDealDamageAction(source,target,amount));
                    }
                    final Collection<MagicTarget> targets2 = game.filterTargets(
                        event.getPlayer(),
                        filter2
                    );
                    for (final MagicTarget target : targets2) {
                        game.doAction(new MagicDealDamageAction(source,target,amount));
                    }
                }
            };
        }
    },
    DamageGroup(
        ARG.IT + " deal(s)? (?<amount>[0-9]+) damage to each (?<group>[^\\.]*)\\.",
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
                        game.doAction(new MagicDealDamageAction(ARG.itSource(event, matcher),target,amount));
                    }
                }
            };
        }
    },
    DamageTarget(
        "sn deal(s)? (?<amount>[0-9]+) damage to " + ARG.YOU + "\\.",
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicDealDamageAction(event.getSource(),ARG.youTarget(event, matcher),amount));
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
                            game.doAction(new MagicDealDamageAction(event.getSource(),target,amount));
                            game.doAction(new MagicDealDamageAction(event.getSource(),event.getPlayer(),amount2));
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
        ARG.IT + " deal(s)? (?<amount>[0-9]+) damage to (?<choice>[^\\.]*)(\\.)?",
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
                            game.doAction(new MagicDealDamageAction(ARG.itSource(event, matcher),target,amount));
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
                game.doAction(new AddTurnTriggerAction(
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
                        game.doAction(new AddTurnTriggerAction(
                            creature,
                            MagicIfDamageWouldBeDealtTrigger.PreventCombatDamageDealtBy
                        ));
                    }
                });
            }
        }
    ),
    PreventAllDamageToChosen(
        "prevent all damage that would be dealt to (?<choice>[^\\.]*) this turn\\.",
        MagicTargetHint.Positive,
        MagicPreventTargetPicker.create(),
        MagicTiming.Pump,
        "Prevent",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new AddTurnTriggerAction(
                            creature,
                            MagicIfDamageWouldBeDealtTrigger.PreventDamageDealtTo
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
                    game.doAction(new ChangeLifeAction(event.getPlayer(), -amount2));
                }
            };
        }
    },
    DrawSelf(
        ARG.YOU + "( )?draw(s)? (?<amount>[a-z]+) (additional )?card(s)?\\.", 
        MagicTiming.Draw, 
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicDrawAction(ARG.youPlayer(event, matcher), amount));
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
                    game.doAction(new AddTriggerAction(
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
                    game.doAction(new AddTriggerAction(
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
    DrawDiscardPlayer(
        ARG.YOU + "( )?draw(s)? (?<amount1>[a-z]+) card(s)?, then discard(s)? (?<amount2>[a-z]+) card(s)?\\.", 
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
                    game.doAction(new MagicDrawAction(ARG.youPlayer(event, matcher), amount1));
                    game.addEvent(new MagicDiscardEvent(event.getSource(), ARG.youPlayer(event, matcher), amount2));
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
    DiscardYou(
        ARG.YOU + "( )?discard(s)? (?<amount>[a-z]+) card(s)?(?<random> at random)?\\.", 
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
                    final MagicPlayer player = ARG.youPlayer(event, matcher);
                    if (isRandom) {
                        game.addEvent(MagicDiscardEvent.Random(event.getSource(), player, amount));
                    } else {
                        game.addEvent(new MagicDiscardEvent(event.getSource(), player, amount));
                    }
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
    DiscardHand(
        "((Y|y)ou)?( )?discard your hand\\.",
        MagicTiming.Draw,
        "Discard"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicDiscardHandEvent(event.getSource()));
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
                            game.doAction(new ChangeLifeAction(player,-amount1));
                            game.doAction(new ChangeLifeAction(event.getPlayer(),amount2));
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
                    game.doAction(new ChangeLifeAction(event.getPlayer(), amount));
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
                            game.doAction(new ChangeLifeAction(player, amount));
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
                    game.doAction(new ChangeLifeAction(event.getPlayer(), -amount));
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
                        game.doAction(new ChangeLifeAction(player, -amount));
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
                            game.doAction(new ChangeLifeAction(player,-amount));
                        }
                    });
                }
            };
        }
    },
    PumpSelf(
        ARG.IT + " get(s)? (?<pt>[+-][0-9]+/[+-][0-9]+) until end of turn\\.", 
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
                    game.doAction(new ChangeTurnPTAction(ARG.itPermanent(event, matcher),power,toughness));
                }
            };
        }
    },
    PumpChosen(
        "(?<choice>target [^\\.]*) get(s)? (?<pt>[0-9+]+/[0-9+]+) until end of turn\\.", 
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
                            game.doAction(new ChangeTurnPTAction(creature,power,toughness));
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
                        game.doAction(new ChangeTurnPTAction(creature,power,toughness));
                    }
                }
            };
        }
    },
    PumpGainSelf(
        ARG.IT + " get(s)? (?<pt>[+-][0-9]+/[+-][0-9]+) and gain(s)? (?<ability>.+) until end of turn(\\.)?", 
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
                    game.doAction(new ChangeTurnPTAction(ARG.itPermanent(event, matcher),power,toughness));
                    game.doAction(new MagicGainAbilityAction(ARG.itPermanent(event, matcher),abilityList));
                }
            };
        }
        public String getName(final Matcher matcher) {
            return GainChosen.getName(matcher);
        }
    },
    PumpGainCanSelf(
        "sn get(s)? (?<pt>[+-][0-9]+/[+-][0-9]+) (until end of turn and|and) (?<ability>can('t)? .+) this turn\\.", 
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
                    game.doAction(new ChangeTurnPTAction(event.getPermanent(),power,toughness));
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
                            game.doAction(new ChangeTurnPTAction(creature,power,toughness));
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
    PumpGainChosenCan(
        "(?<choice>target [^\\.]*) get(s)? (?<pt>[0-9+]+/[0-9+]+) (until end of turn and|and) (?<ability>can('t)? .+) this turn\\.",
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return PumpGainChosen.getAction(matcher);
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
    PumpGainChosenAlt(
        "until end of turn, (?<choice>target [^\\.]*) get(s)? (?<pt>[0-9+]+/[0-9+]+) and (gain(s)?|is) (?<ability>.+)(\\.)?", 
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return PumpGainChosen.getAction(matcher);
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
                        game.doAction(new ChangeTurnPTAction(creature,power,toughness));
                        game.doAction(new MagicGainAbilityAction(creature,abilityList));
                    }
                }
            };
        }
    },
    PumpGainGroupAlt(
        "until end of turn, (?<group>[^\\.]*) get (?<pt>[0-9+]+/[0-9+]+) and gain (?<ability>.+)(\\.)?", 
        MagicTiming.Pump, 
        "Pump"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return PumpGainGroup.getAction(matcher);
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
                            game.doAction(new ChangeTurnPTAction(creature,power,toughness));
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
                        game.doAction(new ChangeTurnPTAction(creature,power,toughness));
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
                            game.doAction(new ChangeTurnPTAction(creature,power,toughness));
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
                            game.doAction(new ChangeTurnPTAction(creature,power,toughness));
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
                        game.doAction(new ChangeTurnPTAction(creature,power,toughness));
                    }
                }
            };
        }
    },
    CounterOnSelf(
        "put (?<amount>[a-z]+) (?<type>[^ ]+) counter(s)? on " + ARG.IT + "\\.",
        MagicTiming.Pump
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new ChangeCountersAction(
                        ARG.itPermanent(event, matcher),
                        counterType,
                        amount
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
                        game.doAction(new ChangeCountersAction(
                            target,
                            counterType,
                            amount
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
                game.doAction(new AddTurnTriggerAction(event.getPermanent(), MagicAtEndOfCombatTrigger.Clockwork));
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
                    game.doAction(new ChangeCountersAction(
                        event.getPermanent(),
                        counterType,
                        -amount
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
                            game.doAction(new ChangeCountersAction(
                                permanent,
                                counterType,
                                amount
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
    Bolster(
        "bolster (?<n>[0-9]+)\\.",
        MagicTiming.Pump,
        "Bolster"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("n"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = event.getPlayer().filterPermanents(MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
                    int minToughness = Integer.MAX_VALUE;
                    for (final MagicPermanent creature: targets) {
                        minToughness = Math.min(minToughness, creature.getToughnessValue());
                    }
                    game.addEvent(new MagicBolsterEvent(event.getSource(), event.getPlayer(), amount, minToughness));
                }
            };
        }
    },
    RecoverCardSelf(
        "return sn from your graveyard to your hand\\.",
        MagicTiming.Draw,
        "Return",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicCard card = event.getCard();
                final MagicRemoveCardAction remove = new MagicRemoveCardAction(card,MagicLocationType.Graveyard);
                game.doAction(remove);
                if (remove.isValid()) {
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                }
            }
        }
    ),
    ReanimateCardSelf(
        "return sn from your graveyard to the battlefield( )?(?<mods>.+)?\\.",
        MagicTiming.Token,
        "Reanimate"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final List<MagicPlayMod> mods = MagicPlayMod.build(matcher.group("mods"));
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicReanimateAction(
                        event.getCard(),
                        event.getPlayer(),
                        mods
                    ));
                }
            };
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
                final MagicRemoveCardAction remove = new MagicRemoveCardAction(card,MagicLocationType.Graveyard);
                game.doAction(remove);
                if (remove.isValid()) {
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                }
            }
        }
    ),
    RecoverChosen(
        "return (?<choice>[^\\.]*from (your|a) graveyard) to (your|its owner's) hand\\.",
        MagicTargetHint.Positive,
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
    ReclaimChosen(
        "put (?<choice>[^\\.]*from (your|a) graveyard) on top of (your|its owner's) library\\.",
        MagicTargetHint.Positive,
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
    TuckChosen(
        "put (?<choice>[^\\.]*from (your|a) graveyard) on the bottom of (your|its owner's) library\\.",
        MagicTargetHint.Negative,
        MagicGraveyardTargetPicker.ExileOpp,
        MagicTiming.Draw,
        "Tuck",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetCard(game, new MagicCardAction() {
                    public void doAction(final MagicCard targetCard) {
                        game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                        game.doAction(new MagicMoveCardAction(
                            targetCard,
                            MagicLocationType.Graveyard,
                            MagicLocationType.BottomOfOwnersLibrary
                        ));
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
    BounceItEndOfCombat(
        "return " + ARG.IT + " to its owner's hand at end of combat\\.",
        MagicTiming.Removal,
        "Bounce"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicPermanent it = ARG.itPermanent(event, matcher);
                    if (it.isValid()) {
                        game.doAction(new AddTriggerAction(
                            it,
                            MagicAtEndOfCombatTrigger.Return
                        ));
                    }
                }
            };
        }
    },
    BounceGroup(
        "return all (?<group>[^\\.]*) to their (owner's hand|owners' hands)\\.",
        MagicTiming.Removal,
        "Bounce"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),filter);
                    for (final MagicPermanent it : targets) {
                        game.doAction(new MagicRemoveFromPlayAction(it, MagicLocationType.OwnersHand));
                    }
                }
            };
        }
    },
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
    BounceLibBottomChosen(
        "put (?<choice>[^\\.]*) on the bottom of its owner's library\\.",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent permanent) {
                        game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.BottomOfOwnersLibrary));
                    }
                });
            }
        }
    ),
    FlickerSelf(
        "exile sn\\. (if you do, )?return (it|sn) to the battlefield under its owner's control at the beginning of the next end step\\.",
        MagicTiming.Removal,
        "Flicker",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicExileUntilEndOfTurnAction(event.getPermanent()));
            }
        }
    ),
    FlickerChosen(
        "exile (?<choice>[^\\.]*)\\. (if you do, )?return (the exiled card|that card|it) to the battlefield under its owner's control at the beginning of the next end step\\.",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Flicker",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game, new MagicPermanentAction() {
                    public void doAction(final MagicPermanent it) {
                        game.doAction(new MagicExileUntilEndOfTurnAction(it));
                    }
                });
            }
        }
    ),
    SearchLibraryToHand(
        "search your library for (?<card>[^\\.]*), reveal (it|that card), (and )?put it into your hand(.|,) (If you do, |(t|T)hen )shuffle your library\\.",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("card")+" from your library");
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
    SearchLibraryToHandAlt(
        "search your library for (?<card>[^\\.]*)( and|,) put (it|that card) into your hand(.|,) (If you do, |(t|T)hen )shuffle your library\\.",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return SearchLibraryToHand.getAction(matcher);
        }
    },
    SearchMultiLibraryToHand(
        "search your library for up to (?<amount>[a-z]+) (?<card>[^\\.]*), reveal (them|those cards), (and )?put them into your hand(.|,) (If you do, |(t|T)hen )shuffle your library\\.",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicCard> filter = MagicTargetFilterFactory.multipleCards(matcher.group("card") + " from your library");
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicSearchToLocationEvent(
                        event,
                        new MagicFromCardFilterChoice(
                            filter,
                            amount, 
                            true, 
                            "to put into your hand"
                        ),
                        MagicLocationType.OwnersHand
                    ));      
                }
            };
        }
    },
    SearchLibraryToTopLibrary(
        "search your library for (?<card>[^\\.]*)(,| and) reveal (it(,|.)|that card.)( then)? (S|s)huffle your library(, then| and) put (that|the) card on top of it\\.",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("card")+" from your library");
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
        "search your library for (?<card>[^\\.]*) and put (that card|it) into your graveyard. (If you do,|Then) shuffle your library\\.",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("card")+" from your library");
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
        "search your library for (?<card>[^\\.]*)(,| and) put (it|that card) onto the battlefield( )?(?<mods>.+)?(.|,) ((T|t)hen|If you do,) shuffle your library\\.",
        MagicTiming.Token,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("card")+" from your library");
            final List<MagicPlayMod> mods = MagicPlayMod.build(matcher.group("mods"));
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicSearchOntoBattlefieldEvent(
                        event,
                        choice,
                        mods
                    ));
                }
            };
        }
    },
    SearchMultiLibraryToBattlefield(
        "search your library for up to (?<amount>[a-z]+) (?<card>[^\\.]*)(,| and) put (them|those cards) onto the battlefield( )?(?<mods>.+)?(.|,) ((T|t)hen|If you do,) shuffle your library\\.",
        MagicTiming.Token,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicTargetFilter<MagicCard> filter = MagicTargetFilterFactory.multipleCards(matcher.group("card") + " from your library");
            final List<MagicPlayMod> mods = MagicPlayMod.build(matcher.group("mods"));
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicSearchOntoBattlefieldEvent(
                        event,
                        new MagicFromCardFilterChoice(
                            filter,
                            amount,
                            true,
                            "to put onto the battlefield"
                        ),
                        mods
                    ));
                }
            };
        }
    },
    FromHandToBattlefield(
        "put (?<card>[^\\.]*hand) onto the battlefield( )?(?<mods>.+)?\\.",
        MagicTiming.Token,
        "Put"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("card"));
            final List<MagicPlayMod> mods = MagicPlayMod.build(matcher.group("mods"));
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicPutOntoBattlefieldEvent(
                        event,
                        choice,
                        mods
                    ));
                }
            };
        }
    },
    Reanimate(
        "return (?<choice>[^\\.]*graveyard) to the battlefield( )?(?<mods>.+)?\\.",
        MagicTargetHint.None,
        MagicGraveyardTargetPicker.PutOntoBattlefield,
        MagicTiming.Token,
        "Return"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final List<MagicPlayMod> mods = MagicPlayMod.build(matcher.group("mods"));
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetCard(game,new MagicCardAction() {
                        public void doAction(final MagicCard card) {
                            game.doAction(new MagicReanimateAction(
                                card,
                                event.getPlayer(),
                                mods
                            ));
                        }
                    });
                }
            };
        }
    },
    Reanimate2(
        "put (?<choice>[^\\.]*graveyard) onto the battlefield under your control\\.",
        MagicTargetHint.None,
        MagicGraveyardTargetPicker.PutOntoBattlefield,
        MagicTiming.Token,
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
    TapParalyzeIt(
        "tap " + ARG.IT + "( and it|. RN) doesn't untap during its controller's next untap step\\.",
        MagicTiming.Tapping,
        "Tap"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicPermanent it = ARG.itPermanent(event, matcher);
                    game.doAction(new MagicTapAction(it));
                    game.doAction(ChangeStateAction.Set(
                        it,
                        MagicPermanentState.DoesNotUntapDuringNext
                    ));
                }
            };
        }
    },
    TapParalyzeChosen(
        "tap (?<choice>[^\\.]*)\\. it doesn't untap during its controller's next untap step\\.",
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
                        game.doAction(ChangeStateAction.Set(
                            creature,
                            MagicPermanentState.DoesNotUntapDuringNext
                        ));
                    }
                });
            }
        }
    ),
    TapIt(
        "tap " + ARG.IT + "\\.",
        MagicTiming.Tapping,
        "Tap"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicTapAction(ARG.itPermanent(event, matcher)));
                }
            };
        }
    },
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
    ParalyzeIt(
        ARG.IT + " doesn't untap during (your|its controller's) next untap step\\.",
        MagicTiming.Tapping,
        "Paralyze"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(ChangeStateAction.Set(
                        ARG.itPermanent(event, matcher),
                        MagicPermanentState.DoesNotUntapDuringNext
                    ));
                }
            };
        }
    },
    ParalyzeChosen(
        "(?<choice>[^\\.]*) doesn't untap during its controller's next untap step\\.",
        MagicTargetHint.Negative,
        new MagicNoCombatTargetPicker(true,true,false),
        MagicTiming.Tapping,
        "Paralyze",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent perm) {
                        game.doAction(ChangeStateAction.Set(
                            perm,
                            MagicPermanentState.DoesNotUntapDuringNext
                        ));
                    }
                });
            }
        }
    ),
    UntapSelf(
        "untap " + ARG.IT + "\\.", 
        MagicTiming.Tapping, 
        "Untap"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicUntapAction(ARG.itPermanent(event, matcher)));
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
        "put (a|an) (?<name>[^\\.]*) onto the battlefield( )?(?<mods>.+)?\\.",
        MagicTiming.Token,
        "Token"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicCardDefinition tokenDef = TokenCardDefinitions.get(matcher.group("name"));
            final List<MagicPlayMod> mods = MagicPlayMod.build(matcher.group("mods"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicPlayTokenAction(
                        event.getPlayer(),
                        tokenDef,
                        mods
                    ));
                }
            };
        }
    },
    TokenMany(
        "put(s)? (?<amount>[a-z]+) (?<name>[^\\.]*tokens[^\\.]*) onto the battlefield( )?(?<mods>.+)?\\.",
        MagicTiming.Token,
        "Token"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final String tokenName = matcher.group("name").replace("tokens", "token");
            final MagicCardDefinition tokenDef = TokenCardDefinitions.get(tokenName);
            final List<MagicPlayMod> mods = MagicPlayMod.build(matcher.group("mods"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (int i = 0; i < amount; i++) {
                        game.doAction(new MagicPlayTokenAction(
                            event.getPlayer(),
                            tokenDef,
                            mods
                        ));
                    }
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
    MillEach(
        "Each (?<group>[^\\.]*) put(s)? the top (?<amount>[a-z]+)?( )?card(s)? of his or her library into his or her graveyard\\.", 
        MagicTiming.Draw,
        "Mill"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicTargetFilter<MagicPlayer> filter = MagicTargetFilterFactory.singlePlayer(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer player : game.filterPlayers(event.getPlayer(), filter)) {
                        game.doAction(new MagicMillLibraryAction(player, amount));
                    }
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
    Manifest(
        "manifest the top (?<amount>[a-z]+)?( )?card(s)? of your library\\.",
        MagicTiming.Token,
        "Manifest"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicManifestAction(event.getPlayer(), amount));
                }
            };
        }
    },
    SacrificeUnless(
        "pay (?<cost>[^\\.]*)\\. If you don't, sacrifice sn\\.", 
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
        "sacrifice sn at the beginning of the next end step\\.",
        MagicTiming.Removal,
        "Sacrifice",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new AddTriggerAction(event.getPermanent(), MagicAtEndOfTurnTrigger.Sacrifice));
            }
        }
    ),
    SacrificeSelfEndCombat(
        "sacrifice sn at end of combat\\.",
        MagicTiming.Removal,
        "Sacrifice",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new AddTriggerAction(event.getPermanent(), MagicAtEndOfCombatTrigger.Sacrifice));
            }
        }
    ),
    SacrificeChosen(
        "sacrifice (?<permanent>[^\\.]*)\\.",
        MagicTiming.Removal,
        "Sacrifice"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("permanent")+" you control");
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), event.getPlayer(), choice));
                }
            };
        }
    },
    EachSacrificeChosen(
        "Each (?<group>[^\\.]*) sacrifices (?<permanent>[^\\.]*)\\.",
        MagicTiming.Removal,
        "Sacrifice"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("permanent")+" you control");
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
    TargetSacrificeChosen(
        "(?<choice>[^\\.]*) sacrifices (?<permanent>[^\\.]*)\\.",
        MagicTargetHint.Negative, 
        MagicTiming.Removal, 
        "Sacrifice"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("permanent")+" you control");
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPlayer(game,new MagicPlayerAction() {
                        public void doAction(final MagicPlayer player) {
                            game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), player, choice));
                        }
                    });
                }
            };
        }
    },
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
        "regenerate " +  ARG.IT + "\\.", 
        MagicTiming.Pump, 
        "Regen"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicRegenerateAction(ARG.itPermanent(event, matcher)));
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
    RegenerateGroup(
        "regenerate each (?<group>[^\\.]*)\\.", 
        MagicTiming.Pump, 
        "Regen"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),filter);
                    for (final MagicPermanent perm : targets) {
                        game.doAction(new MagicRegenerateAction(perm));
                    }
                }
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
                    game.doAction(new AddStaticAction(event.getPermanent(), MagicStatic.SwitchPT));
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
                            game.doAction(new AddStaticAction(creature, MagicStatic.SwitchPT));
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
                        game.doAction(new AttachAction(event.getPermanent(),creature));
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
    Cipher(
        "cipher\\.",
        MagicTiming.Main,
        "Cipher",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new CipherAction(event.getCardOnStack(),event.getPlayer()));
            }
        }
    ),
    DetainChosen(
        "detain (?<choice>[^\\.]*)\\.", 
        MagicTargetHint.Negative,
        new MagicNoCombatTargetPicker(true,true,false),
        MagicTiming.FirstMain,
        "Detain",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicDetainAction(event.getPlayer(), creature));
                    }
                });
            }
        }
    ),
    CopySpell(
        "copy (?<choice>[^\\.]*)\\. You may choose new targets for (the|that) copy\\.", 
        MagicTiming.Spell,
        "Copy",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                    public void doAction(final MagicCardOnStack item) {
                        game.doAction(new MagicCopyCardOnStackAction(event.getPlayer(),item));
                    }
                });
            }
        }
    ),
    Monstrosity(
        "monstrosity (?<n>[0-9+]+)\\.", 
        MagicTiming.Pump, 
        "Monstrous"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt( matcher.group("n"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne, amount));
                    game.doAction(ChangeStateAction.Set(event.getPermanent(),MagicPermanentState.Monstrous));
                }
            };
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return new MagicCondition[] {
                MagicCondition.NOT_MONSTROUS_CONDITION,
            };
        }
    },
    ChooseOneOfThree(
        "choose one — • (?<effect1>.*) • (?<effect2>.*) • (?<effect3>.*)",
        MagicTiming.Pump,
        "Modal"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            final MagicSourceEvent e1 = MagicRuleEventAction.create(matcher.group("effect1"));
            final MagicSourceEvent e2 = MagicRuleEventAction.create(matcher.group("effect2"));
            final MagicSourceEvent e3 = MagicRuleEventAction.create(matcher.group("effect3"));
            return new MagicOrChoice(
                e1.getEvent(MagicEvent.NO_SOURCE).getChoice(),
                e2.getEvent(MagicEvent.NO_SOURCE).getChoice(),
                e3.getEvent(MagicEvent.NO_SOURCE).getChoice()
            );
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicSourceEvent e1 = MagicRuleEventAction.create(matcher.group("effect1"));
            final MagicSourceEvent e2 = MagicRuleEventAction.create(matcher.group("effect2"));
            final MagicSourceEvent e3 = MagicRuleEventAction.create(matcher.group("effect3"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.executeModalEvent(game, e1, e2, e3);
                }
            };
        }
    },
    ChooseOneOfTwo(
        "choose one — • (?<effect1>.*) • (?<effect2>.*)",
        MagicTiming.Pump,
        "Modal"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            final MagicSourceEvent e1 = MagicRuleEventAction.create(matcher.group("effect1"));
            final MagicSourceEvent e2 = MagicRuleEventAction.create(matcher.group("effect2"));
            return new MagicOrChoice(
                e1.getEvent(MagicEvent.NO_SOURCE).getChoice(),
                e2.getEvent(MagicEvent.NO_SOURCE).getChoice()
            );
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicSourceEvent e1 = MagicRuleEventAction.create(matcher.group("effect1"));
            final MagicSourceEvent e2 = MagicRuleEventAction.create(matcher.group("effect2"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.executeModalEvent(game, e1, e2);
                }
            };
        }
    },
    Rebound(
        "rebound"
    ) {
        private final MagicEventAction EVENT_ACTION = new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicCardOnStack spell = event.getCardOnStack();
                if (spell.getFromLocation() == MagicLocationType.OwnersHand) {
                    game.doAction(new ChangeCardDestinationAction(spell, MagicLocationType.Exile));
                    game.doAction(new AddTriggerAction(new MagicReboundTrigger(spell.getCard())));
                }
            }
        };
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return EVENT_ACTION;
        }
    },
    Buyback(
        "buyback"
    ) {
        private final MagicEventAction EVENT_ACTION = new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicCardOnStack spell = event.getCardOnStack();
                if (spell.isKicked()) {
                    game.doAction(new ChangeCardDestinationAction(spell, MagicLocationType.OwnersHand));
                }
            }
        };
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return EVENT_ACTION;
        }
    },
    SelfBecomes(
        "sn become(s)? a(n)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( (with|and gains) (?<ability>.*?))?(?<duration> until end of turn)?(?<additionTo>(\\. It's| that's) still.*)?\\.",
        MagicTiming.Animate,
        "Becomes"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final PermanentSpecParser spec = new PermanentSpecParser(matcher);

            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new BecomesAction(
                        event.getPermanent(),
                        spec.pt,
                        spec.colors,
                        spec.subTypes,
                        spec.types,
                        spec.abilities,
                        spec.duration,
                        spec.additionTo
                    ));
                }
            };   
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return new MagicCondition[]{
                MagicCondition.NOT_EXCLUDE_COMBAT_CONDITION
            };
        }
    },
    SelfBecomesAlt(
        "(?<duration>until end of turn, )sn becomes a(n)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( (with|and gains) (?<ability>.*?))?(?<additionTo>((\\.)? It's| that's) still.*)?\\.",
        MagicTiming.Animate,
        "Becomes"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return SelfBecomes.getAction(matcher);
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return SelfBecomes.getConditions(matcher);
        }
    },
    ChosenBecomesAddition(
        "(?<choice>[^\\.]*) become(s)?( a| an)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( (with|and gains) (?<ability>.*?))?(?<additionTo> in addition to its other [a-z]*)(?<duration> until end of turn)?\\.",
        MagicTiming.Animate,
        "Becomes"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return ChosenBecomes.getAction(matcher);
        }
    },
    ChosenBecomesAlt(
        "(?<duration>until end of turn, )(?<choice>[^\\.]*) becomes( a| an)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( (with|and gains) (?<ability>.*?))?(?<additionTo>((\\.)? It's| that's) still.*)?\\.",
        MagicTiming.Animate,
        "Becomes"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return ChosenBecomes.getAction(matcher);
        }
    },
    ChosenBecomes(
        "(?<choice>[^\\.]*) become(s)?( a| an)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( (with|and gains) (?<ability>.*?))?(?<duration> until end of turn)?(?<additionTo>(\\. It's| that's) still.*)?\\.",
        MagicTiming.Animate,
        "Becomes"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final PermanentSpecParser spec = new PermanentSpecParser(matcher);
            
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent it) {
                            game.doAction(new BecomesAction(
                                it,
                                spec.pt,
                                spec.colors,
                                spec.subTypes,
                                spec.types,
                                spec.abilities,
                                spec.duration,
                                spec.additionTo
                            ));
                        }
                    });
                }
            };   

        }
    },
    GainSelf(
        ARG.IT + " gain(s)? (?<ability>.+) until end of turn\\."
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicGainAbilityAction(ARG.itPermanent(event, matcher),abilityList));
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
    GainSelfCan(
        "sn (?<ability>can('t)? .+) this turn\\."
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
    GainProtectionChosen(
        "(?<choice>target [^\\.]*) gain(s)? protection from the color of your choice until end of turn\\.", 
        MagicTargetHint.Positive, 
        MagicTiming.Pump, 
        "Protection",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game,final MagicEvent event) {
                event.processTargetPermanent(game, new MagicPermanentAction() {
                    public void doAction(final MagicPermanent it) {
                        game.addEvent(new MagicGainProtectionFromEvent(
                            event.getSource(),
                            event.getPlayer(),
                            it
                        ));
                    }
                });
            }
        }
    ), 
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
            return "+" + capitalize(matcher.group("ability"));
        }
    },
    GainChosenAlt(
        "until end of turn, (?<choice>target [^\\.]*) gain(s)? (?<ability>.+)(\\.)?", 
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return GainChosen.getAction(matcher);
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
    GainChosenCan(
        "(?<choice>target [^\\.]*) (?<ability>can('t)? .+) this turn\\." 
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
        "(?<group>[^\\.]*) gain(s)? (?<ability>[^•]+) until end of turn\\."
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
    GainGroupAlt(
        "until end of turn, (?<group>[^\\.]*) gain (?<ability>.+)(\\.)?"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return GainGroup.getAction(matcher);
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
    GainGroupCan(
        "(?<group>[^\\.]*) (?<ability>can('t)? .+) this turn\\."
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return GainGroup.getAction(matcher);
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainChosenCan.getTiming(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainChosenCan.getName(matcher);
        }
    },
    LoseSelf(
        "sn loses (?<ability>.+) until end of turn\\."
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicLoseAbilityAction(event.getPermanent(),abilityList));
                }
            };
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainChosen.getTiming(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return "-" + capitalize(matcher.group("ability"));
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
            return new MagicCondition[]{
                MagicConditionFactory.HasAbility(ability)
            };
        }
    },
    LoseChosen(
        "(?<choice>target [^\\.]*) loses (?<ability>.+) until end of turn\\.", 
        MagicTargetHint.Negative
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent creature) {
                            game.doAction(new MagicLoseAbilityAction(creature,abilityList));
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
            final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
            return new MagicLoseAbilityTargetPicker(ability);
        }
        @Override
        public String getName(final Matcher matcher) {
            return LoseSelf.getName(matcher);
        }
    },
    Clash(
        "clash with an opponent. If you win, (?<effect>.*)",
        MagicTiming.None,
        "Clash"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            final MagicSourceEvent e = MagicRuleEventAction.create(matcher.group("effect"));
            return e.getEvent(MagicEvent.NO_SOURCE).getChoice();
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicSourceEvent e = MagicRuleEventAction.create(matcher.group("effect"));
            final MagicEventAction act = e.getAction();
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    MagicClashEvent.EventAction(act).executeEvent(game, event);
                }
            };
        }
    },
    FlipCoin(
        "Flip a coin\\.( If you win the flip, (?<win>.*?))?( If you lose the flip, (?<lose>.*))?"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            final MagicSourceEvent e = matcher.group("win") != null ?
                MagicRuleEventAction.create(matcher.group("win")):
                MagicRuleEventAction.create(matcher.group("lose"));
            return e.getEvent(MagicEvent.NO_SOURCE).getChoice();
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicSourceEvent win = matcher.group("win") != null ? MagicRuleEventAction.create(matcher.group("win")) : null;
            final MagicSourceEvent lose = matcher.group("lose") != null ? MagicRuleEventAction.create(matcher.group("lose")) : null;
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicRandom rng = new MagicRandom(event.getStateId());
                    if (rng.nextInt(2) == 0) {
                        game.logAppendMessage(event.getPlayer(), "PN wins the flip.");
                        if (win != null) {
                            win.getAction().executeEvent(game, event);
                        }
                    } else {
                        game.logAppendMessage(event.getPlayer(), "PN loses the flip.");
                        if (lose != null) {
                            lose.getAction().executeEvent(game, event);
                        }
                    }
                }
            };
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            final MagicSourceEvent e = matcher.group("win") != null ? 
                MagicRuleEventAction.create(matcher.group("win")):
                MagicRuleEventAction.create(matcher.group("lose"));
            return e.getTiming();
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final MagicSourceEvent e = matcher.group("win") != null ?
                MagicRuleEventAction.create(matcher.group("win")):
                MagicRuleEventAction.create(matcher.group("lose"));
            return e.getEvent(MagicEvent.NO_SOURCE).getTargetPicker();
        }
        @Override
        public String getName(final Matcher matcher) {
            final MagicSourceEvent e = matcher.group("win") != null ?
                MagicRuleEventAction.create(matcher.group("win")):
                MagicRuleEventAction.create(matcher.group("lose"));
            return e.getName();
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

    public static String personalize(final String text) {
        return text
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
            .replaceAll("(P|p)ut ","PN puts ")
            .replaceAll("Choose one ","Choose one\\$ ");
    }

    private static String renameThisThat(final String text) {
        final String thing = "(permanent|creature|artifact|land|player|opponent)";
        final String evenQuotes = "(?=([^\"]*'[^\"]*')*[^\"]*$)";
        return text
            .replaceAll("\\b(T|t)his " + thing + "\\b" + evenQuotes, "SN")
            .replaceAll("\\b(T|t)hat " + thing + "\\b" + evenQuotes, "RN");
    }

    private static String concat(final String part0, final String[] parts) {
        final StringBuilder res = new StringBuilder(part0);
        for (int i = 1; i < parts.length; i++) {
            res.append(' ').append(parts[i]);
        }
        return res.toString();
    }
    
    static final Pattern INTERVENING_IF = Pattern.compile("if " + ARG.COND + ", " + ARG.ANY, Pattern.CASE_INSENSITIVE);
    static final Pattern MAY_PAY = Pattern.compile("you may pay " + ARG.MANACOST + "\\. if you do, .+", Pattern.CASE_INSENSITIVE);
    
    public static MagicSourceEvent create(final String raw) {
        final String text = renameThisThat(raw);
        final String[] part = text.split("~");
        final String rule = part[0];

        // handle intervening if clause
        final Matcher ifMatcher = INTERVENING_IF.matcher(rule);
        final boolean ifMatched = ifMatcher.matches();
        final MagicCondition ifCond = ifMatched ? MagicConditionParser.build(ARG.cond(ifMatcher)) : MagicCondition.NONE;
        final String ruleWithoutIf = ifMatched ? ARG.any(ifMatcher) : rule; 

        // handle you pay 
        final Matcher mayMatcher = MAY_PAY.matcher(ruleWithoutIf);
        final boolean mayMatched = mayMatcher.matches();
        final MagicManaCost mayCost = mayMatched ? MagicManaCost.create(ARG.manacost(mayMatcher)) : MagicManaCost.ZERO;
        final String prefix = mayMatched ? "^(Y|y)ou may pay [^\\.]+\\. If you do, " : "^(Y|y)ou may ";

        final String ruleWithoutMay = ruleWithoutIf.replaceFirst(prefix, "");
        final boolean optional = ruleWithoutMay.length() < ruleWithoutIf.length();
        final String effect = ruleWithoutMay.replaceFirst("^have ", "");

        final MagicRuleEventAction ruleAction = MagicRuleEventAction.build(effect);
        final Matcher matcher = ruleAction.matched(effect);

        // action may be composed from rule and riders
        final MagicEventAction action  = computeEventAction(ruleAction.getAction(matcher), part);

        final MagicEventAction noAction  = ruleAction.getNoAction(matcher);
        final MagicTargetPicker<?> picker = ruleAction.getPicker(matcher);
        final MagicChoice choice = ruleAction.getChoice(matcher);
        final String pnMayChoice = capitalize(ruleWithoutMay).replaceFirst("\\.", "?");

        final String contextRule = concat(ruleWithoutMay, part).replace("your ","PN's ").replace("you ","PN ").replace("you.", "PN.");
        final String playerRule = personalize(text);

        if (mayCost != MagicManaCost.ZERO) {
            return new MagicSourceEvent(ruleAction, matcher) {
                @Override
                public MagicEvent getEvent(final MagicSource source, final MagicCopyable ref) {
                    return ifCond.accept(source) ? new MagicEvent(
                        source,
                        new MagicMayChoice(
                            new MagicPayManaCostChoice(mayCost),
                            choice
                        ),
                        picker,
                        ref,
                        new MagicEventAction() {
                            @Override
                            public void executeEvent(final MagicGame game, final MagicEvent event) {
                                if (ifCond.accept(event.getSource()) == false) {
                                    return;
                                }
                                if (event.isYes()) {
                                    action.executeEvent(game, event);
                                } else {
                                    noAction.executeEvent(game, event);
                                }
                            }
                        },
                        "PN may$ pay " + mayCost + "$. If you do, " + contextRule + "$"
                    ) : MagicEvent.NONE;
                }
            };
        } else if (optional) {
            return new MagicSourceEvent(ruleAction, matcher) {
                @Override
                public MagicEvent getEvent(final MagicSource source, final MagicCopyable ref) {
                    return ifCond.accept(source) ? new MagicEvent(
                        source,
                        new MagicMayChoice(
                            pnMayChoice.replaceAll("SN",source.toString()),
                            choice
                        ),
                        picker,
                        ref,
                        new MagicEventAction() {
                            @Override
                            public void executeEvent(final MagicGame game, final MagicEvent event) {
                                if (ifCond.accept(event.getSource()) == false) {
                                    return;
                                }
                                if (event.isYes()) {
                                    action.executeEvent(game, event);
                                } else {
                                    noAction.executeEvent(game, event);
                                }
                            }
                        },
                        "PN may$ " + contextRule + "$"
                    ) : MagicEvent.NONE;
                }
            };
        } else {
            return new MagicSourceEvent(ruleAction, matcher) {
                @Override
                public MagicEvent getEvent(final MagicSource source, final MagicCopyable ref) {
                    return ifCond.accept(source) ? new MagicEvent(
                        source,
                        choice,
                        picker,
                        ref,
                        new MagicEventAction() {
                            @Override
                            public void executeEvent(final MagicGame game, final MagicEvent event) {
                                if (ifCond.accept(event.getSource()) == false) {
                                    return;
                                }
                                action.executeEvent(game, event);
                            }
                        },
                        capitalize(playerRule) + "$"
                    ) : MagicEvent.NONE;
                }
            };
        }
    }
}
