[
    new MagicStatic(MagicLayer.CostIncrease) {
        @Override
        public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.isArtifact()) {
                final int amt = card.getController().getNrOfPermanents(MagicType.Artifact);
                if (amt > 0) {
                    return cost.increase(MagicCostManaType.Generic, amt);
                }
            }
            return cost;
        }
    }
]
