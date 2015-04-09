[
    new MagicStatic(
        MagicLayer.ModPT,
        CREATURE) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(
                final MagicGame game,
                final MagicPermanent source,
                final MagicPermanent target) {
            return source.getEquippedCreature().hasSubType(MagicSubType.Human) &&
                   target == source.getEquippedCreature();
        }
    }
]
