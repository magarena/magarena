[
    new MagicStatic(MagicLayer.CostIncrease) {
        @Override
        public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.hasColor(MagicColor.Blue) && source.isFriend(card)) {
                return cost.increase(MagicCostManaType.Blue, 1);
            } else {
                return cost;
            }
        }
    }
]
