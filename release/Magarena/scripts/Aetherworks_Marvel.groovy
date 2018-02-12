def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicCardList topCards = new MagicCardList(event.getRefCardList());
    event.processChosenCards(game, {
        game.doAction(CastCardAction.WithoutManaCost(event.getPlayer(), it, MagicLocationType.OwnersLibrary, MagicLocationType.Graveyard));
        topCards.removeCard(it);
    });
    topCards.shuffle();
    topCards.each {
        game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary))
    }
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
            return new MagicEvent(
                source,
                this,
                "PN looks at the top six cards of PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList topCards = player.getLibrary().getCardsFromTop(6);
            game.doAction(new LookAction(topCards, player, "top six cards of your library");
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(topCards, 1, true),
                topCards,
                action,
                "PN may cast a card from among them\$ without paying its mana cost. " +
                "Put the rest on the bottom of PN's library in a random order."
            ));
        }
    }
]

