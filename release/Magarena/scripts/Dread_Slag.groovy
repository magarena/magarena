[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = source.getController().getHandSize() * 4;
            pt.add(-amount,-amount);
        }
    }
]
