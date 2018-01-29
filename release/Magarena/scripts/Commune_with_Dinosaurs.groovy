def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicCardList copyTopCards = new MagicCardList(event.getRefCardList());
    event.processChosenCards(game, {
        final MagicCard chosen ->
        game.doAction(new ShiftCardAction(chosen, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
        copyTopCards.remove(chosen);
    });
    copyTopCards.each({
        game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary));
    });
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN looks at the top five cards of PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList topCards = player.getLibrary().getCardsFromTop(5);
            game.addEvent(new MagicEvent(
                event.getSource(),
                MagicFromCardListChoice(
                    topCards.find({
                        it.hasSubType(MagicSubType.Dinosaur) || it.hasType(MagicType.Land)
                    }),
                    1,
                    true
                ),
                topCards,
                action,
                "PN may reveal a Dinosaur or land card from among them\$ and put it into PN's hand. " +
                "PN puts the rest on the bottom of PN's library in any order."
            ));
        }
    }
]

