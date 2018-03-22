def CREATURE_POWER_4_OR_MORE_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().controlsPermanent(CREATURE_POWER_4_OR_MORE);
    }
};

[
    new MagicHandCastActivation(
        [CREATURE_POWER_4_OR_MORE_CONDITION, MagicCondition.NOT_SORCERY_CONDITION],
        new MagicActivationHints(MagicTiming.Pump,true),
        "Flash"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                MagicPayManaCostEvent.Cast(source, "{2}{R}")
            ];
        }
    }
]
