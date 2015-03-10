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
            final MagicGame game = source.getGame();
            final int n = game.filterPermanents(source.getController(),MagicTargetFilterFactory.ARTIFACT_YOU_CONTROL).size();
            final int cost= Math.max(0,8-n)
            return cost==0 ?
                [new MagicPayManaCostEvent(source,"{U}{U}")]:
                [new MagicPayManaCostEvent(source,"{"+cost.toString()+"}{U}{U}")];
        }
    }
]