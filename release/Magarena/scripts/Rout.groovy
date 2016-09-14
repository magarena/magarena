[
    new MagicHandCastActivation(
        new MagicActivationHints(MagicTiming.Removal,true),
        "Flash"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                MagicPayManaCostEvent.Cast(source, "{5}{W}{W}")
            ];
        }
    }
]
