def NO_CARDS_IN_HAND_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getOpponent().getHandSize() == 0;
    }
};

[
     new MagicCardActivation(
        [NO_CARDS_IN_HAND_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "-Cost"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [new MagicPayManaCostEvent(source,"{U}{U}")];
        }
    }
]
