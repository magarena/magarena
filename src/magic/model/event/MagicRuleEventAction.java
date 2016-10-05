package magic.model.event;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magic.data.CardDefinitions;
import magic.model.*;
import magic.model.action.*;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicChoiceFactory;
import magic.model.choice.MagicFromCardFilterChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicOrChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.condition.MagicConditionParser;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.*;
import magic.model.trigger.*;

public enum MagicRuleEventAction {
    ChooseOneOfFour(
        "choose one — \\(1\\) (?<effect1>.*) \\(2\\) (?<effect2>.*) \\(3\\) (?<effect3>.*) \\(4\\) (?<effect4>.*)",
        MagicTiming.Pump,
        "Modal"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            final MagicSourceEvent e1 = MagicRuleEventAction.create(matcher.group("effect1"));
            final MagicSourceEvent e2 = MagicRuleEventAction.create(matcher.group("effect2"));
            final MagicSourceEvent e3 = MagicRuleEventAction.create(matcher.group("effect3"));
            final MagicSourceEvent e4 = MagicRuleEventAction.create(matcher.group("effect4"));
            return new MagicOrChoice(e1.getChoice(), e2.getChoice(), e3.getChoice(), e4.getChoice());
        }

        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicSourceEvent e1 = MagicRuleEventAction.create(matcher.group("effect1"));
            final MagicSourceEvent e2 = MagicRuleEventAction.create(matcher.group("effect2"));
            final MagicSourceEvent e3 = MagicRuleEventAction.create(matcher.group("effect3"));
            final MagicSourceEvent e4 = MagicRuleEventAction.create(matcher.group("effect4"));
            return (game, event) -> event.executeModalEvent(game, e1, e2, e3, e4);
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
            return (game, event) -> event.executeModalEvent(game, e1, e2, e3);
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
            return (game, event) -> event.executeModalEvent(game, e1, e2);
        }
    },
    DestroyAtEndOfCombat(
        "destroy " + ARG.PERMANENTS + " at end of combat",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Destroy"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new AddTurnTriggerAction(
                        it,
                        AtEndOfCombatTrigger.Destroy
                    ));
                }
            };
        }
    },
    DestroyAtEndOfTurn(
        "destroy " + ARG.PERMANENTS + " at the beginning of the next end step",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Destroy"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new AddTurnTriggerAction(
                        it,
                        AtEndOfTurnTrigger.Destroy
                    ));
                }
            };
        }
    },
    Destroy(
        "destroy " + ARG.PERMANENTS + "(|\\.| and)(?<noregen> (They|It|That creature) can't be regenerated\\.)?",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Destroy"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                final Collection<MagicPermanent> targets = ARG.permanents(event, matcher, filter);
                if (matcher.group("noregen") != null) {
                    for (final MagicPermanent it : targets) {
                        game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                    }
                }
                game.doAction(new DestroyAction(targets));
            };
        }

        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return matcher.group("noregen") == null ? MagicDestroyTargetPicker.Destroy : MagicDestroyTargetPicker.DestroyNoRegen;
        }
    },
    CounterUnless(
        "counter " + ARG.ITEMS + " unless its controller pays (?<cost>[^\\.]*)",
        MagicTargetHint.Negative,
        MagicTiming.Counter,
        "Counter"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicManaCost cost = MagicManaCost.create(matcher.group("cost"));
            final MagicTargetFilter<MagicItemOnStack> filter = ARG.itemsParse(matcher);
            return (game, event) -> {
                for (final MagicItemOnStack item : ARG.items(event, matcher, filter)) {
                    game.addEvent(new MagicCounterUnlessEvent(
                        event.getSource(),
                        item,
                        cost
                    ));
                }
            };
        }
    },
    CounterSpellToExile(
        "counter " + ARG.ITEMS + "\\. if that spell is countered this way, exile it instead of putting it into its owner's graveyard",
        MagicTargetHint.Negative,
        MagicTiming.Counter,
        "Counter"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicItemOnStack> filter = ARG.itemsParse(matcher);
            return (game, event) -> {
                for (final MagicItemOnStack item : ARG.items(event, matcher, filter)) {
                    game.doAction(new CounterItemOnStackAction(item, MagicLocationType.Exile));
                }
            };
        }
    },
    CounterSpell(
        "counter " + ARG.ITEMS,
        MagicTargetHint.Negative,
        MagicTiming.Counter,
        "Counter"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicItemOnStack> filter = ARG.itemsParse(matcher);
            return (game, event) -> {
                for (final MagicItemOnStack item : ARG.items(event, matcher, filter)) {
                    game.doAction(new CounterItemOnStackAction(item));
                }
            };
        }
    },
    FlickerYour(
        "exile " + ARG.PERMANENTS + ", then return (it|that card) to the battlefield under your control",
        MagicTargetHint.Positive,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Flicker"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
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
            };
        }
    },
    FlickerOwner(
        "exile " + ARG.PERMANENTS + ", then return (it|that card) to the battlefield" + ARG.MODS + " under its owner's control",
        MagicTargetHint.Positive,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Flicker"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final List<MagicPlayMod> mods = ARG.mods(matcher);
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new RemoveFromPlayAction(
                        it,
                        MagicLocationType.Exile
                    ));
                    game.doAction(new ReturnCardAction(
                        MagicLocationType.Exile,
                        it.getCard(),
                        it.getOwner(),
                        mods
                    ));
                }
            };
        }
    },
    FlickerEndStep(
        "exile " + ARG.PERMANENTS + "\\. (if you do, )?return (those cards|the exiled card|that card|it|sn) to the battlefield under (their|its) owner('s|s') control at the beginning of the next end step",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Flicker"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new ExileUntilEndOfTurnAction(it));
                }
            };
        }
    },
    ExilePermOrSpell(
        "exile sn",
        MagicTiming.Removal,
        "Exile",
        (game, event) -> {
            if (event.getSource().isPermanent()) {
                game.doAction(new RemoveFromPlayAction(event.getPermanent(), MagicLocationType.Exile));
            } else {
                game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.Exile));
            }
        }
    ),
    ExileCards(
        "exile " + ARG.CARDS,
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Exile"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicCard> filter = ARG.cardsParse(matcher);
            return (game, event) -> {
                for (final MagicCard it : ARG.cards(event, matcher, filter)) {
                    game.doAction(new ExileLinkAction(
                        event.getSource().isPermanent() ? event.getPermanent() : MagicPermanent.NONE,
                        it,
                        it.getLocation()
                    ));
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
    ExilePermanentsUntilLeaves(
        "exile " + ARG.PERMANENTS + " until SN leaves the battlefield",
        MagicTargetHint.Negative,
        MagicExileTargetPicker.create(),
        MagicTiming.Removal,
        "Exile"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent perm : ARG.permanents(event, matcher, filter)) {
                    if (event.getPermanent().isValid()) {
                        game.doAction(new ExileLinkAction(event.getPermanent(), perm));
                        game.doAction(new AddTriggerAction(event.getPermanent(), ThisLeavesBattlefieldTrigger.ExileUntilLeaves));
                    }
                }
            };
        }
    },
    ExilePermanents(
        "exile " + ARG.PERMANENTS,
        MagicTargetHint.Negative,
        MagicExileTargetPicker.create(),
        MagicTiming.Removal,
        "Exile"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent perm : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new ExileLinkAction(
                        event.getSource().isPermanent() ? event.getPermanent() : MagicPermanent.NONE,
                        perm
                    ));
                }
            };
        }
    },
    RemoveFromCombat(
        "remove " + ARG.PERMANENTS + " from combat",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Remove"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent perm : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new RemoveFromCombatAction(perm));
                }
            };
        }
    },
    DamageEqual(
        ARG.IT + " deal(s)? damage equal to " + ARG.WORDRUN + " to " + ARG.TARGETS,
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicTargetFilter<MagicTarget> filter = ARG.targetsParse(matcher);
            return (game, event) -> {
                final int amount = count.getAmount(event);
                game.logAppendValue(event.getPlayer(), amount);
                for (final MagicTarget target : ARG.targets(event, matcher, filter)) {
                    game.doAction(new DealDamageAction(
                        ARG.itSource(event, matcher),
                        target,
                        amount
                    ));
                }
            };
        }

        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            return new MagicDamageTargetPicker(count);
        }
    },
    DamageEqualAlt(
        ARG.IT + " deal(s)? damage to " + ARG.TARGETS + " equal to " + ARG.WORDRUN,
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return DamageEqual.getAction(matcher);
        }

        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return DamageEqual.getPicker(matcher);
        }
    },
    DamageEqualX(
        ARG.IT + " deal(s)? X damage to " + ARG.TARGETS + ", where X is " + ARG.WORDRUN,
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return DamageEqual.getAction(matcher);
        }

        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return DamageEqual.getPicker(matcher);
        }
    },
    DamageTwoGroupAlt(
        ARG.IT + " deal(s)? " + ARG.AMOUNT + " damage to " + ARG.TARGETS + " and " + ARG.AMOUNT2 + " (additional )?damage to " + ARG.TARGETS2,
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = ARG.amountObj(matcher);
            final int amount2 = ARG.amount2(matcher);
            final MagicTargetFilter<MagicTarget> filter1 = ARG.targetsParse(matcher);
            final MagicTargetFilter<MagicTarget> filter2 = ARG.targets2Parse(matcher);
            return (game, event) -> {
                final MagicSource source = ARG.itSource(event, matcher);
                final int amount1 = count.getAmount(event);
                for (final MagicTarget target : ARG.targets(event, matcher, filter1)) {
                    game.doAction(new DealDamageAction(source, target, amount1));
                }
                for (final MagicTarget target : ARG.targets2(event, matcher, filter2)) {
                    game.doAction(new DealDamageAction(source, target, amount2));
                }
            };
        }

        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return DamageGroup.getPicker(matcher);
        }
    },
    DamageTwoGroup(
        ARG.IT + " deal(s)? " + ARG.AMOUNT + " damage to " + ARG.TARGETS + " and " + ARG.TARGETS2,
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = ARG.amountObj(matcher);
            final MagicTargetFilter<MagicTarget> filter1 = ARG.targetsParse(matcher);
            final MagicTargetFilter<MagicTarget> filter2 = ARG.targets2Parse(matcher);
            return (game, event) -> {
                final int amount = count.getAmount(event);
                final MagicSource source = ARG.itSource(event, matcher);
                for (final MagicTarget target : ARG.targets(event, matcher, filter1)) {
                    game.doAction(new DealDamageAction(source, target, amount));
                }
                for (final MagicTarget target : ARG.targets2(event, matcher, filter2)) {
                    game.doAction(new DealDamageAction(source, target, amount));
                }
            };
        }

        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return DamageGroup.getPicker(matcher);
        }
    },
    DamageGroupExile(
        ARG.IT + " deal(s)? " + ARG.AMOUNT + " damage to " + ARG.TARGETS + "\\. " +
            "(?<noregen>That creature can't be regenerated this turn. )?" +
            "If (a|that|the) creature (?<dealt>dealt damage this way )?would die this turn, exile it instead.",
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = ARG.amountObj(matcher);
            final MagicTargetFilter<MagicTarget> filter = ARG.targetsParse(matcher);
            return (game, event) -> {
                final int amount = count.getAmount(event);
                for (final MagicTarget it : ARG.targets(event, matcher, filter)) {
                    final MagicDamage damage = new MagicDamage(ARG.itSource(event, matcher), it, amount);
                    game.doAction(new DealDamageAction(damage));
                    if (it.isPermanent()) {
                        final MagicPermanent perm = (MagicPermanent)it;
                        if (matcher.group("noregen") != null) {
                            game.doAction(ChangeStateAction.Set(
                                perm,
                                MagicPermanentState.CannotBeRegenerated
                            ));
                        }
                        if (matcher.group("dealt") == null || damage.getDealtAmount() > 0) {
                            game.doAction(new AddTurnTriggerAction(
                                perm,
                                ThisLeavesBattlefieldTrigger.IfDieExileInstead
                            ));
                        }
                    }
                }
            };
        }

        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            return DamageGroup.getPicker(matcher);
        }
    },
    DamageGroup(
        ARG.IT + " deal(s)? " + ARG.AMOUNT + " damage to " + ARG.TARGETS,
        MagicTiming.Removal,
        "Damage"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = ARG.amountObj(matcher);
            final MagicTargetFilter<MagicTarget> filter = ARG.targetsParse(matcher);
            return (game, event) -> {
                final int amount = count.getAmount(event);
                for (final MagicTarget target : ARG.targets(event, matcher, filter)) {
                    game.doAction(new DealDamageAction(ARG.itSource(event, matcher), target, amount));
                }
            };
        }

        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final MagicAmount count = ARG.amountObj(matcher);
            return new MagicDamageTargetPicker(count);
        }
    },
    Fight(
        ARG.IT + " fight(s)? " + ARG.TARGETS,
        MagicTiming.Removal,
        "Fight"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicTarget> filter = ARG.targetsParse(matcher);
            return (game, event) -> event.processTargetPermanent(game, (final MagicPermanent other) -> {
                final MagicPermanent it = ARG.itPermanent(event, matcher);
                game.doAction(new DealDamageAction(it, other, it.getPower()));
                game.doAction(new DealDamageAction(other, it, other.getPower()));
            });
        }

        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            if (matcher.group("sn") != null) {
                return new MagicDamageTargetPicker(MagicAmountFactory.SN_Power);
            } else {
                return MagicDefaultTargetPicker.create();
            }
        }
    },
    PreventNextDamage(
        "prevent the next " + ARG.AMOUNT + " damage that would be dealt to " + ARG.TARGETS + " this turn",
        MagicTargetHint.Positive,
        MagicPreventTargetPicker.create(),
        MagicTiming.Pump,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = ARG.amountObj(matcher);
            final MagicTargetFilter<MagicTarget> filter = ARG.targetsParse(matcher);
            return (game, event) -> {
                final int amount = count.getAmount(event);
                for (final MagicTarget it : ARG.targets(event, matcher, filter)) {
                    game.doAction(new PreventDamageAction(it, amount));
                }
            };
        }
    },
    PreventAllCombat(
        "prevent all combat damage that would be dealt this turn",
        MagicTiming.Block,
        "Prevent",
        (game, event) -> game.doAction(new AddTurnTriggerAction(
            PreventDamageTrigger.PreventCombatDamage
        ))
    ),
    PreventAllCombatToBy(
        "prevent all combat damage that would be dealt to and dealt by " + ARG.PERMANENTS + " this turn",
        MagicTiming.Block,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new AddTurnTriggerAction(
                        it,
                        PreventDamageTrigger.PreventCombatDamageDealtToDealtBy
                    ));
                }
            };
        }
    },
    PreventAllCombatTo(
        "prevent all combat damage that would be dealt to " + ARG.TARGETS + " this turn",
        MagicTargetHint.Positive,
        MagicPreventTargetPicker.create(),
        MagicTiming.Block,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicTarget> filter = ARG.targetsParse(matcher);
            return (game, event) -> {
                if (matcher.group("group1") != null) {
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
            };
        }
    },
    PreventAllCombatBy(
        "prevent all combat damage (that would be dealt (this turn )?by )?" + ARG.PERMANENTS + "( would deal)?( this turn)?",
        MagicTargetHint.Negative,
        new MagicNoCombatTargetPicker(true, true, false),
        MagicTiming.Block,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                if (matcher.group("group") != null) {
                    game.doAction(new AddTurnTriggerAction(
                        PreventDamageTrigger.PreventCombatDamageDealtBy(filter)
                    ));
                } else {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new AddTurnTriggerAction(it, PreventDamageTrigger.PreventCombatDamageDealtBy));
                    }
                }
            };
        }
    },
    PreventAllDamageTo(
        "prevent all damage that would be dealt to " + ARG.TARGETS + " this turn",
        MagicTargetHint.Positive,
        MagicPreventTargetPicker.create(),
        MagicTiming.Pump,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicTarget> filter = ARG.targetsParse(matcher);
            return (game, event) -> {
                if (matcher.group("group1") != null) {
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
            };
        }
    },
    PreventAllDamageBy(
        "prevent all damage (that would be dealt (this turn )?by )?" + ARG.PERMANENTS + "( would deal)?( this turn)?",
        MagicTargetHint.Negative,
        new MagicNoCombatTargetPicker(true, true, false),
        MagicTiming.Pump,
        "Prevent"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                if (matcher.group("group") != null) {
                    game.doAction(new AddTurnTriggerAction(
                        PreventDamageTrigger.PreventDamageDealtBy(filter)
                    ));
                } else {
                    for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                        game.doAction(new AddTurnTriggerAction(it, PreventDamageTrigger.PreventDamageDealtBy));
                    }
                }
            };
        }
    },
    DrawLosePlayers(
        ARG.PLAYERS + " draw(s)? " + ARG.AMOUNT + " card(s)? and (you )?lose(s)? " + ARG.AMOUNT2 + " life",
        MagicTiming.Draw,
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount1 = ARG.amount(matcher);
            final int amount2 = ARG.amount2(matcher);
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new DrawAction(it, amount1));
                    game.doAction(new ChangeLifeAction(it, -amount2));
                }
            };
        }
    },
    DrawSelfNextUpkeep(
        "(pn |you )?draw(s)? a card at the beginning of the next turn's upkeep",
        MagicTiming.Draw,
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return (game, event) -> game.doAction(new AddTriggerAction(
                AtUpkeepTrigger.YouDraw(
                    event.getSource(),
                    event.getPlayer()
                )
            ));
        }
    },
    Draw(
        ARG.PLAYERS + "( )?draw(s)?( " + ARG.AMOUNT + ")? (additional )?card(s)?( (for each|equal to) " + ARG.WORDRUN + ")?",
        MagicTargetHint.Positive,
        MagicTiming.Draw,
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount eachCount = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicAmount cardCount = ARG.amountObj(matcher);
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                final int multiplier = eachCount.getAmount(event);
                final int total = cardCount.getAmount(event) * multiplier;
                if (eachCount != MagicAmountFactory.One) {
                    game.logAppendValue(event.getPlayer(), total);
                }
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.addEvent(new MagicDrawEvent(event.getSource(), it, total, ""));
                }
            };
        }
    },
    DrawDiscard(
        ARG.PLAYERS + "( )?draw(s)? " + ARG.AMOUNT + " card(s)?(, then|\\. if you do,) discard(s)? " + ARG.AMOUNT2 + " card(s)?(?<random> at random)?",
        MagicTiming.Draw,
        "Draw"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount1 = ARG.amount(matcher);
            final int amount2 = ARG.amount2(matcher);
            final boolean isRandom = matcher.group("random") != null;
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new DrawAction(it, amount1));
                    if (isRandom) {
                        game.addEvent(MagicDiscardEvent.Random(event.getSource(), it, amount2));
                    } else {
                        game.addEvent(new MagicDiscardEvent(event.getSource(), it, amount2));
                    }
                }
            };
        }
    },
    Discard(
        ARG.PLAYERS + "( )?discard(s)? " + ARG.AMOUNT + " card(s)?(?<random> at random)?( for each " + ARG.WORDRUN + ")?",
        MagicTargetHint.Negative,
        MagicTiming.Draw,
        "Discard"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount eachCount = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicAmount cardCount = ARG.amountObj(matcher);
            final boolean isRandom = matcher.group("random") != null;
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                final int multiplier = eachCount.getAmount(event);
                final int total = cardCount.getAmount(event) * multiplier;
                if (eachCount != MagicAmountFactory.One) {
                    game.logAppendValue(event.getPlayer(), total);
                }
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    if (isRandom) {
                        game.addEvent(MagicDiscardEvent.Random(event.getSource(), it, total));
                    } else {
                        game.addEvent(new MagicDiscardEvent(event.getSource(), it, total));
                    }
                }
            };
        }
    },
    DiscardHand(
        ARG.PLAYERS + "( )?discard(s)? (your|his or her) hand",
        MagicTargetHint.Negative,
        MagicTiming.Draw,
        "Discard"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.addEvent(new MagicDiscardHandEvent(event.getSource(), it));
                }
            };
        }
    },
    DrainLife(
        ARG.PLAYERS + " lose(s)? " + ARG.AMOUNT + " life and you gain " + ARG.AMOUNT2 + " life",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "-Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount1 = ARG.amount(matcher);
            final int amount2 = ARG.amount2(matcher);
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
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
            };
        }
    },
    DrainLifeAlt(
        ARG.PLAYERS + "( )?lose(s)?( " + ARG.AMOUNT + ")? life( (for each|equal to) " + ARG.WORDRUN + ")?\\. You gain life equal to the life lost this way",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "-Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                final int multiplier = count.getAmount(event);
                final int total = amount * multiplier;
                if (count != MagicAmountFactory.One) {
                    game.logAppendValue(event.getPlayer(), total);
                }
                int totalLost = 0;
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    final ChangeLifeAction act = new ChangeLifeAction(it, -total);
                    game.doAction(act);
                    if (act.getLifeChange() < 0) {
                        totalLost += -act.getLifeChange();
                    }
                }
                game.doAction(new ChangeLifeAction(event.getPlayer(), totalLost));
            };
        }
    },
    GainLife(
        ARG.PLAYERS + "( )?gain(s)?( " + ARG.AMOUNT + ")? life( (for each|equal to) " + ARG.WORDRUN + ")?",
        MagicTargetHint.Positive,
        MagicTiming.Removal,
        "+Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount lifeCount = ARG.amountObj(matcher);
            final MagicAmount eachCount = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                final int multiplier = eachCount.getAmount(event);
                final int total = lifeCount.getAmount(event) * multiplier;
                if (eachCount != MagicAmountFactory.One) {
                    game.logAppendValue(event.getPlayer(), total);
                }
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new ChangeLifeAction(it, total));
                }
            };
        }
    },
    LoseLife(
        ARG.PLAYERS + "( )?lose(s)?( " + ARG.AMOUNT + ")? life( (for each|equal to) " + ARG.WORDRUN + ")?",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "-Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                final int multiplier = count.getAmount(event);
                final int total = amount * multiplier;
                if (count != MagicAmountFactory.One) {
                    game.logAppendValue(event.getPlayer(), total);
                }
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new ChangeLifeAction(it, -total));
                }
            };
        }
    },
    SetLife(
        ARG.PLAYERS + "('s|r) life total becomes " + ARG.AMOUNT,
        MagicTiming.Removal,
        "=Life"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new ChangeLifeAction(it, amount - it.getLife()));
                }
            };
        }
    },
    Pump(
        ARG.PERMANENTS + " get(s)? (an additional )?(?<pt>[X0-9+]+/[X0-9+]+) until end of turn( for each " + ARG.WORDRUN + ")?",
        MagicTargetHint.Positive,
        MagicPumpTargetPicker.create(),
        MagicTiming.Pump,
        "Pump"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final String[] pt = ARG.ptStr(matcher);
            final MagicAmount powerCounter = MagicAmountParser.build(pt[0]);
            final MagicAmount toughnessCounter = MagicAmountParser.build(pt[1]);
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                final int amount = count.getAmount(event);
                final int power = powerCounter.getAmount(event);
                final int toughness = toughnessCounter.getAmount(event);
                if (count != MagicAmountFactory.One) {
                    game.logAppendValue(event.getPlayer(), amount);
                }
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new ChangeTurnPTAction(it, power * amount, toughness * amount));
                }
            };
        }
    },
    PumpX(
        ARG.PERMANENTS + "( gain(s)? (?<ability>.+?) and)? get(s)? (an additional )?(?<pt>[X0-9+]+/[X0-9+]+) until end of turn, where X is " + ARG.WORDRUN,
        MagicTargetHint.Positive,
        MagicPumpTargetPicker.create(),
        MagicTiming.Pump,
        "Pump"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final String[] pt = ARG.ptStr(matcher);
            final MagicAmount countX = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            final MagicAbilityList abilityList = matcher.group("ability") != null ?
                MagicAbility.getAbilityList(matcher.group("ability")) : null;
            return (game, event) -> {
                final int X = countX.getAmount(event);
                final int power = MagicAmountParser.getX(pt[0], X);
                final int toughness = MagicAmountParser.getX(pt[1], X);
                game.logAppendX(event.getPlayer(), X);
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new ChangeTurnPTAction(it, power, toughness));
                    if (abilityList != null) {
                        game.doAction(new GainAbilityAction(it, abilityList));
                    }
                }
            };
        }
    },
    Weaken(
        ARG.PERMANENTS + " get(s)? (?<pt>[X0-9-]+/[X0-9-]+) until end of turn( for each " + ARG.WORDRUN + ")?",
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
            final String[] pt = ARG.ptStr(matcher);
            final MagicAmount toughnessCounter = MagicAmountParser.build(pt[1]);
            return new MagicWeakenTargetPicker(toughnessCounter);
        }
    },
    WeakenX(
        ARG.PERMANENTS + "( gain(s)? (?<ability>.+?) and)? get(s)? (an additional )?(?<pt>[X0-9-]+/[X0-9-]+) until end of turn, where X is " + ARG.WORDRUN,
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Weaken"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return PumpX.getAction(matcher);
        }

        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final String[] pt = ARG.ptStr(matcher);
            final MagicAmount countX = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicAmount toughnessCounter = pt[1].equalsIgnoreCase("-x") ? countX : MagicAmountParser.build(pt[1]);
            return new MagicWeakenTargetPicker(toughnessCounter);
        }
    },
    ModPT(
        ARG.PERMANENTS + " get(s)? (?<pt>[X0-9+-]+/[X0-9+-]+) until end of turn( for each " + ARG.WORDRUN + ")?",
        MagicTiming.Removal,
        "Pump"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return Pump.getAction(matcher);
        }
    },
    PumpGain(
        ARG.PERMANENTS + " get(s)? (?<pt>[X0-9+]+/[X0-9+]+) and (gain(s)?|is) (?<ability>.+) until end of turn",
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final String[] pt = ARG.ptStr(matcher);
            final MagicAmount powerCounter = MagicAmountParser.build(pt[0]);
            final MagicAmount toughnessCounter = MagicAmountParser.build(pt[1]);
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                final int power = powerCounter.getAmount(event);
                final int toughness = toughnessCounter.getAmount(event);
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new ChangeTurnPTAction(it, power, toughness));
                    game.doAction(new GainAbilityAction(it, abilityList));
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
        "until end of turn, " + ARG.PERMANENTS + " get(s)? (?<pt>[X0-9+]+/[X0-9+]+) and (gain(s)?|is) (?<ability>.+)",
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
        ARG.PERMANENTS + " get(s)? (?<pt>[X0-9+]+/[X0-9+]+) (until end of turn and|and) (?<ability>can('t)? .+) this turn",
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
        ARG.PERMANENTS + " get(s)? (?<pt>[X0-9+-]+/[X0-9+-]+) and gains (?<ability>.+) until end of turn",
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
        ARG.PERMANENTS + " get(s)? (?<pt>[X0-9+-]+/[X0-9+-]+) and (?<ability>can('t)? .+) this turn",
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
        "put " + ARG.AMOUNT + " (?<type>[^ ]+) counter(s)? on " + ARG.PERMANENTS
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = ARG.amountObj(matcher);
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                final int amount = count.getAmount(event);
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new ChangeCountersAction(
                        it,
                        counterType,
                        amount
                    ));
                }
            };
        }

        @Override
        public MagicTargetHint getHint(final Matcher matcher) {
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            if (counterType.getName().contains("-") || counterType.getScore() < 0) {
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
                return new MagicWeakenTargetPicker(-Integer.parseInt(pt[0]), -Integer.parseInt(pt[1]));
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
            return "+Counters";
        }
    },
    CounterFromSelfClockwork(
        "remove a \\+1\\/\\+1 counter from (sn|it) at end of combat",
        MagicTiming.Pump,
        "Remove",
        (game, event) -> game.doAction(new AddTurnTriggerAction(event.getPermanent(), AtEndOfCombatTrigger.Clockwork))
    ),
    RemoveCounter(
        "remove " + ARG.AMOUNT + " (?<type>[^ ]+) counter(s)? from " + ARG.PERMANENTS,
        MagicTiming.Pump
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = ARG.amountObj(matcher);
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(matcher.group("type"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                final int amount = count.getAmount(event);
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new ChangeCountersAction(
                        it,
                        counterType,
                        -amount
                    ));
                }
            };
        }

        @Override
        public String getName(final Matcher matcher) {
            return "-Counters";
        }
    },
    Bolster(
        "bolster " + ARG.AMOUNT,
        MagicTiming.Pump,
        "Bolster"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            return (game, event) -> {
                game.addEvent(new MagicBolsterEvent(event, amount));
            };
        }
    },
    RecoverCardSelf(
        "return sn from your graveyard to your hand",
        MagicTiming.Draw,
        "Return",
        (game, event) -> game.doAction(new ShiftCardAction(
            event.getSourceCard(),
            MagicLocationType.Graveyard,
            MagicLocationType.OwnersHand
        ))
    ),
    ReanimateCardSelf(
        "return sn from your graveyard to the battlefield" + ARG.MODS,
        MagicTiming.Token,
        "Reanimate"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final List<MagicPlayMod> mods = ARG.mods(matcher);
            return (game, event) -> game.doAction(new ReanimateAction(
                event.getSourceCard(),
                event.getPlayer(),
                mods
            ));
        }
    },
    RecoverSelf(
        "return sn from the graveyard to its owner's hand",
        MagicTiming.Draw,
        "Return",
        (game, event) -> game.doAction(new ShiftCardAction(
            event.getSourceCard(),
            MagicLocationType.Graveyard,
            MagicLocationType.OwnersHand
        ))
    ),
    ReturnPermOrSpell(
        "return sn to its owner's hand",
        MagicTiming.Draw,
        "Return",
        (game, event) -> {
            if (event.getSource().isPermanent()) {
                game.doAction(new RemoveFromPlayAction(event.getPermanent(), MagicLocationType.OwnersHand));
            } else {
                game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
            }
        }
    ),
    RecoverRandomCards(
        "return " + ARG.AMOUNT + " " + ARG.WORDRUN + " at random from your graveyard to your hand",
        MagicTargetHint.Positive,
        MagicGraveyardTargetPicker.ReturnToHand,
        MagicTiming.Draw,
        "Return"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            final MagicTargetFilter<MagicCard> filter = MagicTargetFilterFactory.Card(ARG.wordrun(matcher) + " from your graveyard");
            return (game, event) -> {
                final MagicCardList cards = new MagicCardList(filter.filter(event));
                for (final MagicCard card : cards.getRandomCards(amount)) {
                    game.doAction(new ShiftCardAction(
                        card,
                        MagicLocationType.Graveyard,
                        MagicLocationType.OwnersHand
                    ));
                }
            };
        }
    },
    RecoverCards(
        "return " + ARG.CARDS + " to (your hand|its owner's hand|their owner's hand|their owners' hands)",
        MagicTargetHint.Positive,
        MagicGraveyardTargetPicker.ReturnToHand,
        MagicTiming.Draw,
        "Return"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicCard> filter = ARG.cardsParse(matcher);
            return (game, event) -> {
                for (final MagicCard it : ARG.cards(event, matcher, filter)) {
                    final MagicLocationType from = it.getLocation();
                    game.doAction(new ShiftCardAction(it, from, MagicLocationType.OwnersHand));
                }
            };
        }
    },
    ReclaimCards(
        "put " + ARG.CARDS + " on top of (your|its owner's) library",
        MagicTargetHint.Positive,
        MagicGraveyardTargetPicker.ReturnToHand,
        MagicTiming.Draw,
        "Reclaim"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicCard> filter = ARG.cardsParse(matcher);
            return (game, event) -> {
                for (final MagicCard it : ARG.cards(event, matcher, filter)) {
                    final MagicLocationType from = it.getLocation();
                    game.doAction(new ShiftCardAction(it, from, MagicLocationType.TopOfOwnersLibrary));
                }
            };
        }
    },
    TuckCards(
        "put " + ARG.CARDS + " on the bottom of (your|its owner's) library",
        MagicTargetHint.Negative,
        MagicGraveyardTargetPicker.ExileOpp,
        MagicTiming.Draw,
        "Tuck"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicCard> filter = ARG.cardsParse(matcher);
            return (game, event) -> {
                for (final MagicCard it : ARG.cards(event, matcher, filter)) {
                    final MagicLocationType from = it.getLocation();
                    game.doAction(new ShiftCardAction(it, from, MagicLocationType.BottomOfOwnersLibrary));
                }
            };
        }
    },
    Bounce(
        "return " + ARG.PERMANENTS + " to (your hand|its owner's hand|their owner's hand|their owners' hands)",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new RemoveFromPlayAction(it, MagicLocationType.OwnersHand));
                }
            };
        }
    },
    BounceEndOfCombat(
        "return " + ARG.PERMANENTS + " to (your hand|its owner's hand|their owner's hand|their owners' hands) at end of combat",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new AddTriggerAction(
                        it,
                        AtEndOfCombatTrigger.Return
                    ));
                }
            };
        }
    },
    BounceEndOfTurn(
        "return " + ARG.PERMANENTS + " to (your hand|its owner's hand|their owner's hand|their owners' hands) at the beginning of the next end step",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new AddTriggerAction(
                        it,
                        AtEndOfTurnTrigger.Return
                    ));
                }
            };
        }
    },
    BounceLibTop(
        "put " + ARG.PERMANENTS + " on top of (its owner's library|his or her library|their owners' libraries)",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new RemoveFromPlayAction(it, MagicLocationType.TopOfOwnersLibrary));
                }
            };
        }
    },
    BounceLibBottom(
        "put " + ARG.PERMANENTS + " on the bottom of (your library|its owner's library|his or her library|their owners' libraries)",
        MagicTargetHint.None,
        MagicBounceTargetPicker.create(),
        MagicTiming.Removal,
        "Bounce"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new RemoveFromPlayAction(it, MagicLocationType.BottomOfOwnersLibrary));
                }
            };
        }
    },
    RevealToHand(
        "reveal the top " + ARG.AMOUNT + " cards of your library\\. Put all " + ARG.WORDRUN + " revealed this way into your hand and the rest ((?<graveyard>into your graveyard)|(?<bottom>on the bottom of your library in any order))",
        MagicTiming.Draw,
        "Reveal"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int n = ARG.amount(matcher);
            final MagicTargetFilter<MagicCard> filter = MagicTargetFilterFactory.Card(ARG.wordrun(matcher) + " from your library");
            final MagicLocationType restLocation = matcher.group("graveyard") != null ? MagicLocationType.Graveyard : MagicLocationType.BottomOfOwnersLibrary;
            return (game, event) -> {
                final List<MagicCard> topN = event.getPlayer().getLibrary().getCardsFromTop(n);
                game.doAction(new RevealAction(topN));
                for (final MagicCard it : topN) {
                    game.doAction(new ShiftCardAction(
                        it,
                        MagicLocationType.OwnersLibrary,
                        filter.accept(event.getSource(), event.getPlayer(), it) ?
                            MagicLocationType.OwnersHand :
                            restLocation
                    ));
                }
            };
        }
    },
    RevealToHand2(
        "reveal the top " + ARG.AMOUNT + " cards of your library\\. You may put a(n)? " + ARG.WORDRUN + " from among them into your hand. Put the rest into your graveyard",
        MagicTiming.Draw,
        "Reveal"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            final MagicTargetFilter<MagicCard> filter = MagicTargetFilterFactory.Card(ARG.wordrun(matcher) + " from your library");
            return (game, event) -> game.addEvent(MagicTutorTopEvent.reveal(event, amount, filter));
        }
    },
    SearchLibraryToHand(
        "search your library for (?<card>[^\\.]*?)(?<reveal>, reveal (it|that card))?(, and |, | and )put (it|that card) into your hand(.|,) (If you do, |then )shuffle your library",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("card") + " from your library");
            return (game, event) -> game.addEvent(new MagicSearchToLocationEvent(
                event,
                choice,
                MagicLocationType.OwnersHand,
                matcher.group("reveal") != null
            ));
        }
    },
    SearchMultiLibraryToHand(
        "search your library for up to " + ARG.AMOUNT + " (?<card>[^\\.]*), reveal (them|those cards), (and )?put them into your hand(.|,) (If you do, |(t|T)hen )shuffle your library",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicCard> filter = MagicTargetFilterFactory.Card(matcher.group("card") + " from your library");
            final int amount = ARG.amount(matcher);
            return (game, event) -> game.addEvent(new MagicSearchToLocationEvent(
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
    },
    SearchLibraryToTopLibrary(
        "search your library for (?<card>[^\\.]*?)(,| and) (?<reveal>reveal (it(,|\\.)|that card\\.))?( then)? (S|s)huffle your library(, then| and) put (that|the) card on top of it",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("card") + " from your library");
            return (game, event) -> game.addEvent(new MagicSearchToLocationEvent(
                event,
                choice,
                MagicLocationType.TopOfOwnersLibrary,
                matcher.group("reveal") != null
            ));
        }
    },
    SearchLibraryToGraveyard(
        "search your library for (?<card>[^\\.]*) and put (that card|it) into your graveyard\\. (If you do,|Then) shuffle your library",
        MagicTiming.Draw,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("card") + " from your library");
            return (game, event) -> game.addEvent(new MagicSearchToLocationEvent(
                event,
                choice,
                MagicLocationType.Graveyard
            ));
        }
    },
    SearchLibraryToBattlefield(
        "search your library for (?<card>[^\\.]*)(,| and) put (it|that card) onto the battlefield" + ARG.MODS + "(.|,) ((T|t)hen|If you do,) shuffle your library",
        MagicTiming.Token,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("card") + " from your library");
            final List<MagicPlayMod> mods = ARG.mods(matcher);
            return (game, event) -> game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                choice,
                mods
            ));
        }
    },
    SearchLibraryToBattlefieldAlt(
        "search your library for (?<card>[^\\.]*)(,| and) put (it|that card) onto the battlefield\\. Then shuffle your library\\." + ARG.MODS,
        MagicTiming.Token,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return SearchLibraryToBattlefield.getAction(matcher);
        }
    },
    SearchMultiLibraryToBattlefield(
        "search your library for up to " + ARG.AMOUNT + " (?<card>[^\\.]*)(,| and) put (them|those cards) onto the battlefield" + ARG.MODS + "(.|,) ((T|t)hen|If you do,) shuffle your library",
        MagicTiming.Token,
        "Search"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            final MagicTargetFilter<MagicCard> filter = MagicTargetFilterFactory.Card(matcher.group("card") + " from your library");
            final List<MagicPlayMod> mods = ARG.mods(matcher);
            return (game, event) -> game.addEvent(new MagicSearchOntoBattlefieldEvent(
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
    },
    FromHandToBattlefield(
        "put (?<card>[^\\.]*hand) onto the battlefield" + ARG.MODS,
        MagicTiming.Token,
        "Put"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetChoice choice = new MagicTargetChoice(getHint(matcher), matcher.group("card"));
            final List<MagicPlayMod> mods = ARG.mods(matcher);
            return (game, event) -> game.addEvent(new MagicPutOntoBattlefieldEvent(
                event,
                choice,
                mods
            ));
        }
    },
    Reanimate(
        "return " + ARG.GRAVEYARD + " to the battlefield" + ARG.MODS,
        MagicTargetHint.None,
        MagicGraveyardTargetPicker.PutOntoBattlefield,
        MagicTiming.Token,
        "Reanimate"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final List<MagicPlayMod> mods = ARG.mods(matcher);
            return (game, event) -> event.processTargetCard(game, (final MagicCard card) ->
                game.doAction(new ReanimateAction(
                    card,
                    event.getPlayer(),
                    mods
                ))
            );
        }
    },
    Reanimate2(
        "put " + ARG.GRAVEYARD + " onto the battlefield under your control" + ARG.MODS,
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
        "tap or untap " + ARG.CHOICE,
        MagicTargetHint.None,
        MagicTapTargetPicker.TapOrUntap,
        MagicTiming.Tapping,
        "Tap/Untap",
        (game, event) -> event.processTargetPermanent(game, perm -> game.addEvent(new MagicTapOrUntapEvent(event.getSource(), perm)))
    ),
    TapParalyze(
        "tap " + ARG.PERMANENTS + "( and it|\\. RN|\\. it|\\. Those creatures|\\. That creature|\\. That permanent) (doesn't|don't) untap during (its|their) controller('s|s') next untap step(s)?",
        MagicTargetHint.Negative,
        MagicTapTargetPicker.Tap,
        MagicTiming.Tapping,
        "Tap"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new TapAction(it));
                    game.doAction(ChangeStateAction.Set(
                        it,
                        MagicPermanentState.DoesNotUntapDuringNext
                    ));
                }
            };
        }
    },
    Tap(
        "tap " + ARG.PERMANENTS,
        MagicTargetHint.Negative,
        MagicTapTargetPicker.Tap,
        MagicTiming.Tapping,
        "Tap"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new TapAction(it));
                }
            };
        }
    },
    Paralyze(
        ARG.PERMANENTS + " doesn't untap during (your|its controller's) next untap step",
        MagicTargetHint.Negative,
        new MagicNoCombatTargetPicker(true, true, false),
        MagicTiming.Tapping,
        "Paralyze"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(ChangeStateAction.Set(
                        it,
                        MagicPermanentState.DoesNotUntapDuringNext
                    ));
                }
            };
        }
    },
    Untap(
        "untap " + ARG.PERMANENTS,
        MagicTargetHint.Positive,
        MagicTapTargetPicker.Untap,
        MagicTiming.Tapping,
        "Untap"
    ) {
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new UntapAction(it));
                }
            };
        }

        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return matcher.group("sn") != null ?
                new MagicCondition[]{new MagicArtificialCondition(MagicCondition.TAPPED_CONDITION)} :
                MagicActivation.NO_COND;
        }
    },
    PutTokenCopy(
        "(you )?put a token onto the battlefield that's a copy of " + ARG.PERMANENTS + "\\." + ARG.MODS,
        MagicTiming.Token,
        "Token"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            final List<MagicPlayMod> mods = ARG.mods(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new PlayTokenAction(
                        event.getPlayer(),
                        it,
                        mods
                    ));
                }
            };
        }
    },
    PutTokenCopyAlt(
        "(you )?put a token that's a copy of " + ARG.PERMANENTS + " onto the battlefield" + ARG.MODS,
        MagicTiming.Token,
        "Token"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return PutTokenCopy.getAction(matcher);
        }
    },
    PutTokens(
        ARG.PLAYERS + "( )?put(s)? " + ARG.AMOUNT + " (?<name>[^\\.]*token[^\\.]*) onto the battlefield" + ARG.MODS + "( )?((for each|where X is) " + ARG.WORDRUN + ")?",
        MagicTiming.Token,
        "Token"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount eachCount = MagicAmountParser.build(ARG.wordrun(matcher));
            final MagicAmount tokenCount = ARG.amountObj(matcher);
            final String tokenName = matcher.group("name").replace("tokens", "token");
            final MagicCardDefinition tokenDef = CardDefinitions.getToken(tokenName);
            final List<MagicPlayMod> mods = ARG.mods(matcher);
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                final int multiplier = eachCount.getAmount(event);
                int total = (eachCount != MagicAmountFactory.One && tokenCount == MagicAmountFactory.XCost) ?
                    multiplier : tokenCount.getAmount(event) * multiplier;
                if (eachCount != MagicAmountFactory.One) {
                    game.logAppendValue(event.getPlayer(), total);
                }
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    for (int i = 0; i < total; i++) {
                        game.doAction(new PlayTokenAction(
                            it,
                            tokenDef,
                            mods
                        ));
                    }
                }
            };
        }
    },
    CreateTokens(
        ARG.PLAYERS + "( )?create(s)? " + ARG.AMOUNT + " (?<name>[^\\.]*token(s)?[^\\.]*?(?= that's| for each| where|\\.))( that's)?" + ARG.MODS + "( )?((for each|where X is) " + ARG.WORDRUN + ")?",
        MagicTiming.Token,
        "Token"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return PutTokens.getAction(matcher);
        }
    },
    Mill(
        ARG.PLAYERS + "( )?put(s)? the top " + ARG.AMOUNT + "?( )?card(s)? of (your|his or her) library into (your|his or her) graveyard",
        MagicTiming.Draw,
        "Mill"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAmount count = ARG.amountObj(matcher);
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                final int amount = count.getAmount(event);
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new MillLibraryAction(it, amount));
                }
            };
        }
    },
    CantCastSpells(
        ARG.PLAYERS + " can't cast spells this turn",
        MagicTargetHint.Negative,
        MagicTiming.FirstMain,
        "Silence"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new ChangePlayerStateAction(
                        it,
                        MagicPlayerState.CantCastSpells
                    ));
                }
            };
        }
    },
    Manifest(
        "manifest the top " + ARG.AMOUNT + "?( )?card(s)? of your library",
        MagicTiming.Token,
        "Manifest"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            return (game, event) -> game.doAction(new ManifestAction(event.getPlayer(), amount));
        }
    },
    SacrificeSelf(
        "(its controller )?sacrifice(s)? sn",
        MagicTiming.Removal,
        "Sacrifice",
        (game, event) -> game.doAction(new SacrificeAction(event.getPermanent()))
    ),
    SacrificeEndStep(
        "(its controller )?sacrifice(s)? " + ARG.PERMANENTS + " at the beginning of the next end step",
        MagicTiming.Removal,
        "Sacrifice"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new AddTriggerAction(it, AtEndOfTurnTrigger.Sacrifice));
                }
            };
        }
    },
    SacrificeEndCombat(
        "(its controller )?sacrifice(s)? " + ARG.PERMANENTS + " at end of combat",
        MagicTiming.Removal,
        "Sacrifice"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new AddTriggerAction(it, AtEndOfCombatTrigger.Sacrifice));
                }
            };
        }
    },
    SacrificeChosen(
        ARG.PLAYERS + "( )?sacrifice(s)? (?<another>another )?(" + ARG.AMOUNT + " )?" + ARG.WORDRUN,
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Sacrifice"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPlayer> pfilter = ARG.playersParse(matcher);
            final int amt = ARG.amount(matcher);
            final String chosen = MagicTargetFilterFactory.toSingular(ARG.wordrun(matcher)) + " you control";
            final MagicTargetFilter<MagicPermanent> regular = MagicTargetFilterFactory.Permanent(chosen);
            final MagicTargetFilter<MagicPermanent> filter = matcher.group("another") != null ?
                new MagicOtherPermanentTargetFilter(regular) : regular;
            final MagicTargetChoice choice = new MagicTargetChoice(
                filter,
                ("aeiou".indexOf(chosen.charAt(0)) >= 0 ? "an " : "a ") + chosen
            );
            return (game, event) -> {
                for (final MagicPlayer it : ARG.players(event, matcher, pfilter)) {
                    for (int i = 0; i < amt; i++) {
                        game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), it, choice));
                    }
                }
            };
        }
    },
    Scry1(
        "(pn )?scry 1",
        MagicTiming.Draw,
        "Scry"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return (game, event) -> game.addEvent(new MagicScryEvent(event));
        }
    },
    PseudoScry(
        "Look at the top card of your library\\. You may put that card on the bottom of your library",
        MagicTiming.Draw,
        "Scry"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return (game, event) -> game.addEvent(MagicScryEvent.Pseudo(event));
        }
    },
    TutorTopReveal(
        "Look at the top " + ARG.AMOUNT + " cards of your library. You may reveal a(n)? " + ARG.WORDRUN + " from among them and put it into your hand\\. " +
        "(Then )?Put the rest on the bottom of your library in any order",
        MagicTiming.Draw,
        "Look"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            final MagicTargetFilter<MagicCard> filter = MagicTargetFilterFactory.Card(ARG.wordrun(matcher) + " from your library");
            return (game, event) -> game.addEvent(MagicTutorTopEvent.look(event, amount, filter));
        }
    },
    TutorTopBottom(
        "Look at the top " + ARG.AMOUNT + " cards of your library. Put " + ARG.AMOUNT2 + " of (them|those cards) into your hand and the (other|rest) on the bottom of your library( in any order)?",
        MagicTiming.Draw,
        "Look"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int n = ARG.amount(matcher);
            final int h = ARG.amount2(matcher);
            return (game, event) -> game.addEvent(MagicTutorTopEvent.toBottom(event, n, h));
        }
    },
    TutorTopGraveyard(
        "Look at the top " + ARG.AMOUNT + " cards of your library. Put " + ARG.AMOUNT2 + " of them into your hand and the (other|rest) into your graveyard",
        MagicTiming.Draw,
        "Look"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int n = ARG.amount(matcher);
            final int h = ARG.amount2(matcher);
            return (game, event) -> game.addEvent(MagicTutorTopEvent.toGraveyard(event, n, h));
        }
    },
    LoseGame(
        ARG.PLAYERS + " lose(s)? the game",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Lose"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new LoseGameAction(it, LoseGameAction.EFFECT_REASON));
                }
            };
        }
    },
    PayLose(
        "At the beginning of your next upkeep, pay " + ARG.MANACOST + "\\. If you don't, you lose the game",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Lose"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return (game, event) -> {
                game.doAction(new AddTriggerAction(
                    AtYourUpkeepTrigger.PayOrLose(
                        event.getSource(),
                        event.getPlayer(),
                        ARG.manacost(matcher)
                    )
                ));
            };
        }
    },
    WinGame(
        ARG.PLAYERS + " win(s)? the game",
        MagicTargetHint.Positive,
        MagicTiming.Removal,
        "Win"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new LoseGameAction(it.getOpponent(), LoseGameAction.EFFECT_REASON));
                }
            };
        }
    },
    /*
    Scry(
        "(pn )?scry (?<amount>[0-9]+)",
        MagicTiming.Draw,
        "Scry"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = Integer.parseInt(matcher.group("amount"));
            return (final MagicGame game, final MagicEvent event) -> {
                game.addEvent(new MagicScryXEvent(event.getSource(),event.getPlayer(),amount));
            };
        }
    },
    LookHand(
        "look at " + ARG.CHOICE + "'s hand",
        MagicTargetHint.Negative,
        MagicTiming.Flash,
        "Look"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return (final MagicGame game, final MagicEvent event) -> {
                event.processTargetPlayer(game, (final MagicPlayer player) -> {
                    game.doAction(new MagicRevealAction(player.getHand()));
                });
            };
        }
    },
    */
    Regenerate(
        "regenerate " + ARG.PERMANENTS,
        MagicTargetHint.Positive,
        MagicRegenerateTargetPicker.create(),
        MagicTiming.Pump,
        "Regen"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new RegenerateAction(it));
                }
            };
        }

        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return matcher.group("sn") != null ?
                new MagicCondition[]{MagicCondition.CAN_REGENERATE_CONDITION} :
                MagicActivation.NO_COND;
        }
    },
    SwitchPT(
        "switch " + ARG.PERMANENTS + "'s power and toughness until end of turn",
        MagicTargetHint.None,
        MagicDefaultPermanentTargetPicker.create(),
        MagicTiming.Pump,
        "Switch"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new AddStaticAction(it, MagicStatic.SwitchPT));
                }
            };
        }
    },
    ShuffleSelf(
        "(shuffle sn|sn's owner shuffles it) into (its owner's|his or her) library",
        MagicTiming.Removal,
        "Shuffle",
        (game, event) -> {
            final MagicSource source = event.getSource();
            if (source.isPermanent()) {
                game.doAction(new RemoveFromPlayAction(event.getPermanent(), MagicLocationType.OwnersLibrary));
            } else {
                game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersLibrary));
            }
        }
    ),
    ShuffleYourLibrary(
        "shuffle your library",
        MagicTiming.None,
        "Shuffle",
        (game, event) -> game.doAction(new ShuffleLibraryAction(event.getPlayer()))
    ),
    AttachSelf(
        "attach sn to " + ARG.PERMANENTS,
        MagicTargetHint.Positive,
        MagicPumpTargetPicker.create(),
        MagicTiming.Pump,
        "Attach"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new AttachAction(event.getPermanent(), it));
                }
            };
        }
    },
    TurnFaceDown(
        "turn " + ARG.PERMANENTS + " face down",
        MagicTiming.Tapping,
        "Face Down"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new TurnFaceDownAction(it));
                }
            };
        }
    },
    TurnFaceUp(
        "turn " + ARG.PERMANENTS + " face up",
        MagicTiming.Tapping,
        "Face Up"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new TurnFaceUpAction(it));
                }
            };
        }
    },
    FlipSelf(
        "flip sn",
        MagicTiming.Pump,
        "Flip",
        (game, event) -> game.doAction(new FlipAction(event.getPermanent()))
    ),
    Transform(
        "transform " + ARG.PERMANENTS,
        MagicTiming.Pump,
        "Transform"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new TransformAction(it));
                }
            };
        }
    },
    Populate(
        "populate",
        MagicTiming.Token,
        "Populate",
        (game, event) -> game.addEvent(new MagicPopulateEvent(event.getSource()))
    ),
    Cipher(
        "cipher",
        MagicTiming.Main,
        "Cipher",
        (game, event) -> game.doAction(new CipherAction(event.getCardOnStack(), event.getPlayer()))
    ),
    DetainChosen(
        "detain " + ARG.CHOICE,
        MagicTargetHint.Negative,
        new MagicNoCombatTargetPicker(true, true, false),
        MagicTiming.FirstMain,
        "Detain",
        (game, event) -> event.processTargetPermanent(game, (final MagicPermanent creature) ->
            game.doAction(new DetainAction(event.getPlayer(), creature)))
    ),
    CopySpell(
        "copy " + ARG.CHOICE + "\\. You may choose new targets for (the|that) copy",
        MagicTiming.Spell,
        "Copy",
        (game, event) -> event.processTargetCardOnStack(game, (final MagicCardOnStack item) ->
            game.doAction(new CopyCardOnStackAction(event.getPlayer(), item)))
    ),
    Monstrosity(
        "monstrosity " + ARG.AMOUNT,
        MagicTiming.Pump,
        "Monstrous"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            return (game, event) -> {
                final MagicPermanent SN = event.getPermanent();
                if (MagicCondition.NOT_MONSTROUS_CONDITION.accept(SN)) {
                    game.doAction(new ChangeCountersAction(SN, MagicCounterType.PlusOne, amount));
                    game.doAction(ChangeStateAction.Set(SN, MagicPermanentState.Monstrous));
                }
            };
        }

        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return new MagicCondition[]{
                MagicCondition.NOT_MONSTROUS_CONDITION,
            };
        }
    },
    Rebound(
        "rebound"
    ) {
        private final MagicEventAction EVENT_ACTION = (game, event) -> {
            final MagicCardOnStack spell = event.getCardOnStack();
            if (spell.getFromLocation() == MagicLocationType.OwnersHand) {
                game.doAction(new ChangeCardDestinationAction(spell, MagicLocationType.Exile));
                game.doAction(new AddTriggerAction(new ReboundTrigger(spell.getCard())));
            }
        };

        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return EVENT_ACTION;
        }
    },
    Investigate(
        "investigate",
        MagicTiming.Token,
        "Investigate"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return (game, event) -> game.doAction(new PlayTokenAction(event.getPlayer(), CardDefinitions.getToken("colorless Clue artifact token")));
        }
    },
    BecomesMonarch(
        "you become the monarch"
    ){
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return (game, event) -> game.doAction(new BecomeMonarchAction(event.getPlayer()));
        }
    },
    BecomeBlocked(
        ARG.PERMANENTS + " become(s)? blocked",
        MagicTiming.Block,
        "Block"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(ChangeStateAction.Set(it, MagicPermanentState.Blocked));
                }
            };
        }
    },
    BecomesAlt(
        "(?<duration>until end of turn, )" + ARG.PERMANENTS + " becomes( a| an)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( (with|and gains) (?<ability>.*?))?(?<additionTo>((\\.)? It's| that's) still [^\\.]*)?",
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
        ARG.PERMANENTS + " become(s)?( a| an)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( (with|and gains) (?<ability>.*?))?(?<additionTo> in addition to its other [a-z]*)(?<duration> until end of turn)?",
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
        ARG.PERMANENTS + " become(s)?( a| an)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( (with|and gains) (?<ability>.*?))?(?<duration> until end of turn)?(?<additionTo>(\\. It's| that's) still [^\\.]*)?",
        MagicTiming.Animate,
        "Becomes"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final PermanentSpecParser spec = new PermanentSpecParser(matcher);
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
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
            };
        }

        @Override
        public MagicCondition[] getConditions(final Matcher matcher) {
            return matcher.group("sn") != null ?
                new MagicCondition[]{MagicCondition.NOT_EXCLUDE_COMBAT_CONDITION} :
                MagicActivation.NO_COND;
        }
    },
    GainProtection(
        ARG.PERMANENTS + " gain(s)? protection from the color of your choice until end of turn",
        MagicTargetHint.Positive,
        MagicTiming.Pump,
        "Protection"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.addEvent(new MagicGainProtectionFromEvent(
                        event.getSource(),
                        event.getPlayer(),
                        it
                    ));
                }
            };
        }
    },
    GainAbilityAlt(
        "(?<ueot>until end of turn), " + ARG.PERMANENTS + " gain(s)? (?<ability>.+)",
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
    GainAbility(
        ARG.PERMANENTS + " gain(s)? (?<ability>.+?)(?<ueot> until end of turn)?",
        MagicTargetHint.Positive
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            final boolean duration = matcher.group("ueot") != null ? MagicStatic.UntilEOT : MagicStatic.Forever;
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new GainAbilityAction(it, abilityList, duration));
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
    GainAbilityCantBlockSN(
        ARG.PERMANENTS + " can't block SN this turn",
        MagicTargetHint.Negative,
        MagicTiming.Attack,
        "Can't Block"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                final int pIdx = event.getPlayer().getIndex();
                final CantBlockTrigger trigger = CantBlockTrigger.create(event.getPermanent().getId());
                game.doAction(new AddStaticAction(new MagicStatic(MagicLayer.Game, MagicStatic.UntilEOT) {
                    @Override
                    public void modGame(final MagicPermanent source, final MagicGame game) {
                        for (final MagicPermanent it : filter.filter(game.getPlayer(pIdx))) {
                            it.addAbility(trigger);
                        }
                    }
                }));
            };
        }
    },
    GainAbilityCan(
        ARG.PERMANENTS + " (?<ability>(can('t)?|attack(s)?) .+?)( this turn)?"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
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
            };
        }

        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            final MagicAbility ability = MagicAbility.getAbilityList(matcher.group("ability")).getFirst();
            switch (ability) {
                case CannotAttack:
                case AttacksEachTurnIfAble:
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
                    return new MagicNoCombatTargetPicker(true, false, false);
                case CannotBlock:
                    return new MagicNoCombatTargetPicker(false, true, false);
                case CannotAttackOrBlock:
                    return new MagicNoCombatTargetPicker(true, true, false);
                case AttacksEachTurnIfAble:
                    return MagicMustAttackTargetPicker.create();
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
        ARG.PERMANENTS + " lose(s)? (?<ability>.+?)(?<ueot> until end of turn)?",
        MagicTargetHint.Negative
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicAbilityList abilityList = MagicAbility.getAbilityList(matcher.group("ability"));
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            final boolean duration = matcher.group("ueot") != null ? MagicStatic.UntilEOT : MagicStatic.Forever;
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new LoseAbilityAction(it, abilityList, duration));
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
    NoCombatDamage(
        ARG.IT + " assigns no combat damage this turn"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            return (game, event) -> {
                game.doAction(ChangeStateAction.Set(
                    ARG.itPermanent(event, matcher),
                    MagicPermanentState.NoCombatDamage
                ));
            };
        }
    },
    GainControl(
        "gain control of " + ARG.PERMANENTS + "(?<ueot> until end of turn)?",
        MagicTargetHint.Negative,
        MagicExileTargetPicker.create(),
        MagicTiming.Removal,
        "Control"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final boolean duration = matcher.group("ueot") != null ? MagicStatic.UntilEOT : MagicStatic.Forever;
            final MagicTargetFilter<MagicPermanent> filter = ARG.permanentsParse(matcher);
            return (game, event) -> {
                for (final MagicPermanent it : ARG.permanents(event, matcher, filter)) {
                    game.doAction(new GainControlAction(event.getPlayer(), it, duration));
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
            return (game, event) -> MagicClashEvent.EventAction(act).executeEvent(game, event);
        }
    },
    FlipCoin(
        "Flip a coin\\.( If you win the flip, (?<win>.*?))?( If you lose the flip, (?<lose>.*))?"
    ) {
        @Override
        public MagicChoice getChoice(final Matcher matcher) {
            final String[] alts = {"win", "lose"};
            for (final String alt : alts) {
                final String effect = matcher.group(alt);
                if (effect != null) {
                    final MagicSourceEvent e = MagicRuleEventAction.create(effect);
                    if (e.getChoice().isValid()) {
                        throw new RuntimeException("flip effect should not have choice: \"" + effect + "\"");
                    }
                }
            }
            return MagicChoice.NONE;
        }

        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final MagicEventAction winAction = matcher.group("win") != null ?
                MagicRuleEventAction.create(matcher.group("win")).getAction() :
                MagicEventAction.NONE;

            final MagicEventAction loseAction = matcher.group("lose") != null ?
                MagicRuleEventAction.create(matcher.group("lose")).getAction() :
                MagicEventAction.NONE;

            return (game, event) -> game.addEvent(new MagicCoinFlipEvent(event, winAction, loseAction));
        }

        @Override
        public MagicTiming getTiming(final Matcher matcher) {
            final MagicSourceEvent e = matcher.group("win") != null ?
                MagicRuleEventAction.create(matcher.group("win")) :
                MagicRuleEventAction.create(matcher.group("lose"));
            return e.getTiming();
        }

        @Override
        public MagicTargetPicker<?> getPicker(final Matcher matcher) {
            final MagicSourceEvent e = matcher.group("win") != null ?
                MagicRuleEventAction.create(matcher.group("win")) :
                MagicRuleEventAction.create(matcher.group("lose"));
            return e.getPicker();
        }

        @Override
        public String getName(final Matcher matcher) {
            final MagicSourceEvent e = matcher.group("win") != null ?
                MagicRuleEventAction.create(matcher.group("win")) :
                MagicRuleEventAction.create(matcher.group("lose"));
            return e.getName();
        }
    },
    Poison(
        ARG.PLAYERS + " get(s)? " + ARG.AMOUNT + " poison counter(s)?",
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Poison"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new ChangePoisonAction(it, amount));
                }
            };
        }
    },
    Energy(
        ARG.PLAYERS + " get(s)? " + ARG.ENERGY,
        MagicTargetHint.Negative,
        MagicTiming.Removal,
        "Poison"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.energy(matcher);
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new ChangeCountersAction(it, MagicCounterType.Energy, amount));
                }
            };
        }
    },
    ExtraTurn(
        ARG.PLAYERS + "( )?take(s)? " + ARG.AMOUNT + " extra turn(s)? after this one",
        MagicTargetHint.Positive,
        MagicTiming.SecondMain,
        "+Turn"
    ) {
        @Override
        public MagicEventAction getAction(final Matcher matcher) {
            final int amount = ARG.amount(matcher);
            final MagicTargetFilter<MagicPlayer> filter = ARG.playersParse(matcher);
            return (game, event) -> {
                for (final MagicPlayer it : ARG.players(event, matcher, filter)) {
                    game.doAction(new ChangeExtraTurnsAction(it, amount));
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
        pattern = Pattern.compile(aPattern + "(\\.|,)?", Pattern.CASE_INSENSITIVE);
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

    public static MagicRuleEventAction match(final String rule) {
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
                final String riderWithoutPrefix = rider.replaceAll("^(and|then|Then|If you do,) ", "");
                final MagicSourceEvent riderSourceEvent = build(riderWithoutPrefix);

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
            .replaceAll("discard ", "discards ")
            .replaceAll("reveal ", "reveals ")
            .replaceAll("(S|s)earch your ", "PN searches his or her ")
            .replaceAll("(S|s)huffle your ", "PN shuffles his or her ")
            .replaceAll("(Y|y)ou draw ", "PN draws ")
            .replaceAll("(D|d)raw ", "PN draws ")
            .replaceAll("(Y|y)ou put ", "PN puts ")
            .replaceAll("(P|p)ut ", "PN puts ")
            .replaceAll("(C|c)reate ", "PN creates ")
            .replaceAll("(S|s)acrifice ", "PN sacrifices ")
            .replaceAll("(Y|y)ou don't ", "PN doesn't ")
            .replaceAll("(Y|y)ou do ", "PN does ")
            .replaceAll("(Y|y)ou gain ", "PN gains ")
            .replaceAll("(Y|y)ou lose ", "PN loses ")
            .replaceAll("(Y|y)ou control ", "PN controls ")
            .replaceAll("(Y|y)our ", "PN's ")
            .replaceAll("(Y|y)ou\\b", "PN")
            .replaceAll("(C|c)hoose one ", "$1hoose one\\$ ")
            ;
    }

    public static String mayTense(final String text) {
        return text
            .replaceAll("PN's hand ", "his or her hand ")
            .replaceAll("PN searches ", "search ")
            .replaceAll("PN shuffles ", "shuffle ")
            .replaceAll("PN draws", "draw ")
            .replaceAll("(D|d)raws ", "$1raw ")
            .replaceAll("(S|s)acrifices ", "$1acrifice ")
            .replaceAll("(G|g)ains ", "$1ain ")
            .replaceAll("(L|l)oses ", "$1lose ")
            .replaceAll("PN puts ", "put ")
            .replaceAll("reveals ", "reveal ")
            .replaceAll("you don't", "he or she doesn't")
            .replaceFirst("^PN ", "")
            ;
    }

    public static String addChoiceIndicator(final MagicChoice choice, final String text) {
        final MagicTargetChoice tchoice = choice.getTargetChoice();
        if (tchoice.isValid()) {
            final Pattern p = Pattern.compile(Pattern.quote(tchoice.getTargetDescription()), Pattern.CASE_INSENSITIVE);
            final Matcher m = p.matcher(text);
            return m.replaceFirst("$0\\$");
        } else if (choice.isValid()) {
            //replace final period with target indicator and period
            return text.replaceAll("\\.$", "\\$.");
        } else {
            return text;
        }
    }

    private static String renameThisThat(final String text) {
        final String replaceThis = text.replaceAll("\\b(T|t)his " + ARG.THING + "( |\\.|'s|\\b)" + ARG.EVENQUOTES, "SN$3");

        // only perform 'that' replacement for part of text before before first occurrence of 'target'
        final String[] parts = replaceThis.replaceAll("\\b(T|t)arget\\b", "|$1arget|").split("\\|");
        parts[0] = parts[0].replaceAll("\\b(T|t)hat " + ARG.THING + "( |\\.|'s|\\b)" + ARG.EVENQUOTES, "RN$3");
        final String result = String.join("", parts);
        return result;
    }

    private static String concat(final String part0, final String[] parts) {
        final StringBuilder res = new StringBuilder(part0);
        for (int i = 1; i < parts.length; i++) {
            res.append(' ').append(parts[i]);
        }
        return res.toString();
    }

    static final Pattern INTERVENING_IF = Pattern.compile("if " + ARG.COND + ", " + ARG.ANY, Pattern.CASE_INSENSITIVE);
    static final Pattern EFFECT_UNLESS = Pattern.compile(ARG.WORDRUN + " unless you " + ARG.COST, Pattern.CASE_INSENSITIVE);
    static final Pattern MAY_DO = Pattern.compile("you may " + ARG.MAY_COST + "\\. if you do, .+", Pattern.CASE_INSENSITIVE);
    static final Pattern MAY_DONT = Pattern.compile("you may " + ARG.COST + "\\. if you don't, .+", Pattern.CASE_INSENSITIVE);

    public static MagicSourceEvent create(final String text) {
        return build(renameThisThat(text));
    }

    private static MagicSourceEvent build(final String text) {
        final String[] part = text.split("~");
        final String rule = part[0];

        // handle intervening if clause
        final Matcher ifMatcher = INTERVENING_IF.matcher(rule);
        final boolean ifMatched = ifMatcher.matches();
        final MagicCondition ifCond = ifMatched ? MagicConditionParser.buildCompose(ARG.cond(ifMatcher)) : MagicCondition.NONE;
        String ruleWithoutIf = ifMatched ? ARG.any(ifMatcher) : rule;

        // rewrite effect unless clause into "you may <cost>, if you don't, <effect>"
        final Matcher unlessMatcher = EFFECT_UNLESS.matcher(ruleWithoutIf);
        if (unlessMatcher.matches()) {
            ruleWithoutIf = "You may " + ARG.cost(unlessMatcher) + " If you don't, " + ARG.wordrun(unlessMatcher) + ".";
        }

        // handle you may <cost>. if you do, <effect>
        final Matcher mayDoMatcher = MAY_DO.matcher(ruleWithoutIf);
        final boolean mayDoMatched = mayDoMatcher.matches();
        final MagicMatchedCostEvent mayDoCost = mayDoMatched ?
            new MagicRegularCostEvent(ARG.cost(mayDoMatcher)) :
            MagicRegularCostEvent.NONE;
        String prefix = mayDoMatched ? "^(Y|y)ou may [^\\.]+\\. If you do, " : "^(Y|y)ou may ";

        // handle you may <cost>. if you don't, <effect>
        final Matcher mayDontMatcher = MAY_DONT.matcher(ruleWithoutIf);
        final boolean mayDontMatched = mayDontMatcher.matches();
        final MagicMatchedCostEvent mayDontCost = mayDontMatched ?
            new MagicRegularCostEvent(ARG.cost(mayDontMatcher)) :
            MagicRegularCostEvent.NONE;
        prefix = mayDontMatched ? "^(Y|y)ou may [^\\.]+\\. If you don't, " : prefix;

        final String ruleWithoutMay = ruleWithoutIf.replaceFirst(prefix, "");
        final boolean optional = ruleWithoutMay.length() < ruleWithoutIf.length();
        final String effect = ruleWithoutMay.replaceFirst("^have ", "");

        final MagicRuleEventAction ruleAction = match(effect);
        final Matcher matcher = ruleAction.matched(effect);

        // action may be composed from rule and riders
        final MagicEventAction action = computeEventAction(ruleAction.getAction(matcher), part);

        final MagicTargetPicker<?> picker = ruleAction.getPicker(matcher);
        final MagicChoice choice = ruleAction.getChoice(matcher);
        final String pnMayChoice = capitalize(ruleWithoutMay).replaceFirst("\\.", "?");

        final String contextRule = personalize(choice, concat(ruleWithoutMay, part));
        final String playerRule = personalize(choice, concat(rule, part));

        //'If you don't' effect cannot have a choice as MagicMayChoice only prompts for choice in "Yes" case
        if (mayDontMatched && choice.isValid()) {
            throw new RuntimeException("'If you don't' effect should not have choice: \"" + effect + "\"");
        }

        final MagicChoiceFactory choiceFact = (source, player, ref) -> {
            if (mayDoMatched) {
                return new MagicMayChoice(
                    MagicMessage.replaceName(capitalize(ARG.cost(mayDoMatcher)) + "? If you do, " + effect, source, player, ref),
                    choice
                );
            } else if (mayDontMatched) {
                return new MagicMayChoice(
                    MagicMessage.replaceName(capitalize(ARG.cost(mayDontMatcher)) + "? If you don't, " + effect, source, player, ref)
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

        final String eventDesc =
              mayDoMatched ? "PN may$ " + ARG.cost(mayDoMatcher) + ". If PN does, " + contextRule
            : mayDontMatched ? "PN may$ " + ARG.cost(mayDontMatcher) + ". If PN doesn't, " + contextRule
            : optional ? "PN may$ " + mayTense(contextRule)
            : capitalize(playerRule);

        return new MagicSourceEvent(
            ruleAction,
            matcher,
            ifCond,
            choiceFact,
            picker,
            (game, event) -> {
                if (ifCond.accept(event) == false) {
                    return;
                }

                final MagicMatchedCostEvent matchedCost =
                    mayDoMatched ? mayDoCost
                  : mayDontMatched ? mayDontCost
                  : MagicRegularCostEvent.NONE;

                final MagicEvent costEvent = matchedCost.getEvent(event.getSource());

                if (optional == false || (event.isYes() && costEvent.isSatisfied())) {
                    if (matchedCost != MagicRegularCostEvent.NONE) {
                        game.addEvent(costEvent);
                    }
                    if (mayDontMatched == false) {
                        action.executeEvent(game, event);
                    }
                } else {
                    if (mayDontMatched) {
                        action.executeEvent(game, event);
                    }
                }
            },
            eventDesc
        );
    }
}
