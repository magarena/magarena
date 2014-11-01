def SEVEN_LANDS_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getOpponent().getNrOfPermanents(MagicType.Land) >= 7;
    }
};

[
     new MagicCardActivation(
        [SEVEN_LANDS_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "-Cost"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [new MagicPayManaCostEvent(source,"{R}{R}")];
        }
    }
]
