package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.MagicPermanent;
import magic.model.MagicManaCost;
import magic.model.MagicCostManaType;
import magic.model.MagicAmount;
import magic.model.action.PlayCardAction;
import magic.model.action.PutItemOnStackAction;
import magic.model.action.RemoveCardAction;
import magic.model.choice.MagicChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetFilter;

import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

public class MagicHandCastActivation extends MagicActivation<MagicCard> implements MagicChangeCardDefinition, MagicCardEvent {

    public static final MagicCondition[] CARD_CONDITION = new MagicCondition[]{
        MagicCondition.CARD_CONDITION,
    };

    final boolean usesStack;

    public MagicHandCastActivation(final MagicCardDefinition cdef) {
        super(
            CARD_CONDITION,
            cdef.getActivationHints(),
            "Cast"
        );
        usesStack = cdef.usesStack();
    }

    protected MagicHandCastActivation(final MagicActivationHints hints, final String txt) {
        super(MagicActivation.NO_COND, hints, txt);
        usesStack = true;
    }

    protected MagicHandCastActivation(final MagicCondition[] conditions, final MagicActivationHints hints, final String txt) {
        super(conditions, hints, txt);
        usesStack = true;
    }

    @Override
    boolean usesStack() {
        return usesStack;
    }

    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return source.getCostEvent();
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            EVENT_ACTION,
            "Play SN."
        );
    }

    private final MagicEventAction EVENT_ACTION = genPlayEventAction(MagicLocationType.OwnersHand);

    protected MagicEventAction genPlayEventAction(final MagicLocationType fromLocation) {
        return (final MagicGame game, final MagicEvent event) -> {
            final MagicCard card = event.getCard();
            if (card.getCardDefinition().isLand()) {
                game.incLandsPlayed();
            }

            game.doAction(new RemoveCardAction(card, fromLocation));

            if (usesStack) {
                final MagicCardOnStack cardOnStack=new MagicCardOnStack(
                    card,
                    MagicHandCastActivation.this,
                    game.getPayedCost()
                );
                cardOnStack.setFromLocation(fromLocation);
                game.doAction(new PutItemOnStackAction(cardOnStack));
            } else {
                game.doAction(new PlayCardAction(card,card.getController()));
            }
        };
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        throw new RuntimeException(getClass() + " did not override executeEvent");
    }

    @Override
    public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
        return cardOnStack.getCardDefinition().getCardEvent().getEvent(cardOnStack, payedCost);
    }

    @Override
    MagicChoice getChoice(final MagicCard source) {
        final MagicCardOnStack cardOnStack=new MagicCardOnStack(source,this,MagicPayedCost.NO_COST);
        return cardOnStack.getEvent().getChoice();
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addHandAct(this);
    }

    public static final MagicHandCastActivation create(final MagicCardDefinition cardDef, final String costs, final String name) {
        return create(cardDef, CARD_CONDITION, costs, name);
    }

    public static final MagicHandCastActivation create(final MagicCardDefinition cardDef, final MagicCondition cond, final String costs, final String name) {
        return create(cardDef, new MagicCondition[]{cond, MagicCondition.CARD_CONDITION}, costs, name);
    }

    public static final MagicHandCastActivation create(final MagicCardDefinition cardDef, final MagicCondition[] conds, final String costs, final String name) {
        final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.buildCast(costs);
        assert matchedCostEvents.size() > 0;

        return new MagicHandCastActivation(conds, cardDef.getActivationHints(), name) {
            @Override
            public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
                final List<MagicEvent> costEvents = new LinkedList<MagicEvent>();
                for (final MagicMatchedCostEvent matched : matchedCostEvents) {
                    costEvents.add(matched.getEvent(source));
                }
                return costEvents;
            }
        };
    }

    public static final MagicHandCastActivation reduction(final MagicCardDefinition cardDef, final MagicAmount amount) {
        return new MagicHandCastActivation(CARD_CONDITION, cardDef.getActivationHints(), "Cast") {
            @Override
            public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
                return Collections.<MagicEvent>singletonList(
                    new MagicPayManaCostEvent(
                        source,
                        source.getGameCost().reduce(
                            amount.getAmount(source, source.getController())
                        )
                    )
                );
            }
            @Override
            public void change(final MagicCardDefinition cdef) {
                cdef.setHandAct(this);
            }
        };
    }

    public static final MagicHandCastActivation affinity(final MagicCardDefinition cardDef, final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicHandCastActivation(CARD_CONDITION, cardDef.getActivationHints(), "Cast") {
            @Override
            public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
                return Collections.<MagicEvent>singletonList(
                    new MagicPayManaCostEvent(
                        source,
                        source.getGameCost().reduce(
                            source.getController().getNrOfPermanents(filter)
                        )
                    )
                );
            }
            @Override
            public void change(final MagicCardDefinition cdef) {
                cdef.setHandAct(this);
            }
        };
    }

    public static final MagicHandCastActivation awaken(final MagicCardDefinition cardDef, final int n, final String costs) {
        final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.buildCast(costs);
        assert matchedCostEvents.size() > 0;

        return new MagicHandCastActivation(CARD_CONDITION, cardDef.getActivationHints(), "Awaken") {
            @Override
            public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
                final List<MagicEvent> costEvents = new LinkedList<MagicEvent>();
                for (final MagicMatchedCostEvent matched : matchedCostEvents) {
                    costEvents.add(matched.getEvent(source));
                }
                return costEvents;
            }
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                final MagicEvent ev = cardDef.getCardEvent().getEvent(cardOnStack, payedCost);
                final MagicEventAction effect = ev.getEventAction();

                final MagicEventAction awaken = (final MagicGame game, final MagicEvent event) -> {
                    ev.getEventAction().executeEvent(game, event);
                    game.addEvent(new MagicAwakenEvent(event.getSource(), event.getPlayer(), n));
                };
                return new MagicEvent(
                    ev.getSource(),
                    ev.getPlayer(),
                    ev.getChoice(),
                    ev.getTargetPicker(),
                    ev.getRef(),
                    awaken,
                    ev.getDescription() + " Awaken " + n + "."
                );
            }
        };
    }

    public static final MagicHandCastActivation emerge(final MagicCardDefinition cardDef, final MagicManaCost manaCost) {
        return new MagicHandCastActivation(CARD_CONDITION, cardDef.getActivationHints(), "Emerge") {
            @Override
            public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
                return Collections.<MagicEvent>singletonList(
                    new MagicEmergeCostEvent(
                        source,
                        manaCost
                    )
                );
            }
        };
    }
}
