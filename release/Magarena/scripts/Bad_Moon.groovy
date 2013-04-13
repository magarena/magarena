[
    new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_BLACK_CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
    }
]
