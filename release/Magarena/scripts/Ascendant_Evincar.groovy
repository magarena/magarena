[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_BLACK_CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    },
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_NONBLACK_CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(-1,-1);
        }
    }
]
