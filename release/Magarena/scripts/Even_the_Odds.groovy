def FEWER_CREATURES_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(MagicType.Creature) <
               source.getOpponent().getNrOfPermanents(MagicType.Creature);
    }
};

[
    new MagicHandCastActivation(
        [FEWER_CREATURES_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Token),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setHandAct(this);
        }
    }
]
