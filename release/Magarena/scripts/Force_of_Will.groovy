[
    new MagicCardActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Counter,true),
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
                new MagicPayLifeEvent(source, 1),
                new MagicExileCardEvent(source, targetChoice)
            ];
        }
    }
]
