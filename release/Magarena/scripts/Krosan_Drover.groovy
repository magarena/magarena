[
    new MagicStatic(MagicLayer.CostReduction) {
        @Override
        public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.isCreature() && card.getConvertedCost() >= 6 && source.isFriend(card)) {
                return cost.reduce(MagicCostManaType.Generic, 2);
            } else {
                return cost;
            }
        }
    }
]
