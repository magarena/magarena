[
    new MagicWeakenCreatureActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "-1/-1") {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source,"{4}")
            ];
        }
    }
]
