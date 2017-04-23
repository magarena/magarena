[
    new MagicStatic(MagicLayer.CostIncrease) {
        @Override
        public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (source.isEnemy(card)) {
                return cost.increase(1);
            } else {
                return cost;
            }
        }
    }
]
