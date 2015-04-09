[
    new MagicCardActivation(
        [
            MagicConditionFactory.OtherCardInHand(MagicColor.Blue, 1), 
            MagicCondition.CARD_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Main, true),
        "Alt"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                MagicTargetFilterFactory.BLUE_CARD_FROM_HAND.except(source),
                MagicTargetHint.None,
                "a blue card from your hand"
            );
            return [
                new MagicExileCardEvent(source, targetChoice)
            ];
        }
    }
]
