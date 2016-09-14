def TakeCard = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard chosen ->
        final MagicCardList library = new MagicCardList(event.getPlayer().getLibrary().getCardsFromTop(3));
        for (final MagicCard card : library) {
            if (card != chosen) {
                game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.Exile));
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
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PLAYER,
                this,
                "Target player\$ looks at the top three cards of his or her library, "+
                "puts one of them back on top of his or her library, then exiles the rest."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                final List<MagicCard> topCards = player.getLibrary().getCardsFromTop(3);
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    player,
                    new MagicFromCardListChoice(topCards,1),
                    MagicGraveyardTargetPicker.ReturnToHand,
                    TakeCard,
                    "PN puts a card on top of his or her library."
                ));
            });
        }
    }
]
