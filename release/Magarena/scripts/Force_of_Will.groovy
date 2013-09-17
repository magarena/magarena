[
    new MagicCardActivation(
        new MagicActivationHints(MagicTiming.Counter,true),
        "Alt"
    ) {
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicTargetFilter.MagicOtherCardTargetFilter(
                    MagicTargetFilter.TARGET_BLUE_CARD_FROM_HAND, 
                    source
                ),
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
