[
    new MagicStatic(MagicLayer.CostIncrease) {
        @Override
        public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            return card.isCreature() && source.isFriend(card) ? cost.increase(1) : cost;
        }
    }
]
