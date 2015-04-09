[
    new MagicStatic(
        MagicLayer.ModPT,
        CREATURE
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            int X = 0 - permanent.getController().getHandSize();
            pt.add(X,0);
        }
    }
]
