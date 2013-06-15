[
    new MagicTapCreatureActivation(
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{W/U}"),
                new MagicUntapEvent(source)
            ];
        }
    }
]
