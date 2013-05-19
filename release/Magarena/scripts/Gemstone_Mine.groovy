[
    new MagicManaActivation(
        MagicManaType.ALL_TYPES,
        [
            MagicCondition.CAN_TAP_CONDITION,
            MagicConditionFactory.ChargeCountersAtLeast(1)
        ],
        2
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent permanent) {
            return (permanent.getCounters(MagicCounterType.Charge) == 1) ?
                [
                    new MagicTapEvent(permanent),
                    new MagicRemoveCounterEvent(
                        permanent,
                        MagicCounterType.Charge,
                        1
                    ),
                    new MagicSacrificeEvent(permanent)
                ]:
                [
                    new MagicTapEvent(permanent),
                    new MagicRemoveCounterEvent(
                        permanent,
                        MagicCounterType.Charge,
                        1
                    )
                ];
        }
    }
]
