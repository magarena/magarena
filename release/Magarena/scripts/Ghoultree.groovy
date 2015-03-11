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
            final int n = game.filterCards(source.getController(), MagicTargetFilterFactory.CREATURE_CARD_FROM_GRAVEYARD).size();    
            final int cost= Math.max(0,7-n)
            return cost==0 ?
                [new MagicPayManaCostEvent(source,"{G}")]:
                [new MagicPayManaCostEvent(source,"{"+cost.toString()+"}{G}")];
        }
    }
]