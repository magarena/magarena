def ZOMBIE_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().controlsPermanent(MagicSubType.Zombie);
    }
};

[
    new MagicGraveyardCastActivation(
        [MagicCondition.CARD_CONDITION, ZOMBIE_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Cast"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{B}")
            ];
        }
    }
]
