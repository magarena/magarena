[
    new MagicCardActivation(
        [
            MagicCondition.ONE_LIFE_CONDITION,
            MagicCondition.HAS_TWO_BLUE_CARDS_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Counter,true),
        "Alt"
    ) {
        public MagicEvent[] getCostEvent(final MagicCard source) {
            final MagicTargetFilter<MagicCard> filter = 
                new MagicTargetFilter.MagicOtherCardTargetFilter(MagicTargetFilter.TARGET_BLUE_CARD_FROM_HAND, source);
            final MagicTargetChoice targetChoice =
                new MagicTargetChoice(filter,false,MagicTargetHint.None,"a blue card from your hand");
            return [
                new MagicPayLifeEvent(source, source.getController(), 1),
                new MagicExileCardEvent(source, source.getController(), targetChoice)
            ];
        }
    }
]
