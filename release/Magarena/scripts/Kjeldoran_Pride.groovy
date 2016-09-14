[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Attach"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{U}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                PosOther("target creature", source.getEnchantedPermanent()),
                this,
                "Attach SN to target creature\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AttachAction(
                    event.getPermanent(),
                    it
                ));
            });
        }
    }
]
