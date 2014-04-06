[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilterFactory.CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (permanent.isUntapped() && !permanent.isAttacking()) {
                pt.add(0,2);
            } 
        }
    }
]
