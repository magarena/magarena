[
    new MagicStatic(MagicLayer.CostIncrease) {
        @Override
        public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.isEnchantment() && 
                (card.hasColor(MagicColor.Green) || card.hasColor(MagicColor.White))) {
                return cost.increase(MagicCostManaType.Generic, 2);
            } else {
                return cost;
            }
        }
    }
]
