package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicMessage;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicCopyPermanentPicker;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicTargetType;
import magic.model.target.MagicTargetPicker;
import magic.model.condition.MagicCondition;
import magic.model.choice.MagicTargetChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.action.EnterAsCopyAction;
import magic.model.action.PlayCardFromStackAction;
import magic.model.action.MagicPlayMod;

public abstract class MagicETBEvent implements MagicCardEvent,MagicEventAction,MagicChangeCardDefinition {

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.setEvent(this);
    }

    public static MagicETBEvent copyOf(final String desc) {
        final MagicTargetChoice choice = new MagicTargetChoice("a " + desc);
        final MagicTargetPicker<?> targetPicker = choice.getTargetFilter().acceptType(MagicTargetType.Permanent) ?
            MagicCopyPermanentPicker.create() :
            MagicGraveyardTargetPicker.PutOntoBattlefield;
        return new MagicETBEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    cardOnStack,
                    new MagicMayChoice(choice),
                    targetPicker,
                    this,
                    "PN may$ have SN enter the battlefield as a copy of any " + desc + "$."
                );
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                if (event.isYes()) {
                    game.doAction(new EnterAsCopyAction(event.getCardOnStack(), event.getTarget()));
                } else {
                    game.logAppendMessage(event.getPlayer(), "Put ${event.getCardOnStack()} onto the battlefield.");
                    game.doAction(new PlayCardFromStackAction(
                        event.getCardOnStack()
                    ));
                }
            }
        };
    }

    public static MagicETBEvent tappedUnless(final MagicCondition condition) {
        return new MagicETBEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    cardOnStack,
                    this,
                    condition.accept(cardOnStack) ?
                        "Put SN onto the battlefield tapped." :
                        "Put SN onto the battlefield."
                );
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                if (condition.accept(event)) {
                    game.doAction(new PlayCardFromStackAction(
                        event.getCardOnStack(),
                        MagicPlayMod.TAPPED
                    ));
                } else {
                    game.doAction(new PlayCardFromStackAction(
                        event.getCardOnStack()
                    ));
                }
            }
        };
    }

    public static MagicETBEvent tappedUnless(final String cost) {
        final MagicMatchedCostEvent costFact = new MagicRegularCostEvent(cost);
        return new MagicETBEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    cardOnStack,
                    new MagicMayChoice(MagicRuleEventAction.capitalize(cost) + "?"),
                    this,
                    "PN may$ " + cost + ". If you don't, SN enters the battlefield tapped."
                );
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicEvent costEvent = costFact.getEvent(event.getSource());
                if (event.isYes() && costEvent.isSatisfied()) {
                    game.addEvent(costEvent);
                    game.doAction(new PlayCardFromStackAction(
                        event.getCardOnStack()
                    ));
                    game.logAppendMessage(
                        event.getPlayer(),
                        "Put " + MagicMessage.getCardToken(event.getSource()) + " on the battlefield."
                    );
                } else {
                    game.doAction(new PlayCardFromStackAction(
                        event.getCardOnStack(),
                        MagicPlayMod.TAPPED
                    ));
                    game.logAppendMessage(
                        event.getPlayer(),
                        "Put " + MagicMessage.getCardToken(event.getSource()) + " on the battlefield tapped."
                    );
                }
            }
        };
    }
}
