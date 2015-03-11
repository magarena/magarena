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
            final int n = game.filterPermanents(MagicTargetFilterFactory.CREATURE).size();
            final int cost= Math.max(0,8-n)
            return cost==0 ?
                [new MagicPayManaCostEvent(source,"{R}")]:
                [new MagicPayManaCostEvent(source,"{"+cost.toString()+"}{R}")];
        }
    }
]