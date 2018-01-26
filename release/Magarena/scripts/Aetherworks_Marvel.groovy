def action = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        game.doAction(CastCardAction.WithoutManaCost(event.getPlayer(), it, MagicLocationType.OwnersLibrary, MagicLocationType.Graveyard));
        final MagicCardList copyTopCards = new MagicCardList(event.getRefCardList());
        copyTopCards.removeCard(it);
        copyTopCards.shuffle();
        copyTopCards.each({ game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary)) });
    });
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.CounterFlash),
        "Cast"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayEnergyEvent(source, 6)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicPlayer player = source.getController();
            final MagicCardList topCards = player.getLibrary().getCardsFromTop(6);
            return new MagicEvent(
                source,
                player,
                new MagicFromCardListChoice(topCards, 1, true),
                topCards,
                action,
                "Look at the top six cards of your library. " +
                "PN may cast a card\$ from among them without paying its mana cost. " +
                "Put the rest on the bottom of PN's library in a random order."
            );
        }
    }
]

