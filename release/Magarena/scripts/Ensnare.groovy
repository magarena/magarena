def choice = new MagicTargetChoice("an Island you control");

[
     new MagicCardActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Tapping),
        "Alt"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [ 
                new MagicRepeatedPermanentsEvent(source, choice, 2, MagicChainEventFactory.Bounce)
            ];
        }
    }
]
