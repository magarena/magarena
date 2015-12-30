[
    new MagicHandCastActivation(
        new MagicActivationHints(MagicTiming.Removal,true),
        "Flash"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{5}{W}{W}")
            ];
        }
    }
]
