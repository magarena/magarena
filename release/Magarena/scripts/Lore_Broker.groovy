[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Each player draws a card, then discards a card"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
                game.doAction(new MagicDrawAction(player));
                game.addEvent(new MagicDiscardEvent(event.getSource(),player));
            }
        }
    }
]
