[
    new MagicStatic(MagicLayer.CostIncrease) {
        @Override
        public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (source.getGame().getTurnPlayer() != card.getController()) {
                return cost.increase(MagicCostManaType.Generic, 3);
            } else {
                return cost;
            }
        }
    }
]
