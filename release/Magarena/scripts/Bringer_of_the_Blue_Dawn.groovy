[
    new MagicCardActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main,true),
        "Alt"
    ) {
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{W}{U}{B}{R}{G}"),
            ];
        }
    }
]
