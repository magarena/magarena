[
    new MagicCardActivation(
        [
            MagicConditionFactory.OtherCardInHand(MagicColor.White, 2), 
            MagicCondition.CARD_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Main, true),
        "Alt"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherCardTargetFilter(
                    MagicTargetFilterFactory.WHITE_CARD_FROM_HAND, 
                    source
                ),
                MagicTargetHint.None,
                "a white card from your hand"
            );
            return [
                new MagicExileCardEvent(source, targetChoice),
                new MagicExileCardEvent(source, targetChoice)
            ];
        }
    }
]
