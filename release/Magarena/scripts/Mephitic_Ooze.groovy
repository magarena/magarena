[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = source.getController().getNrOfPermanents(MagicType.Artifact);
            pt.add(amount,0);
        }
    }
]
