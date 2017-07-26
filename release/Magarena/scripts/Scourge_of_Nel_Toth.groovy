def sac = new MagicRegularCostEvent("Sacrifice two creatures");

[
    new MagicGraveyardCastActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Cast"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{B}{B}"),
				sac.getEvent(source)
            ];
        }
    }
]

