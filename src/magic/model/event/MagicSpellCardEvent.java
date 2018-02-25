package magic.model.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicMessage;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayerState;
import magic.model.action.ChangeCardDestinationAction;
import magic.model.action.ChangePlayerStateAction;
import magic.model.action.EnqueueTriggerAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicOrChoice;
import magic.model.stack.MagicCardOnStack;

public abstract class MagicSpellCardEvent implements MagicCardEvent,MagicEventAction,MagicChangeCardDefinition {

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.setEvent(this);
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        throw new RuntimeException(getClass() + " did not override executeEvent");
    }

    public static MagicSpellCardEvent create(final MagicCardDefinition cdef, final String rule) {
        if (cdef.hasAbility(MagicAbility.Entwine)) {
            return Entwine(rule);
        }
        if (cdef.hasAbility(MagicAbility.Buyback)) {
            return Buyback(rule);
        }
        if (cdef.hasAbility(MagicAbility.HauntSpell)) {
            return Haunt(rule);
        }
        if (cdef.hasAbility(MagicAbility.Ascend)) {
            return Ascend(rule);
        }
        final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(rule);
        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                return sourceEvent.getEvent(cardOnStack, payedCost);
            }
        };
    }

    private static MagicSpellCardEvent Buyback(final String rule) {
        final MagicSourceEvent effect = MagicRuleEventAction.create(rule);
        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                final MagicEvent event = effect.getEvent(cardOnStack);
                return new MagicEvent(
                    event.getSource(),
                    event.getChoice(),
                    payedCost,
                    this,
                    event.getDescription()
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                effect.getAction().executeEvent(game, event);
                final MagicCardOnStack spell = event.getCardOnStack();
                if (spell.isKicked()) {
                    game.doAction(new ChangeCardDestinationAction(spell, MagicLocationType.OwnersHand));
                    game.logAppendMessage(
                        event.getPlayer(),
                        MagicMessage.format("%s is put into %s's hand as it resolves.", spell, event.getPlayer())
                    );
                }
            }
        };
    }

    private static MagicSpellCardEvent Haunt(final String rule) {
        final MagicSourceEvent effect = MagicRuleEventAction.create(rule);
        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
                final MagicEvent event = effect.getEvent(cardOnStack);
                return new MagicEvent(
                    event.getSource(),
                    event.getChoice(),
                    payedCost,
                    this,
                    event.getDescription()
                );
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                effect.getAction().executeEvent(game, event);
                game.doAction(new EnqueueTriggerAction(
                    new MagicHauntEvent(event.getCardOnStack(), effect)
                ));
            }
        };
    }

    private static MagicSpellCardEvent Entwine(final String rule) {
        final Pattern pattern = Pattern.compile("choose one — \\(1\\) (?<effect1>.*) \\(2\\) (?<effect2>.*)", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(rule);
        if (!matcher.matches()) {
            throw new RuntimeException("unknown effect: \"" + rule + "\"");
        }
        final String text1 = matcher.group("effect1");
        final String text2 = matcher.group("effect2");
        final MagicSourceEvent effect1 = MagicRuleEventAction.create(text1);
        final MagicSourceEvent effect2 = MagicRuleEventAction.create(text2);
        final MagicChoice choice1 = effect1.getChoice();
        final MagicChoice choice2 = effect2.getChoice();
        if (choice1.isValid() && choice2.isValid()) {
            throw new RuntimeException("effect cannot have two valid choices: \"" + rule + "\"");
        }
        final String desc1 = MagicRuleEventAction.personalizeWithChoice(choice1, text1);
        final String desc2 = MagicRuleEventAction.personalizeWithChoice(choice2, text2);

        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    cardOnStack,
                    payedCost.isKicked() ?
                        (choice1.isValid() ? choice1 : choice2):
                        new MagicOrChoice(
                            choice1,
                            choice2
                        ),
                    payedCost,
                    this,
                    payedCost.isKicked() ?
                        desc1 + " " + desc2 :
                        "Choose one$ — • " + desc1 +  " • " + desc2
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                if (event.isKicked()) {
                    event.executeAllEvents(game, effect1, effect2);
                } else {
                    event.executeModalEvent(game, effect1, effect2);
                }
            }
        };
    }

    private static MagicSpellCardEvent Ascend(final String rule) {
        final MagicSourceEvent effect = MagicRuleEventAction.create(rule);
        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                final MagicEvent event = effect.getEvent(cardOnStack);
                return new MagicEvent(
                    event.getSource(),
                    event.getChoice(),
                    payedCost,
                    this,
                    event.getDescription()
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                if (!event.getPlayer().hasState(MagicPlayerState.CitysBlessing) &&
                        event.getPlayer().getNrOfPermanents() >= 10) {
                    game.doAction(new ChangePlayerStateAction(event.getPlayer(), MagicPlayerState.CitysBlessing));
                }
                effect.getAction().executeEvent(game, event);
            }
        };
    }
}
