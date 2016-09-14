[
    new MagicHandCastActivation(
        new MagicActivationHints(MagicTiming.Removal,true),
        "Flash"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{X}{2}{R}")
            ];
        }
    }
]
