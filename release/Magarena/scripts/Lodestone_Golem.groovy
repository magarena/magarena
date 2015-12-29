[
    new MagicStatic(MagicLayer.CostIncrease) {
        @Override
        public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.isArtifact() == false) {
                return cost.increase(MagicCostManaType.Generic, 1);
            } else {
                return cost;
            }
        }
    }
]
