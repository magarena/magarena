[
     new MagicCardActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setCardAct(this);
        }

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final int n = source.getController().getDomain();
            final int cost= Math.max(0,5-n)
            return cost==0 ?
                [new MagicPayManaCostEvent(source,"{5}")]:
                [new MagicPayManaCostEvent(source,"{"+cost.toString()+"}{5}")];
        }
    }
]