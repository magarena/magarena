[
    new MagicPermanentActivation(
        [
            MagicCondition.HELLBENT,
        ],
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card if PN has no cards in hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (MagicCondition.HELLBENT.accept(event.getSource())) {
                game.doAction(new DrawAction(event.getPlayer()));
            }
        }
    }
]
