package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.action.MoveCardAction;
import magic.model.action.PutItemOnStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.stack.MagicCardOnStack;


public class MagicCascadeTrigger extends MagicWhenSpellIsCastTrigger {

    private static final MagicCascadeTrigger INSTANCE = new MagicCascadeTrigger();

    private MagicCascadeTrigger() {}

    public static MagicCascadeTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack spell) {
        return new MagicEvent(
            spell,
            this,
            "Cascade"
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCardList library = event.getPlayer().getLibrary();
        final MagicCardList exiled = new MagicCardList();
        MagicCard nonland = MagicCard.NONE;
        // exile cards from the top of your library until you exile a nonland card 
        // whose converted mana cost is less than this spell's converted mana cost. 
        while (nonland == MagicCard.NONE && library.isEmpty() == false) {
            final MagicCard top = library.getCardAtTop();
            game.doAction(new MagicRemoveCardAction(
                top,
                MagicLocationType.OwnersLibrary
            ));
            game.doAction(new MoveCardAction(
                top,
                MagicLocationType.OwnersLibrary,
                MagicLocationType.Exile
            ));
            exiled.add(top);
            if (top.hasType(MagicType.Land) == false && top.getConvertedCost() < event.getCardOnStack().getConvertedCost()) {
                nonland = top;
            }
        }
        // You may cast that card without paying its mana cost. But paying additional costs.
        //FIXME: Needs to check that additional costs can be paid/casting conditions are met.
        if (nonland.isInExile()) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                new MagicMayChoice("Cast " + nonland + " without paying its mana cost?"),
                nonland,
                CAST_ACTION,
                "You may$ cast RN without paying its mana cost."
            ));
        }
        // Then put all cards exiled this way that weren't cast on the bottom of your library in random order
        game.addEvent(new MagicEvent(
            event.getSource(),
            event.getPlayer(),
            exiled,
            RESTORE_CARDS,
            "Put all cards exiled this way that weren't cast on the bottom of PN's library in random order"
        ));
    }

    private final MagicEventAction CAST_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicCard card = event.getRefCard();
                game.doAction(new MagicRemoveCardAction(card, MagicLocationType.Exile));
                final MagicCardOnStack cardOnStack=new MagicCardOnStack(
                    card,
                    MagicPayedCost.NO_COST
                );
                cardOnStack.setFromLocation(MagicLocationType.Exile);
                game.doAction(new PutItemOnStackAction(cardOnStack));
            }
        }
    };
    
    private final MagicEventAction RESTORE_CARDS = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList cards = event.getRefCardList();
            cards.shuffle();
            for (final MagicCard card : cards) {
                if (card.isInExile()) {
                    game.doAction(new MagicRemoveCardAction(
                        card,
                        MagicLocationType.Exile
                    ));
                    game.doAction(new MoveCardAction(
                        card,
                        MagicLocationType.Exile,
                        MagicLocationType.BottomOfOwnersLibrary
                    ));
                }
            }
        }
    };
}
