[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +X/+0 until end of turn, where X is its power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicPermanent permanent = event.getPermanent()
                game.doAction(new ChangeTurnPTAction(permanent, permanent.getPower(), 0));
        }
    }
]
