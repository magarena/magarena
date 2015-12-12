package magic.model.event;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magic.data.EnglishToInt;
import magic.data.CardDefinitions;
import magic.model.ARG;
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
import magic.model.MagicMessage;
import magic.model.MagicAmount;
import magic.model.MagicAmountParser;
import magic.model.MagicAmountFactory;
import magic.model.action.*;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicChoiceFactory;
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
import magic.model.mstatic.MagicLayer;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.*;
import magic.model.trigger.AtEndOfCombatTrigger;
import magic.model.trigger.AtEndOfTurnTrigger;
import magic.model.trigger.AtUpkeepTrigger;
import magic.model.trigger.PreventDamageTrigger;
import magic.model.trigger.ReboundTrigger;

public enum MagicRuleEventAction {
    DestroyAtEndOfCombat(
        "destroy " + ARG.PERMANENTS + " at end of combat\\.",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Destroy"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doValidAction(it, new AddTurnTriggerAction(
                            it,
                            AtEndOfCombatTrigger.Destroy
                        ));
                    }
                }
            };
        }
    },
    Destroy(
        "destroy " + ARG.PERMANENTS + "\\.(?<noregen> (They|It) can't be regenerated\\.)?",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Destroy"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicPermanent> targets = ARG.permanents(event, matcher, filter);
                    if (matcher.group("noregen") != null) {
                        for (final MagicPermanent it : targets) {
                            game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                        }
                    }
                    game.doAction(new DestroyAction(targets));
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
        "counter " + ARG.CHOICE + " unless its controller pays (?<cost>[^\\.]*)\\.",
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
        "counter " + ARG.CHOICE + "\\. if that spell is countered this way, exile it instead of putting it into its owner's graveyard\\.",
        MagicTargetHint.Negative,
        MagicDefaultTargetPicker.create(),
        MagicTiming.Counter,
        "Counter",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetItemOnStack(game,new MagicItemOnStackAction() {
                    public void doAction(final MagicItemOnStack item) {
                        game.doAction(new CounterItemOnStackAction(item, MagicLocationType.Exile));
                    }
                });
            }
        }
    ),
    CounterSpell(
        "counter " + ARG.CHOICE + "\\.",
        MagicTargetHint.Negative,
        MagicDefaultTargetPicker.create(),
        MagicTiming.Counter,
        "Counter",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetItemOnStack(game,new MagicItemOnStackAction() {
                    public void doAction(final MagicItemOnStack item) {
                        game.doAction(new CounterItemOnStackAction(item));
                    }
                });
            }
        }
    ),
    FlickerYour(
        "exile " + ARG.PERMANENTS + ", then return (it|that card) to the battlefield under your control\\.",
        MagicTargetHint.Positive,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Flicker"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new RemoveFromPlayAction(
                            it,
                            MagicLocationType.Exile
                        ));
                        game.doAction(new ReturnCardAction(
                            MagicLocationType.Exile,
                            it.getCard(),
                            event.getPlayer()
                        ));
                    }
                }
            };
        }
    },
    FlickerOwner(
        "exile " + ARG.PERMANENTS + ", then return (it|that card) to the battlefield under its owner's control\\.",
        MagicTargetHint.Positive,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Flicker"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new RemoveFromPlayAction(
                            it,
                            MagicLocationType.Exile
                        ));
                        game.doAction(new ReturnCardAction(
                            MagicLocationType.Exile,
                            it.getCard(),
                            it.getOwner()
                        ));
                    }
                }
            };
        }
    },
    FlickerEndStep(
        "exile " + ARG.PERMANENTS + "\\. (if you do, )?return (those cards|the exiled card|that card|it|sn) to the battlefield under (their|its) owner's control at the beginning of the next end step\\.",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Flicker"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new ExileUntilEndOfTurnAction(it));
                    }
                }
            };
        }
    },
    ExileCards(
        "exile " + ARG.CARDS + "\\.",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Exile"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicCard> filter = ARG.cardsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicCard it : ARG.cards(event, matcher, filter)) {
                        game.doAction(new ExileLinkAction(
                            event.getSource().isPermanent() ? event.getPermanent() : MagicPermanent.NONE,
                            it,
                            it.getLocation()
                        ));
                    }
                }
            };
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            if (matcher.group("choice") != null) {
                if (matcher.group("choice").contains("your")) {
                    return MagicGraveyardTargetPicker.ExileOwn;
                } else {
                    return MagicGraveyardTargetPicker.ExileOpp;
                }
            } else {
                return MagicDefaultTargetPicker.create();
            }
        }
    },
    ExilePermanents(
        "exile " + ARG.PERMANENTS + "\\.",
        MagicTargetHint.Negative,
        MagicExileTargetPicker.create(),
        MagicTiming.Removal,
        "Exile"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent perm : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new ExileLinkAction(
                            event.getSource().isPermanent() ? event.getPermanent() : MagicPermanent.NONE,
                            perm
                        ));
                    }
                }
            };
        }
    },
    DamageChosenAndController(
        "sn deal(s)? (?<amount>[0-9]+) damage to " + ARG.CHOICE + " and (?<amount2>[0-9]+) damage to you\\.",
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
                            game.doAction(new DealDamageAction(event.getSource(),target,amount));
                            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),amount2));
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
        ARG.IT + " deal(s)? (?<amount>[0-9]+) damage to " + ARG.CHOICE + "(\\.)?",
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
                            game.doAction(new DealDamageAction(ARG.itSource(event, matcher),target,amount));
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
    DamageChosenEqual(
        ARG.IT + " deal(s)? damage equal to " + ARG.WORDRUN + " to " + ARG.CHOICE + "(\\.)?",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTarget(game,new MagicTargetAction() {
                        public void doAction(final MagicTarget target) {
                            final int amount = count.getAmount(event);
                            game.logAppendMessage(event.getPlayer(),"("+amount+")");
                            game.doAction(new DealDamageAction(
                                ARG.itSource(event, matcher),
                                target,
                                amount
                            ));
                        }
                    });
                }
            };
        }
    },
    DamageChosenEqualAlt(
        ARG.IT + " deal(s)? damage to " + ARG.CHOICE + " equal to " + ARG.WORDRUN + "(\\.)?",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return DamageChosenEqual.getAction(matcher);
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
                    game.doAction(new DealDamageAction(event.getSource(),ARG.youTarget(event, matcher),amount));
                }
            };
        }
    },
    DamageTwoGroup(
        ARG.IT + " deal(s)? (?<amount>[0-9]+) damage to (?<group1>[^\\.]*) and (?<group2>[^\\.]*)\\.",
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            final MagicTargetFilter<MagicTarget> filter1 = MagicTargetFilterFactory.Target(matcher.group("group1"));
            final MagicTargetFilter<MagicTarget> filter2 = MagicTargetFilterFactory.Target(matcher.group("group2"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicSource source = ARG.itSource(event, matcher);
                    final Collection<MagicTarget> targets1 = filter1.filter(event);
                    for (final MagicTarget target : targets1) {
                        game.doAction(new DealDamageAction(source,target,amount));
                    }
                    final Collection<MagicTarget> targets2 = filter2.filter(event);
                    for (final MagicTarget target : targets2) {
                        game.doAction(new DealDamageAction(source,target,amount));
                    }
                }
            };
        }
    },
    DamageGroup(
        ARG.IT + " deal(s)? (?<amount>[0-9]+) damage to (?<group>[^\\.]*)\\.",
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            final MagicTargetFilter<MagicTarget> filter = MagicTargetFilterFactory.Target(matcher.group("group"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final Collection<MagicTarget> targets = filter.filter(event);
                    for (final MagicTarget target : targets) {
                        game.doAction(new DealDamageAction(ARG.itSource(event, matcher),target,amount));
                    }
                }
            };
        }
    },
    PreventNextDamage(
        "prevent the next (?<amount>[0-9]+) damage that would be dealt to " + ARG.TARGETS + " this turn\\.",
        MagicTargetHint.Positive,
        MagicPreventTargetPicker.create(),
        MagicTiming.Pump,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            final MagicTargetFilter<MagicTarget> filter = ARG.targetsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicTarget it : ARG.targets(event, matcher, filter)) {
                        game.doAction(new PreventDamageAction(it, amount));
                    }
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
                    PreventDamageTrigger.PreventCombatDamage
                ));
            }
        }
    ),
    PreventAllCombatBy(
        "prevent all combat damage that would be dealt by " + ARG.PERMANENTS + " this turn\\.",
        MagicTargetHint.Negative,
        new MagicNoCombatTargetPicker(true, true, false),
        MagicTiming.Block,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    if (matcher.group("group") != null) {
                        game.doAction(new AddTurnTriggerAction(
                            PreventDamageTrigger.PreventCombatDamageDealtBy(filter)
                        ));
                    } else {
                        for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                            game.doAction(new AddTurnTriggerAction(it, PreventDamageTrigger.PreventCombatDamageDealtBy));
                        }
                    }
                }
            };
        }
    },
    PreventAllCombatTo(
        "prevent all combat damage that would be dealt to " + ARG.TARGETS + " this turn\\.",
        MagicTargetHint.Positive,
        MagicPreventTargetPicker.create(),
        MagicTiming.Block,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicTarget> filter = ARG.targetsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    if (matcher.group("group") != null) {
                        game.doAction(new AddTurnTriggerAction(
                            PreventDamageTrigger.PreventCombatDamageDealtTo(filter)
                        ));
                    } else {
                        for (final MagicTarget it : ARG.targets(event, matcher, filter)) {
                            if (it.isPermanent()) {
                                game.doAction(new AddTurnTriggerAction(
                                    (MagicPermanent)it, 
                                    PreventDamageTrigger.PreventCombatDamageDealtTo
                                ));
                            } else {
                                game.doAction(new AddTurnTriggerAction(
                                    PreventDamageTrigger.PreventCombatDamageDealtToYou((MagicPlayer)it)
                                ));
                            }
                        }
                    }
                }
            };
        }
    },
    PreventAllDamageBy(
        "prevent all damage that would be dealt by " + ARG.PERMANENTS + " this turn\\.",
        MagicTargetHint.Negative,
        new MagicNoCombatTargetPicker(true, true, false),
        MagicTiming.Pump,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    if (matcher.group("group") != null) {
                        game.doAction(new AddTurnTriggerAction(
                            PreventDamageTrigger.PreventDamageDealtBy(filter)
                        ));
                    } else {
                        for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                            game.doAction(new AddTurnTriggerAction(it, PreventDamageTrigger.PreventDamageDealtBy));
                        }
                    }
                }
            };
        }
    },
    PreventAllDamageTo(
        "prevent all damage that would be dealt to " + ARG.TARGETS + " this turn\\.",
        MagicTargetHint.Positive,
        MagicPreventTargetPicker.create(),
        MagicTiming.Pump,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicTarget> filter = ARG.targetsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    if (matcher.group("group") != null) {
                        game.doAction(new AddTurnTriggerAction(
                            PreventDamageTrigger.PreventDamageDealtTo(filter)
                        ));
                    } else {
                        for (final MagicTarget it : ARG.targets(event, matcher, filter)) {
                            if (it.isPermanent()) {
                                game.doAction(new AddTurnTriggerAction(
                                    (MagicPermanent)it,
                                    PreventDamageTrigger.PreventDamageDealtTo
                                ));
                            } else {
                                game.doAction(new AddTurnTriggerAction(
                                    PreventDamageTrigger.PreventDamageDealtToYou((MagicPlayer)it)
                                ));
                            }
                        }
                    }
                }
            };
        }
    },
    DrawLosePlayers(
        ARG.PLAYERS + " draw(s)? (?<amount>[a-z]+) card(s)? and (you )?lose(s)? (?<amount2>[0-9]+) life\\.",
        MagicTiming.Draw,
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final int amount2 = Integer.parseInt(matcher.group("amount2"));
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.doAction(new DrawAction(it, amount));
                        game.doAction(new ChangeLifeAction(it, -amount2));
                    }
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
                        AtUpkeepTrigger.YouDraw(
                            event.getSource(),
                            event.getPlayer()
                        )
                    ));
                }
            };
        }
    },
    Draw(
        ARG.PLAYERS + "( )?draw(s)? (?<amount>[a-z]+) (additional )?card(s)?( for each " + ARG.WORDRUN +")?\\.",
        MagicTargetHint.Positive,
        MagicTiming.Draw,
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final int multiplier = count.getAmount(event);
                    final int total = amount * multiplier;
                    if (count != MagicAmountFactory.One) {
                        game.logAppendMessage(event.getPlayer(), "(" + total + ")");
                    }
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.doAction(new DrawAction(it, total));
                    }
                }
            };
        }
    },
    DrawAlt(
        ARG.PLAYERS + "( )?draw(s)? (?<amount>[a-z]+)?cards equal to " + ARG.WORDRUN +"\\.",
        MagicTiming.Draw,
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return Draw.getAction(matcher);
        }
    },
    DrawDiscard(
        ARG.PLAYERS + "( )?draw(s)? (?<amount1>[a-z]+) card(s)?(, then|\\. if you do,) discard(s)? (?<amount2>[a-z]+) card(s)?(?<random> at random)?\\.",
        MagicTiming.Draw,
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount1 = EnglishToInt.convert(matcher.group("amount1"));
            final int amount2 = EnglishToInt.convert(matcher.group("amount2"));
            final boolean isRandom = matcher.group("random") != null;
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.doAction(new DrawAction(it, amount1));
                        if (isRandom) {
                            game.addEvent(MagicDiscardEvent.Random(event.getSource(), it, amount2));
                        } else {
                            game.addEvent(new MagicDiscardEvent(event.getSource(), it, amount2));
                        }
                    }
                }
            };
        }
    },
    Discard(
        ARG.PLAYERS + "( )?discard(s)? (?<amount>[a-z]+) card(s)?(?<random> at random)?( for each " + ARG.WORDRUN +")?\\.",
        MagicTargetHint.Negative,
        MagicTiming.Draw,
        "Discard"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final boolean isRandom = matcher.group("random") != null;
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final int multiplier = count.getAmount(event);
                    final int total = amount * multiplier;
                    if (count != MagicAmountFactory.One) {
                        game.logAppendMessage(event.getPlayer(), "(" + total + ")");
                    }
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        if (isRandom) {
                            game.addEvent(MagicDiscardEvent.Random(event.getSource(), it, total));
                        } else {
                            game.addEvent(new MagicDiscardEvent(event.getSource(), it, total));
                        }
                    }
                }
            };
        }
    },
    DiscardHand(
        ARG.PLAYERS + "( )?discard(s)? (your|his or her) hand\\.",
        MagicTargetHint.Negative,
        MagicTiming.Draw,
        "Discard"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.addEvent(new MagicDiscardHandEvent(event.getSource(), it));
                    }
                }
            };
        }
    },
    DrainLife(
        ARG.PLAYERS + " lose(s)? (?<amount1>[0-9]+) life and you gain (?<amount2>[0-9]+) life\\.",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "-Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount1 = Integer.parseInt(matcher.group("amount1"));
            final int amount2 = Integer.parseInt(matcher.group("amount2"));
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final List<MagicPlayer> players = ARG.players(event, matcher, filter);
                    for (final MagicPlayer it : players) {
                        game.doAction(new ChangeLifeAction(it, -amount1));
                    }
                    //continue to the second part if 
                    //  there is no target OR
                    //  there is a target and it is legal
                    if (matcher.group("choice") == null || players.isEmpty() == false) {
                        game.doAction(new ChangeLifeAction(event.getPlayer(), amount2));
                    }
                }
            };
        }
    },
    GainLife(
        ARG.PLAYERS + "( )?gain(s)? (?<amount>[0-9]+) life( for each " + ARG.WORDRUN + ")?\\.",
        MagicTargetHint.Positive,
        MagicTiming.Removal,
        "+Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final int multiplier = count.getAmount(event);
                    final int total = amount * multiplier;
                    if (count != MagicAmountFactory.One) {
                        game.logAppendMessage(event.getPlayer(), "(" + total + ")");
                    }
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.doAction(new ChangeLifeAction(it, total));
                    }
                }
            };
        }
    },
    LoseLife(
        ARG.PLAYERS + "( )?lose(s)? (?<amount>[0-9]+) life( for each " + ARG.WORDRUN + ")?\\.",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "-Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final int multiplier = count.getAmount(event);
                    final int total = amount * multiplier;
                    if (count != MagicAmountFactory.One) {
                        game.logAppendMessage(event.getPlayer(), "(" + total + ")");
                    }
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.doAction(new ChangeLifeAction(it, -total));
                    }
                }
            };
        }
    },
    Pump(
        ARG.PERMANENTS + " get(s)? (?<pt>[0-9+]+/[0-9+]+) until end of turn( for each " + ARG.WORDRUN + ")?\\.",
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
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final int amount = count.getAmount(event);
                    if (count != MagicAmountFactory.One) {
                        game.logAppendMessage(event.getPlayer(), "("+amount+")");
                    }
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new ChangeTurnPTAction(it, power * amount, toughness * amount));
                    }
                }
            };
        }
    },
    Weaken(
        ARG.PERMANENTS + " get(s)? (?<pt>[0-9-]+/[0-9-]+) until end of turn( for each " + ARG.WORDRUN + ")?\\.",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Weaken"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return Pump.getAction(matcher);
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final String[] args = matcher.group("pt").replace('+','0').split("/");
            final int p = -Integer.parseInt(args[0]);
            final int t = -Integer.parseInt(args[1]);
            return new MagicWeakenTargetPicker(p, t);
        }
    },
    ModPT(
        ARG.PERMANENTS + " get(s)? (?<pt>[0-9+-]+/[0-9+-]+) until end of turn( for each " + ARG.WORDRUN + ")?\\.",
        MagicTiming.Removal,
        "Pump"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return Pump.getAction(matcher);
        }
    },
    PumpGain(
        ARG.PERMANENTS + " get(s)? (?<pt>[0-9+]+/[0-9+]+) and (gain(s)?|is) (?<ability>.+) until end of turn(\\.)?",
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final String[] pt = matcher.group("pt").replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new ChangeTurnPTAction(it,power,toughness));
                        game.doAction(new GainAbilityAction(it,abilityList));
                    }
                }
            };
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainAbility.getTiming(matcher);
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return GainAbility.getPicker(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainAbility.getName(matcher);
        }
    },
    PumpGainAlt(
        "until end of turn, " + ARG.PERMANENTS + " get(s)? (?<pt>[0-9+]+/[0-9+]+) and (gain(s)?|is) (?<ability>.+)(\\.)?",
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return PumpGain.getAction(matcher);
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainAbility.getTiming(matcher);
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return GainAbility.getPicker(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainAbility.getName(matcher);
        }
    },
    PumpGainCan(
        ARG.PERMANENTS + " get(s)? (?<pt>[0-9+]+/[0-9+]+) (until end of turn and|and) (?<ability>can('t)? .+) this turn\\.",
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return PumpGain.getAction(matcher);
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainAbility.getTiming(matcher);
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return GainAbility.getPicker(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainAbility.getName(matcher);
        }
    },
    ModPTGain(
        ARG.PERMANENTS + " get(s)? (?<pt>[0-9+-]+/[0-9+-]+) and gains (?<ability>.+) until end of turn\\.",
        MagicTiming.Removal
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return PumpGain.getAction(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainAbility.getName(matcher);
        }
    },
    ModPTGainCan(
        ARG.PERMANENTS + " get(s)? (?<pt>[0-9+-]+/[0-9+-]+) and (?<ability>can('t)? .+) this turn\\.",
        MagicTiming.Removal
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return PumpGain.getAction(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainAbility.getName(matcher);
        }
    },
    PutCounter(
        "put (?<amount>[a-z]+) (?<type>[^ ]+) counter(s)? on " + ARG.PERMANENTS + "\\."
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new ChangeCountersAction(
                            it,
                            counterType,
                            amount
                        ));
                    }
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
            return (amount>1) ? "+Counters" : "+Counter";
        }
    },
    CounterFromSelfClockwork(
        "remove a \\+1\\/\\+1 counter from (sn|it) at end of combat\\.",
        MagicTiming.Pump,
        "Remove",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game,final MagicEvent event) {
                game.doAction(new AddTurnTriggerAction(event.getPermanent(), AtEndOfCombatTrigger.Clockwork));
            }
        }
    ),
    RemoveCounter(
        "remove (?<amount>[a-z]+) (?<type>[^ ]+) counter(s)? from " +  ARG.PERMANENTS + "\\.",
        MagicTiming.Pump
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new ChangeCountersAction(
                            it,
                            counterType,
                            -amount
                        ));
                    }
                }
            };
        }
        @Override
        public String getName(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            if (amount>1) {
                final String name = "-Counters";
                return name;
            } else {
                final String name = "-Counter";
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
                    final Collection<MagicPermanent> targets = MagicTargetFilterFactory.CREATURE_YOU_CONTROL.filter(event);
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
                game.doAction(new ShiftCardAction(
                    event.getCard(),
                    MagicLocationType.Graveyard,
                    MagicLocationType.OwnersHand
                ));
            }
        }
    ),
    ReanimateCardSelf(
        "return sn from your graveyard to the battlefield(\\. | )?(?<mods>.+)?\\.",
        MagicTiming.Token,
        "Reanimate"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final List<MagicPlayMod> mods = MagicPlayMod.build(matcher.group("mods"));
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new ReanimateAction(
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
                game.doAction(new ShiftCardAction(
                    event.getPermanent().getCard(),
                    MagicLocationType.Graveyard,
                    MagicLocationType.OwnersHand
                ));
            }
        }
    ),
    RecoverCards(
        "return " + ARG.CARDS + " to (your|its owner's) hand\\.",
        MagicTargetHint.Positive,
        MagicGraveyardTargetPicker.ReturnToHand,
        MagicTiming.Draw,
        "Return"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicCard> filter = ARG.cardsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicCard it : ARG.cards(event, matcher, filter)) {
                        final MagicLocationType from = it.getLocation();
                        game.doAction(new ShiftCardAction(it, from, MagicLocationType.OwnersHand));
                    }
                }
            };
        }
    },
    ReclaimCards(
        "put " + ARG.CARDS + " on top of (your|its owner's) library\\.",
        MagicTargetHint.Positive,
        MagicGraveyardTargetPicker.ReturnToHand,
        MagicTiming.Draw,
        "Reclaim"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicCard> filter = ARG.cardsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicCard it : ARG.cards(event, matcher, filter)) {
                        final MagicLocationType from = it.getLocation();
                        game.doAction(new ShiftCardAction(it, from, MagicLocationType.TopOfOwnersLibrary));
                    }
                }
            };
        }
    },
    TuckCards(
        "put " + ARG.CARDS + " on the bottom of (your|its owner's) library\\.",
        MagicTargetHint.Negative,
        MagicGraveyardTargetPicker.ExileOpp,
        MagicTiming.Draw,
        "Tuck"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicCard> filter = ARG.cardsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicCard it : ARG.cards(event, matcher, filter)) {
                        final MagicLocationType from = it.getLocation();
                        game.doAction(new ShiftCardAction(it, from, MagicLocationType.BottomOfOwnersLibrary));
                    }
                }
            };
        }
    },
    Bounce(
        "return " + ARG.PERMANENTS + " to (your hand|its owner's hand|their owner's hand|their owners' hands)\\.",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new RemoveFromPlayAction(it, MagicLocationType.OwnersHand));
                    }
                }
            };
        }
    },
    BounceEndOfCombat(
        "return " + ARG.PERMANENTS + " to (your hand|its owner's hand|their owner's hand|their owners' hands) at end of combat\\.",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doValidAction(it, new AddTriggerAction(
                            it,
                            AtEndOfCombatTrigger.Return
                        ));
                    }
                }
            };
        }
    },
    BounceLibTop(
        "put " + ARG.PERMANENTS + " on top of (its owner's library|his or her library|their owners' libraries)\\.",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new RemoveFromPlayAction(it,MagicLocationType.TopOfOwnersLibrary));
                    }
                }
            };
        }
    },
    BounceLibBottom(
        "put " + ARG.PERMANENTS + " on the bottom of (its owner's library|his or her library|their owners' libraries)\\.",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new RemoveFromPlayAction(it,MagicLocationType.BottomOfOwnersLibrary));
                    }
                }
            };
        }
    },
    RevealToHand(
        "reveal the top " + ARG.AMOUNT + " cards of your library\\. Put all " +  ARG.WORDRUN + " revealed this way into your hand and the rest on the bottom of your library in any order\\.",
        MagicTiming.Draw,
        "Reveal"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int n = ARG.amount(matcher);
            final MagicTargetFilter<MagicCard> filter = MagicTargetFilterFactory.Card(ARG.wordrun(matcher) + " from your hand");
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final List<MagicCard> topN = event.getPlayer().getLibrary().getCardsFromTop(n);
                    game.doAction(new RevealAction(topN));
                    for (final MagicCard it : topN) {
                        game.doAction(new ShiftCardAction(
                            it,
                            MagicLocationType.OwnersLibrary,
                            filter.accept(event.getSource(), event.getPlayer(), it) ?
                                MagicLocationType.OwnersHand :
                                MagicLocationType.BottomOfOwnersLibrary
                        ));
                    }
                }
            };
        }
    },
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
    SearchLibraryToHandHidden(
        "search your library for (?<card>[^\\.]*)( and|,) put (it|that card) into your hand(.|,) (If you do, |(t|T)hen )shuffle your library\\.",
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
                        MagicLocationType.OwnersHand,
                        false
                    ));
                }
            };
        }
    },
    SearchMultiLibraryToHand(
        "search your library for up to (?<amount>[a-z]+) (?<card>[^\\.]*), reveal (them|those cards), (and )?put them into your hand(.|,) (If you do, |(t|T)hen )shuffle your library\\.",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicCard> filter = MagicTargetFilterFactory.Card(matcher.group("card") + " from your library");
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
        "search your library for (?<card>[^\\.]*)(,| and) reveal (it(,|\\.)|that card\\.)( then)? (S|s)huffle your library(, then| and) put (that|the) card on top of it\\.",
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
        "search your library for (?<card>[^\\.]*) and put (that card|it) into your graveyard\\. (If you do,|Then) shuffle your library\\.",
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
            final MagicTargetFilter<MagicCard> filter = MagicTargetFilterFactory.Card(matcher.group("card") + " from your library");
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
        "put (?<card>[^\\.]*hand) onto the battlefield(\\. | )?(?<mods>.+)?\\.",
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
        "return " + ARG.GRAVEYARD + " to the battlefield(\\. | )?(?<mods>.+)?\\.",
        MagicTargetHint.None,
        MagicGraveyardTargetPicker.PutOntoBattlefield,
        MagicTiming.Token,
        "Reanimate"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final List<MagicPlayMod> mods = MagicPlayMod.build(matcher.group("mods"));
            return new MagicEventAction () {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetCard(game,new MagicCardAction() {
                        public void doAction(final MagicCard card) {
                            game.doAction(new ReanimateAction(
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
        "put " + ARG.GRAVEYARD + " onto the battlefield under your control(\\. | )?(?<mods>.+)?\\.",
        MagicTargetHint.None,
        MagicGraveyardTargetPicker.PutOntoBattlefield,
        MagicTiming.Token,
        "Reanimate"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return Reanimate.getAction(matcher);
        }
    },
    TapOrUntapChosen(
        "tap or untap " + ARG.CHOICE + "\\.",
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
    TapParalyze(
        "tap " + ARG.PERMANENTS + "( and it|\\. RN|\\. it|\\. Those creatures) (doesn't|don't) untap during (its|their) controller's next untap step\\.",
        MagicTargetHint.Negative,
        MagicTapTargetPicker.Tap,
        MagicTiming.Tapping,
        "Tap"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new TapAction(it));
                        game.doAction(ChangeStateAction.Set(
                            it,
                            MagicPermanentState.DoesNotUntapDuringNext
                        ));
                    }
                }
            };
        }
    },
    Tap(
        "tap " + ARG.PERMANENTS + "\\.",
        MagicTargetHint.Negative,
        MagicTapTargetPicker.Tap,
        MagicTiming.Tapping,
        "Tap"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new TapAction(it));
                    }
                }
            };
        }
    },
    Paralyze(
        ARG.PERMANENTS + " doesn't untap during (your|its controller's) next untap step\\.",
        MagicTargetHint.Negative,
        new MagicNoCombatTargetPicker(true,true,false),
        MagicTiming.Tapping,
        "Paralyze"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(ChangeStateAction.Set(
                            it,
                            MagicPermanentState.DoesNotUntapDuringNext
                        ));
                    }
                }
            };
        }
    },
    Untap(
        "untap " + ARG.PERMANENTS + "\\.",
        MagicTargetHint.Positive,
        MagicTapTargetPicker.Untap,
        MagicTiming.Tapping,
        "Untap"
    ) {
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new UntapAction(it));
                    }
                }
            };
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return matcher.group("sn") != null ? 
                new MagicCondition[] { new MagicArtificialCondition(MagicCondition.TAPPED_CONDITION) } :
                MagicActivation.NO_COND;
        }
    },
    PutTokens(
        "put(s)? (?<amount>[a-z]+) (?<name>[^\\.]*token[^\\.]*) onto the battlefield(\\. | )?(?<mods>.+?)??( )?(for each " + ARG.WORDRUN + ")?\\.",
        MagicTiming.Token,
        "Token"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final String tokenName = matcher.group("name").replace("tokens", "token");
            final MagicCardDefinition tokenDef = CardDefinitions.getToken(tokenName);
            final List<MagicPlayMod> mods = MagicPlayMod.build(matcher.group("mods"));
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final int multiplier = count.getAmount(event);
                    final int total = amount * multiplier;
                    if (count != MagicAmountFactory.One) {
                        game.logAppendMessage(event.getPlayer(), "(" + total + ")");
                    }
                    for (int i = 0; i < total; i++) {
                        game.doAction(new PlayTokenAction(
                            event.getPlayer(),
                            tokenDef,
                            mods
                        ));
                    }
                }
            };
        }
    },
    Mill(
        ARG.PLAYERS + "( )?put(s)? the top (?<amount>[a-z]+)?( )?card(s)? of (your|his or her) library into (your|his or her) graveyard\\.",
        MagicTiming.Draw,
        "Mill"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = EnglishToInt.convert(matcher.group("amount"));
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.doAction(new MillLibraryAction(it, amount));
                    }
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
                    game.doAction(new ManifestAction(event.getPlayer(), amount));
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
                    game.doAction(new SacrificeAction(event.getPermanent()));
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
                game.doAction(new SacrificeAction(event.getPermanent()));
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
                game.doAction(new AddTriggerAction(event.getPermanent(), AtEndOfTurnTrigger.Sacrifice));
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
                game.doAction(new AddTriggerAction(event.getPermanent(), AtEndOfCombatTrigger.Sacrifice));
            }
        }
    ),
    SacrificeChosen(
        ARG.PLAYERS + "( )?sacrifice(s)? (?<permanent>[^\\.]*)\\.",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Sacrifice"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("permanent")+" you control");
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), it, choice));
                    }
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
    PseudoScry(
       "Look at the top card of your library\\. You may put that card on the bottom of your library\\.",
       MagicTiming.Draw,
       "Scry"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(MagicScryEvent.Pseudo(event));
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
        "look at " + ARG.CHOICE + "'s hand\\.",
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
    Regenerate(
        "regenerate " + ARG.PERMANENTS + "\\.",
        MagicTargetHint.Positive,
        MagicRegenerateTargetPicker.create(),
        MagicTiming.Pump,
        "Regen"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new RegenerateAction(it));
                    }
                }
            };
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return matcher.group("sn") != null ? 
                new MagicCondition[] { MagicCondition.CAN_REGENERATE_CONDITION } :
                MagicActivation.NO_COND;
        }
    },
    SwitchPT(
        "switch " + ARG.PERMANENTS + "'s power and toughness until end of turn\\.",
        MagicTargetHint.None,
        MagicDefaultPermanentTargetPicker.create(),
        MagicTiming.Pump,
        "Switch"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new AddStaticAction(it, MagicStatic.SwitchPT));
                    }
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
                game.doAction(new RemoveFromPlayAction(event.getPermanent(),MagicLocationType.OwnersLibrary));
            }
        }
    ),
    AttachSelf(
        "attach sn to " + ARG.CHOICE + "\\.",
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
    TurnFaceDown(
        "turn " + ARG.PERMANENTS + " face down\\.",
        MagicTiming.Tapping,
        "Face Down"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new TurnFaceDownAction(it));
                    }
                }
            };
        }
    },
    TurnFaceUp(
        "turn " + ARG.PERMANENTS + " face up\\.",
        MagicTiming.Tapping,
        "Face Up"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new TurnFaceUpAction(it));
                    }
                }
            };
        }
    },
    FlipSelf(
        "flip sn\\.",
        MagicTiming.Pump,
        "Flip",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new FlipAction(event.getPermanent()));
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
                game.doAction(new TransformAction(event.getPermanent()));
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
        "detain " + ARG.CHOICE + "\\.",
        MagicTargetHint.Negative,
        new MagicNoCombatTargetPicker(true,true,false),
        MagicTiming.FirstMain,
        "Detain",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new DetainAction(event.getPlayer(), creature));
                    }
                });
            }
        }
    ),
    CopySpell(
        "copy " + ARG.CHOICE + "\\. You may choose new targets for (the|that) copy\\.",
        MagicTiming.Spell,
        "Copy",
        new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                    public void doAction(final MagicCardOnStack item) {
                        game.doAction(new CopyCardOnStackAction(event.getPlayer(),item));
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
        "choose one — \\(1\\) (?<effect1>.*) \\(2\\) (?<effect2>.*) \\(3\\) (?<effect3>.*)",
        MagicTiming.Pump,
        "Modal"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            final MagicSourceEvent e1 = MagicRuleEventAction.create(matcher.group("effect1"));
            final MagicSourceEvent e2 = MagicRuleEventAction.create(matcher.group("effect2"));
            final MagicSourceEvent e3 = MagicRuleEventAction.create(matcher.group("effect3"));
            return new MagicOrChoice(e1.getChoice(), e2.getChoice(), e3.getChoice());
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
        "choose one — \\(1\\) (?<effect1>.*) \\(2\\) (?<effect2>.*)",
        MagicTiming.Pump,
        "Modal"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            final MagicSourceEvent e1 = MagicRuleEventAction.create(matcher.group("effect1"));
            final MagicSourceEvent e2 = MagicRuleEventAction.create(matcher.group("effect2"));
            return new MagicOrChoice(e1.getChoice(), e2.getChoice());
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
                    game.doAction(new AddTriggerAction(new ReboundTrigger(spell.getCard())));
                }
            }
        };
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return EVENT_ACTION;
        }
    },
    BecomesAlt(
        "(?<duration>until end of turn, )" + ARG.PERMANENTS + " becomes( a| an)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( (with|and gains) (?<ability>.*?))?(?<additionTo>((\\.)? It's| that's) still [^\\.]*)?\\.",
        MagicTiming.Animate,
        "Becomes"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return Becomes.getAction(matcher);
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return Becomes.getConditions(matcher);
        }
    },
    BecomesAddition(
        ARG.PERMANENTS + " become(s)?( a| an)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( (with|and gains) (?<ability>.*?))?(?<additionTo> in addition to its other [a-z]*)(?<duration> until end of turn)?\\.",
        MagicTiming.Animate,
        "Becomes"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return Becomes.getAction(matcher);
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return Becomes.getConditions(matcher);
        }
    },
    Becomes(
        ARG.PERMANENTS + " become(s)?( a| an)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( (with|and gains) (?<ability>.*?))?(?<duration> until end of turn)?(?<additionTo>(\\. It's| that's) still [^\\.]*)?\\.",
        MagicTiming.Animate,
        "Becomes"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final PermanentSpecParser spec = new PermanentSpecParser(matcher);
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);

            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
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
                }
            };
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return matcher.group("sn") != null ? 
                new MagicCondition[]{ MagicCondition.NOT_EXCLUDE_COMBAT_CONDITION } :
                MagicActivation.NO_COND;
        }
    },
    GainProtection(
        ARG.PERMANENTS + " gain(s)? protection from the color of your choice until end of turn\\.",
        MagicTargetHint.Positive,
        MagicTiming.Pump,
        "Protection"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.addEvent(new MagicGainProtectionFromEvent(
                            event.getSource(),
                            event.getPlayer(),
                            it
                        ));
                    }
                }
            };
        }
    },
    GainAbility(
        ARG.PERMANENTS + " gain(s)? (?<ability>.+) until end of turn\\.",
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new GainAbilityAction(it, abilityList));
                    }
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
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            if (matcher.group("sn") != null) {
                final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
                return new MagicCondition[]{
                    MagicConditionFactory.NoAbility(ability)
                };
            } else {
                return MagicActivation.NO_COND;
            }
        }
    },
    GainAbilityAlt(
        "until end of turn, " + ARG.PERMANENTS + " gain(s)? (?<ability>.+)(\\.)?",
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return GainAbility.getAction(matcher);
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainAbility.getTiming(matcher);
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return GainAbility.getPicker(matcher);
        }
        @Override
        public String getName(final Matcher matcher) {
            return GainAbility.getName(matcher);
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return GainAbility.getConditions(matcher);
        }
    },
    GainAbilityCan(
        ARG.PERMANENTS + " (?<ability>can('t)? .+) this turn\\."
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    if (filter.isStatic()) {
                        for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                            game.doAction(new GainAbilityAction(it, abilityList));
                        }
                    } else {
                        final int pIdx = event.getPlayer().getIndex();
                        game.doAction(new AddStaticAction(new MagicStatic(MagicLayer.Game, MagicStatic.UntilEOT) {
                            @Override
                            public void modGame(final MagicPermanent source, final MagicGame game) {
                                for (final MagicPermanent it : filter.filter(game.getPlayer(pIdx))) {
                                    it.addAbility(abilityList);
                                }
                            }
                        }));
                    }
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
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return GainAbility.getConditions(matcher);
        }
    },
    LoseAbility(
        ARG.PERMANENTS + " loses (?<ability>.+) until end of turn\\.",
        MagicTargetHint.Negative
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new LoseAbilityAction(it, abilityList));
                    }
                }
            };
        }
        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            return GainAbility.getTiming(matcher);
        }
        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
            return new MagicLoseAbilityTargetPicker(ability);
        }
        @Override
        public String getName(final Matcher matcher) {
            return "-" + capitalize(matcher.group("ability"));
        }
        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            if (matcher.group("sn") != null) {
                final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
                return new MagicCondition[]{
                    MagicConditionFactory.HasAbility(ability)
                };
            } else {
                return MagicActivation.NO_COND;
            }
        }
    },
    GainControl(
        "gain control of " + ARG.PERMANENTS + "(?<ueot> until end of turn)?\\.",
        MagicTargetHint.Negative,
        MagicExileTargetPicker.create(),
        MagicTiming.Removal,
        "Control"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final boolean duration = matcher.group("ueot") != null ? MagicStatic.UntilEOT : MagicStatic.Forever;
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new GainControlAction(event.getPlayer(), it, duration));
                    }
                }
            };
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
            return e.getChoice();
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
            return e.getChoice();
        }
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicEventAction winAction = matcher.group("win") != null ?
                MagicRuleEventAction.create(matcher.group("win")).getAction() :
                MagicEventAction.NONE;

            final MagicEventAction loseAction = matcher.group("lose") != null ?
                MagicRuleEventAction.create(matcher.group("lose")).getAction() :
                MagicEventAction.NONE;

            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.addEvent(new MagicCoinFlipEvent(event, winAction, loseAction));
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
            return e.getPicker();
        }
        @Override
        public String getName(final Matcher matcher) {
            final MagicSourceEvent e = matcher.group("win") != null ?
                MagicRuleEventAction.create(matcher.group("win")):
                MagicRuleEventAction.create(matcher.group("lose"));
            return e.getName();
        }
    },
    Poison(
        ARG.PLAYERS + " get(s)? " + ARG.AMOUNT + " poison counter(s)?\\.",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Poison"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.doAction(new ChangePoisonAction(it, amount));
                    }
                }
            };
        }
    },
    LoseGame(
        ARG.PLAYERS + " lose(s)? the game\\.",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Lose"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.doAction(new LoseGameAction(it, LoseGameAction.EFFECT_REASON));
                    }
                }
            };
        }
    },
    WinGame(
        ARG.PLAYERS + " win(s)? the game\\.",
        MagicTargetHint.Positive,
        MagicTiming.Removal,
        "Win"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.doAction(new LoseGameAction(it.getOpponent(), LoseGameAction.EFFECT_REASON));
                    }
                }
            };
        }
    },
    ExtraTurn(
        ARG.PLAYERS + "( )?take(s)? (?<amount>[a-z]+) extra turn(s)? after this one\\.",
        MagicTargetHint.Positive,
        MagicTiming.SecondMain,
        "+Turn"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                        game.doAction(new ChangeExtraTurnsAction(it, amount));
                    }
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
        this(aPattern, MagicTargetHint.None, MagicDefaultTargetPicker.create(), MagicTiming.None, "", MagicEventAction.NONE);
    }

    private MagicRuleEventAction(final String aPattern, final MagicTargetHint aHint) {
        this(aPattern, aHint, MagicDefaultTargetPicker.create(), MagicTiming.None, "", MagicEventAction.NONE);
    }

    private MagicRuleEventAction(final String aPattern, final MagicTiming timing) {
        this(aPattern, MagicTargetHint.None, MagicDefaultTargetPicker.create(), timing, "", MagicEventAction.NONE);
    }

    private MagicRuleEventAction(final String aPattern, final MagicTiming aTiming, final String aName) {
        this(aPattern, MagicTargetHint.None, MagicDefaultTargetPicker.create(), aTiming, aName, MagicEventAction.NONE);
    }

    private MagicRuleEventAction(final String aPattern, final MagicTiming aTiming, final String aName, final MagicEventAction aAction) {
        this(aPattern, MagicTargetHint.None, MagicDefaultTargetPicker.create(), aTiming, aName, aAction);
    }

    private MagicRuleEventAction(
        final String aPattern,
        final MagicTargetHint aHint,
        final MagicTargetPicker<?> aPicker,
        final MagicTiming aTiming,
        final String aName
    ) {
        this(aPattern, aHint, aPicker, aTiming, aName, MagicEventAction.NONE);
    }

    private MagicRuleEventAction(
        final String aPattern,
        final MagicTargetHint aHint,
        final MagicTiming aTiming,
        final String aName
    ) {
        this(aPattern, aHint, MagicDefaultTargetPicker.create(), aTiming, aName, MagicEventAction.NONE);
    }

    private MagicRuleEventAction(
        final String aPattern,
        final MagicTargetHint aHint,
        final MagicTiming aTiming,
        final String aName,
        final MagicEventAction aAction
    ) {
        this(aPattern, aHint, MagicDefaultTargetPicker.create(), aTiming, aName, aAction);
    }

    private MagicRuleEventAction(
        final String aPattern,
        final MagicTargetHint aHint,
        final MagicTargetPicker<?> aPicker,
        final MagicTiming aTiming,
        final String aName,
        final MagicEventAction aAction
    ) {
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
        return MagicEventAction.NONE;
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
            return matcher.group("choice") != null ? 
                new MagicTargetChoice(getHint(matcher), matcher.group("choice")) :
                MagicChoice.NONE;
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

    static final Pattern TARGET_REFERENCE = Pattern.compile("\\*([^*]+)\\*");

    private static MagicEventAction computeEventAction(final MagicEventAction main, final String[] part) {
        if (part.length > 1) {
            final MagicEventAction[] acts = new MagicEventAction[part.length];
            acts[0] = main;
            for (int i = 1; i < part.length; i++) {
                final Matcher matcher = TARGET_REFERENCE.matcher(part[i]);
                final boolean hasReference = matcher.find();
                final String rider = (hasReference && matcher.group().contains("player")) ?
                        matcher.replaceAll("target player") :
                        matcher.replaceAll("target permanent");
                final MagicSourceEvent riderSourceEvent = MagicRuleEventAction.create(rider);

                //rider cannot have choices
                if (hasReference == false && riderSourceEvent.getChoice().isValid()) {
                    throw new RuntimeException("rider should not have choice: \"" + part[i] + "\"");
                }

                acts[i] = riderSourceEvent.getEventAction();
                part[i] = matcher.replaceAll("$1");
            }
            return MagicEventActionFactory.compose(acts);
        } else {
            return main;
        }
    }

    public static String personalize(final MagicChoice choice, final String text) {
        final String withIndicator = addChoiceIndicator(choice, text);
        return withIndicator
            .replaceAll("discard ","discards ")
            .replaceAll("reveal ","reveals ")
            .replaceAll("(S|s)earch your ", "PN searches his or her ")
            .replaceAll("(S|s)huffle your ","PN shuffles his or her ")
            .replaceAll("(Y|y)ou draw","PN draws")
            .replaceAll("(D|d)raw ","PN draws ")
            .replaceAll("(S|s)acrifice ","PN sacrifices ")
            .replaceAll("(Y|y)ou don't","PN doesn't")
            .replaceAll("(Y|y)ou do","PN does")
            .replaceAll("(Y|y)ou gain ","PN gains ")
            .replaceAll("(Y|y)ou lose ","PN loses ")
            .replaceAll("(Y|y)ou control","PN controls")
            .replaceAll("(Y|y)our ","PN's ")
            .replaceAll("(Y|y)ou\\b","PN")
            .replaceAll("(P|p)ut ","PN puts ")
            .replaceAll("(C|c)hoose one ","$1hoose one\\$ ")
            ;
    }

    public static String mayTense(final String text) {
        return text
            .replaceAll("PN's hand ","his or her hand ")
            .replaceAll("PN searches ","search ")
            .replaceAll("PN shuffles ","shuffle ")
            .replaceAll("PN draws","draw ")
            .replaceAll("(D|d)raws ","$1raw ")
            .replaceAll("(S|s)acrifices ","$1acrifice ")
            .replaceAll("(G|g)ains ","$1ain ")
            .replaceAll("(L|l)oses ","$1lose ")
            .replaceAll("PN puts ","put ")
            .replaceAll("reveals ","reveal ")
            .replaceAll("you don't","he or she doesn't")
            .replaceFirst("^PN ","")
            ;
    }

    public static String addChoiceIndicator(final MagicChoice choice, final String text) {
        final MagicTargetChoice tchoice = choice.getTargetChoice();
        if (tchoice.isValid()) {
            final String desc = tchoice.getTargetDescription();
            return text.replace(desc, desc + "$");
        } else if (choice.isValid()) {
            //replace final period with target indicator and period
            return text.replaceAll("\\.$", "\\$.");
        } else {
            return text;
        }
    }

    private static String renameThisThat(final String text) {
        final String thing = "(permanent|creature|artifact|land|player|opponent)";
        final String evenQuotes = "(?=([^\"]*'[^\"]*')*[^\"]*$)";
        return text
            .replaceAll("\\b(T|t)his " + thing + "( |\\.)" + evenQuotes, "SN$3")
            .replaceAll("\\b(T|t)hat " + thing + "( |\\.)" + evenQuotes, "RN$3");
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
        final Matcher mayPayMatcher = MAY_PAY.matcher(ruleWithoutIf);
        final boolean mayPayMatched = mayPayMatcher.matches();
        final MagicManaCost mayCost = mayPayMatched ? MagicManaCost.create(ARG.manacost(mayPayMatcher)) : MagicManaCost.ZERO;
        final String prefix = mayPayMatched ? "^(Y|y)ou may pay [^\\.]+\\. If you do, " : "^(Y|y)ou may ";

        final String ruleWithoutMay = ruleWithoutIf.replaceFirst(prefix, "");
        final boolean optional = ruleWithoutMay.length() < ruleWithoutIf.length();
        final String effect = ruleWithoutMay.replaceFirst("^have ", "");

        final MagicRuleEventAction ruleAction = build(effect);
        final Matcher matcher = ruleAction.matched(effect);

        // action may be composed from rule and riders
        final MagicEventAction action  = computeEventAction(ruleAction.getAction(matcher), part);

        final MagicEventAction noAction  = ruleAction.getNoAction(matcher);
        final MagicTargetPicker<?> picker = ruleAction.getPicker(matcher);
        final MagicChoice choice = ruleAction.getChoice(matcher);
        final String pnMayChoice = capitalize(ruleWithoutMay).replaceFirst("\\.", "?");

        final String contextRule = personalize(choice, concat(ruleWithoutMay, part));
        final String playerRule = personalize(choice, concat(rule, part));

        final MagicChoiceFactory choiceFact = (source, player, ref) -> {
            if (mayPayMatched) {
                return new MagicMayChoice(
                    MagicMessage.replaceName(pnMayChoice, source, player, ref),
                    new MagicPayManaCostChoice(mayCost),
                    choice
                );
            } else if (optional) {
                return new MagicMayChoice(
                    MagicMessage.replaceName(pnMayChoice, source, player, ref),
                    choice
                );
            } else {
                return choice;
            }
        };

        final String eventDesc = mayPayMatched ? "PN may$ pay " + mayCost + "$. If PN does, " + contextRule
                               : optional      ? "PN may$ " + mayTense(contextRule)
                               : capitalize(playerRule);

        return new MagicSourceEvent(
            ruleAction,
            matcher,
            ifCond,
            choiceFact,
            picker,
            new MagicEventAction() {
                @Override
                public void executeEvent(MagicGame game, MagicEvent event) {
                    if (ifCond.accept(event.getSource()) == false) {
                        return;
                    }
                    if (optional == false || event.isYes()) {
                        action.executeEvent(game, event);
                    } else {
                        noAction.executeEvent(game, event);
                    }
                }
            },
            eventDesc
        );
    }
}
