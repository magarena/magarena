[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilterFactory.TARGET_CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(2,2);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target == source.getEnchantedPermanent() &&
                   source.getController().getOpponent().getNrOfPermanents(MagicTargetFilterFactory.TARGET_RED_PERMANENT) >= 1;
        }
    }
]
