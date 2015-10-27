package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicAbility;
import magic.model.MagicLocationType;
import magic.model.MagicMessage;
import magic.model.stack.MagicCardOnStack;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicOrChoice;
import magic.model.action.ChangeCardDestinationAction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(rule);
        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                return sourceEvent.getEvent(cardOnStack);
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
                        String.format("%s is put into %s's hand as it resolves.",
                            MagicMessage.getCardToken(spell),
                            event.getPlayer()
                        )
                    );
                }
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
        final String desc1 = MagicRuleEventAction.personalize(text1) + (choice1.isValid() ? "$" : "");
        final String desc2 = MagicRuleEventAction.personalize(text2) + (choice2.isValid() ? "$" : "");

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
}
