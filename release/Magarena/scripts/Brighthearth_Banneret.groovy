[
    new MagicStatic(MagicLayer.CostReduction) {
        @Override
        public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if ((card.hasSubType(MagicSubType.Elemental) || card.hasSubType(MagicSubType.Warrior)) && source.isFriend(card)) {
                return cost.reduce(MagicCostManaType.Generic, 1);
            } else {
                return cost;
            }
        }
    }
]
