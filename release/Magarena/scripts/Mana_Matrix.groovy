[
    new MagicStatic(MagicLayer.CostReduction) {
        @Override
        public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (source.isFriend(card) && (card.isInstant() || card.isEnchantment())) {
                return cost.reduce(2);
            } else {
                return cost;
            }
        }
    }
]
