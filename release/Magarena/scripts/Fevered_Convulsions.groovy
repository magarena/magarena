[
    new MagicWeakenCreatureActivation(
        new MagicActivationHints(MagicTiming.Removal,true),
        "-1/-1") {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{B}{B}")
            ];
        }
    }
]
