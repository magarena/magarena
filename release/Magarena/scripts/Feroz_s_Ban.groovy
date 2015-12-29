[
    new MagicStatic(MagicLayer.CostIncrease) {
        @Override
        public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.isCreature()) {
                return cost.increase(MagicCostManaType.Generic, 2);
            } else {
                return cost;
            }
        }
    }
]
