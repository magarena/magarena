[
    new MagicTapCreatureActivation(
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source,"{W}")
            ];
        }
    }
]
