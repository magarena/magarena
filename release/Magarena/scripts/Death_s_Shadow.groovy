[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amt = permanent.getController().getLife();
            pt.add(-amt, -amt);
        }
    }
]
