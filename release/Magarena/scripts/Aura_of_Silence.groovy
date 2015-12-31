[
    new MagicStatic(MagicLayer.CostIncrease) {
        @Override
        public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (source.isEnemy(card) && 
                (card.isArtifact() || card.isEnchantment())) {
                return cost.increase(2);
            } else {
                return cost;
            }
        }
    }
]
