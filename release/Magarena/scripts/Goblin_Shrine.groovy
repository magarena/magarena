[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilterFactory.GOBLIN_CREATURE
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1, 0);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getEnchantedPermanent().hasSubType(MagicSubType.Mountain);
        }
    }
]
