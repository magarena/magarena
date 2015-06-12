def TakeCard = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard chosen ->
        final MagicCardList library = new MagicCardList(event.getPlayer().getLibrary().getCardsFromTop(5));
        for (final MagicCard card : library) {
            if (card == chosen) { //Isn't a draw action, a card is 'put' into hand
                game.doAction(new MoveCardAction(chosen, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                game.doAction(new RemoveCardAction(chosen, MagicLocationType.OwnersLibrary));
            } else {
                game.doAction(new MoveCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.Exile));
                game.doAction(new RemoveCardAction(card, MagicLocationType.OwnersLibrary));
            }
        }
    });
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Look"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{2}{U}{U}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN looks at the top five cards of his or her library, "+
                "puts one of them into his or her hand, and exiles the rest."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicCard> topCards = event.getPlayer().getLibrary().getCardsFromTop(5);
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(topCards,1),
                MagicGraveyardTargetPicker.ReturnToHand,
                TakeCard,
                "PN puts a card into his or her hand."
            ));
        }
    }
]
