[
    new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_CREATURE_WITH_FLYING_YOU_CONTROL) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            pt.add(1,1);
        }
    }
]
