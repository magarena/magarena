[
    new MagicCardActivation(
        new MagicActivationHints(MagicTiming.Counter,true),
        "Alt"
    ) {
        public MagicEvent[] getCostEvent(final MagicCard source) {
            final MagicTargetFilter<MagicCard> filter = 
                new MagicTargetFilter.MagicOtherCardTargetFilter(MagicTargetFilter.TARGET_BLUE_CARD_FROM_HAND, source);
            final MagicTargetChoice targetChoice =
                new MagicTargetChoice(filter,false,MagicTargetHint.None,"a blue card from your hand");
            return [
                new MagicPayLifeEvent(source, 1),
                new MagicExileCardEvent(source, targetChoice)
            ];
        }
    }
]
