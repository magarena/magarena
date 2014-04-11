[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            if (permanent.isEnchanted()) {
                pt.add(
                    2 * permanent.getAuraPermanents().size(),
                    2 * permanent.getAuraPermanents().size()
                );
            }
        }
    }
]
