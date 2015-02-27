[
    new MagicManaActivation(
        MagicManaType.ALL_TYPES,
        2
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent permanent) {
            [
                new MagicSacrificePermanentEvent(permanent,new MagicTargetChoice("a Saproling to sacrifice"))
            ];
        }
    }
]
