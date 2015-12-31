[
    new MagicStatic(MagicLayer.CostReduction) {
        @Override
        public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (source.isTapped() && card.isCreature() && source.isFriend(card)) {
                return cost.reduce(MagicCostManaType.Generic, 2);
            } else {
                return cost;
            }
        }
    }
]
