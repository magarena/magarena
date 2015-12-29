[
    new MagicStatic(MagicLayer.CostIncrease) {
        @Override
        public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.hasColor(MagicColor.Black) && source.isFriend(card)) {
                return cost.increase(MagicCostManaType.Black, 1);
            } else {
                return cost;
            }
        }
    }
]
