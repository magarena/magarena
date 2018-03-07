def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(CastCardAction.WithoutManaCost(event.getPlayer(), event.getRefCard(), MagicLocationType.OwnersLibrary, MagicLocationType.Graveyard));
    } else {
        game.doAction(new ShiftCardAction(event.getRefCard(), MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
    }
}

[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return (permanent.getController().getSpellsCast() == 1) ?
                new MagicEvent(
                    permanent,
                    cardOnStack,
                    this,
                    "Whenever PN cast his or her first spell each turn, reveal the top card of PN's library."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard topCard = event.getPlayer().getLibrary().getCardAtTop();
            if (topCard != MagicCard.NONE) {
                game.doAction(new RevealAction(topCard));
                if (!topCard.hasType(MagicType.Land) && topCard.getConvertedCost() < event.getRefCardOnStack().getConvertedCost()) {
                    game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice("Cast the card without paying its mana cost?"),
                    topCard,
                    action,
                    "If it's a nonland card with converted mana cost less than that spell's, PN may\$ cast it without paying its mana cost. If PN doesn't cast the revealed card, put it into PN's hand."
                    ));
                } else {
                    game.doAction(new ShiftCardAction(topCard, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                }
            }
        }
    }
]

