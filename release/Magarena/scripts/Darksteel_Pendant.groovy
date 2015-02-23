[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Look"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Look at the top card of your library. You may put that card on the bottom of your library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new MagicLookAction(card, "top card of your library"));   
                game.addEvent(new MagicScryEvent(event));
            }
        }
    }
]
