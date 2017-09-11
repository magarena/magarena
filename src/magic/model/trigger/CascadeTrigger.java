package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.action.ShiftCardAction;
import magic.model.action.CastCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.stack.MagicCardOnStack;


public class CascadeTrigger extends ThisSpellIsCastTrigger {

    private static final CascadeTrigger INSTANCE = new CascadeTrigger();

    private CascadeTrigger() {}

    public static CascadeTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack spell) {
        return new MagicEvent(
            spell,
            this,
            "Cascade."
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
            game.doAction(new ShiftCardAction(
                top,
                MagicLocationType.OwnersLibrary,
                MagicLocationType.Exile
            ));
            exiled.add(top);
            if (top.hasType(MagicType.Land) == false && top.getConvertedCost() < event.getCardOnStack().getConvertedCost()) {
                nonland = top;
            }
        }
        // You may cast that card without paying its mana cost.
        // Then put all cards exiled this way that weren't cast on the bottom of your library in random order
        if (nonland.isInExile()) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                new MagicMayChoice("Cast " + nonland + " without paying its mana cost?"),
                exiled,
                CAST_ACTION,
                "PN may$ cast " + nonland + " without paying its mana cost. " +
                "Then put all cards exiled this way that weren't cast on the bottom of PN's library in random order."
            ));
        } else {
            game.addEvent(new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                exiled,
                RESTORE_CARDS,
                "Put all cards exiled this way that weren't cast on the bottom of PN's library in random order."
            ));
        }
    }

    private final MagicEventAction RESTORE_CARDS = (final MagicGame game, final MagicEvent event) -> {
        final MagicCardList cards = event.getRefCardList();
        cards.shuffle();
        for (final MagicCard card : cards) {
            game.doAction(new ShiftCardAction(
                card,
                MagicLocationType.Exile,
                MagicLocationType.BottomOfOwnersLibrary
            ));
        }
    };

    private final MagicEventAction CAST_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicCardList exiled = new MagicCardList(event.getRefCardList());
        if (event.isYes()) {
            final MagicCard card = exiled.removeCardAtTop();
            game.doAction(CastCardAction.WithoutManaCost(
                event.getPlayer(),
                card,
                MagicLocationType.Exile,
                MagicLocationType.Graveyard
            ));
        }
        game.addEvent(new MagicEvent(
            event.getSource(),
            event.getPlayer(),
            exiled,
            RESTORE_CARDS,
            ""
        ));
    };
}
