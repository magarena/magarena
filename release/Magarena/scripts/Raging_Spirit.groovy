[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Colorless"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}"),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN becomes colorless until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddStaticAction(event.getPermanent(),
                new MagicStatic(MagicLayer.Color,MagicStatic.UntilEOT) {
                @Override
                public int getColorFlags(final MagicPermanent permanent, final int flags) {
                    return 0;
                }
            }));
        }
    }
]
