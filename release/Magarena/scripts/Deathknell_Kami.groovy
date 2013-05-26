[
    new MagicPermanentActivation(
        [MagicConditionFactory.ManaCost("{2}")],
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +1/+1 until end of turn. " +
                "Sacrifice it at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new MagicChangeTurnPTAction(permanent,1,1));
            game.doAction(new MagicChangeStateAction(
                permanent,
                MagicPermanentState.SacrificeAtEndOfTurn,
                true
            ));
        }
    }
]
