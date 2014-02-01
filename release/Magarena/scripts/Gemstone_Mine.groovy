[
    new MagicManaActivation(
        MagicManaType.ALL_TYPES,
        2
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent permanent) {
            return (permanent.getCounters(MagicCounterType.Mining) == 1) ?
                [
                    new MagicTapEvent(permanent),
                    new MagicRemoveCounterEvent(
                        permanent,
                        MagicCounterType.Mining,
                        1
                    ),
                    new MagicSacrificeEvent(permanent)
                ]:
                [
                    new MagicTapEvent(permanent),
                    new MagicRemoveCounterEvent(
                        permanent,
                        MagicCounterType.Mining,
                        1
                    )
                ];
        }
    }
]
