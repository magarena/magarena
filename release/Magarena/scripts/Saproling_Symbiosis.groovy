[
    new MagicHandCastActivation(
        new MagicActivationHints(MagicTiming.Token,true),
        "Flash"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{5}{G}")
            ];
        }
    }
]
