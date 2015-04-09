[
    new MagicStatic(
        MagicLayer.ModPT,
        GOBLIN_CREATURE
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1, 0);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            final MagicPermanent enchanted = source.getEnchantedPermanent();
            return enchanted.hasType(MagicType.Basic) &&
                   enchanted.hasSubType(MagicSubType.Mountain);
        }
    }
]
