[
    new MagicCardActivation(
        [MagicCondition.OPPONENTS_UPKEEP_CONDITION],
        new MagicActivationHints(MagicTiming.None,true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setCardAct(this);
        }
    }
]
