[
    new MagicStatic(
        MagicLayer.ModPT,
        CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(2,2);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target == source.getEnchantedPermanent() &&
                   source.getOpponent().getNrOfPermanents(BLACK_PERMANENT) >= 1;
        }
    }
]
