[
    new MagicStatic(MagicLayer.CostReduction) {
        @Override
        public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.hasColor(MagicColor.White) && card.hasType(MagicType.Creature) && source.isFriend(card)) {
                return cost.reduce(1);
            } else {
                return cost;
            }
        }
    }
]
