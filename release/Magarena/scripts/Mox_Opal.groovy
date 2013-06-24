[
    new MagicManaActivation(
        MagicManaType.ALL_TYPES,
        [MagicCondition.METALCRAFT_CONDITION],
        2
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent permanent) {
            return [new MagicTapEvent(permanent)];
        }
    }
]
