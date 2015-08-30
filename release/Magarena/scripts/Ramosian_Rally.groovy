def AN_UNTAPPED_CREATURE_YOU_CONTROL = new MagicTargetChoice("an untapped creature you control");

[
    new MagicHandCastActivation(
        [MagicCondition.PLAINS_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Pump, true),
        "Tap"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicTapPermanentEvent(source, AN_UNTAPPED_CREATURE_YOU_CONTROL)
            ];
        }
    }
]
