[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Pump"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{R}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Attacking creatures get +X/+0 until end of turn, " +
                "where X is SN's power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            ATTACKING_CREATURE.filter(event) each {
                game.doAction(new ChangeTurnPTAction(it, permanent.getPower(), 0));
            }
        }
    }
]
