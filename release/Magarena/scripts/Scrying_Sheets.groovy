def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        final MagicCard card = event.getRefCard();
        final MagicPlayer player = event.getPlayer();
        game.doAction(new RevealAction(card));
        game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
        game.logAppendMessage(player, "${player} puts (${card}) into his or her hand.")
    }
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
                new MagicPayManaCostEvent(source, "{1}{S}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN looks at the top card of his or her library. If that card is snow, "+
                "PN may reveal it and put it into his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = player.getLibrary().getCardsFromTop(1);
            for (final MagicCard card : cards) {
                game.doAction(new LookAction(card, player, "top card of your library"));
                if (card.hasType(MagicType.Snow)) {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        new MagicSimpleMayChoice(
                            MagicSimpleMayChoice.DRAW_CARDS,
                            1,
                            MagicSimpleMayChoice.DEFAULT_YES
                        ),
                        card,
                        action,
                        "\$"
                    ));
                }
            }
        }
    }
]
