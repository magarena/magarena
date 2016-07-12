[
    new MagicStatic(MagicLayer.CostReduction) {
        @Override
        public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (source.isFriend(card) && card.isCreature()) {
                return cost.reduce(1);
            } else {
                return cost;
            }
        }
    }
]
