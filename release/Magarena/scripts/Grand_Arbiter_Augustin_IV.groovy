[
    new MagicStatic(MagicLayer.CostReduction) {
        @Override
        public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.hasColor(MagicColor.White) && source.isFriend(card)) {
                return cost.reduce(1);
            } else {
                return cost;
            }
        }
    },
    new MagicStatic(MagicLayer.CostReduction) {
        @Override
        public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.hasColor(MagicColor.Blue) && source.isFriend(card)) {
                return cost.reduce(1);
            } else {
                return cost;
            }
        }
    },
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
