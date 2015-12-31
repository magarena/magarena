[
    new MagicStatic(MagicLayer.CostIncrease) {
        @Override
        public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if (card.isArtifact()) {
                final int amt = card.getController().getNrOfPermanents(MagicType.Artifact);
                return cost.increase(amt);
            } else {
                return cost;
            }
        }
    }
]
