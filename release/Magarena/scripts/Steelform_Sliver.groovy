[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_SLIVER_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(0,1);
        }
    }
]