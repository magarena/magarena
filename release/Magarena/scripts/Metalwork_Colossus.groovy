def NONCREATURE_ARTIFACT_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isArtifact() && !target.isCreature() && target.isController(player);
    }
};
[
    new MagicHandCastActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Draw, true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setHandAct(this);
        }

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            int n = 0;
            NONCREATURE_ARTIFACT_YOU_CONTROL.filter(source) each {
                n += it.getConvertedCost();
            }
            return [
                new MagicPayManaCostEvent(source, source.getGameCost().reduce(n))
            ];
        }
    }
]
