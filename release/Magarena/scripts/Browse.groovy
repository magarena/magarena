def TakeCard = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard chosen ->
        for (final MagicCard card : event.getRefCardList()) {
            game.doAction(new ShiftCardAction(
                card,
                MagicLocationType.OwnersLibrary,
                card == chosen ?
                    MagicLocationType.OwnersHand :
                    MagicLocationType.Exile
            ));
        }
    });
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Look"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
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
            final MagicCardList topCards = event.getPlayer().getLibrary().getCardsFromTop(5);
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(topCards, 1),
                MagicGraveyardTargetPicker.ReturnToHand,
                topCards,
                TakeCard,
                "PN puts a card into his or her hand."
            ));
        }
    }
]
