[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_DRAGON_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(3,3);
        }
    }
]
