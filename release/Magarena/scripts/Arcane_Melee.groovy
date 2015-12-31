[
    new MagicStatic(MagicLayer.CostReduction) {
        @Override
        public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.isInstant() || card.isSorcery()) {
                return cost.reduce(MagicCostManaType.Generic, 2);
            } else {
                return cost;
            }
        }
    }
]
