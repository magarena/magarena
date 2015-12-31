[
    new MagicStatic(MagicLayer.CostReduction) {
        @Override
        public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.hasType(MagicType.Creature) && source.isFriend(card)) {
                final int amt = source.getCounters(MagicCounterType.PlusOne);
                return cost.reduce(MagicCostManaType.Generic, amt);
            } else {
                return cost;
            }
        }
    }
]
