[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amt = permanent.getAuraPermanents().size() * 2;
            pt.add(amt, amt);
        }
    }
]
