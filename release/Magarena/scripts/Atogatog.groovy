def choice = new MagicTargetChoice("a Pegasus to sacrifice");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificePermanentEvent(source,choice)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getTarget(),
                this,
                "SN gets +X/+X until end of turn, where X is the sacrificed creature's power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent sacrificed=event.getRefPermanent();
            final int X = sacrificed.getPower();
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(), X, X));
        }
    }
]
