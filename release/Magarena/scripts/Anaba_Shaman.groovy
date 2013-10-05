[
    new MagicPingActivation(1) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source, "{R}")
            ];
        }
    }
]
