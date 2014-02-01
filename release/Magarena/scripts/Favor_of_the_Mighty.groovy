[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_CREATURE
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            final Collection<MagicPermanent> creatures = source.getGame().filterPermanents(MagicTargetFilter.TARGET_CREATURE);
            int cmc = permanent.getConvertedCost();
            for (final MagicPermanent creature : creatures) {
                if (creature.getConvertedCost() > cmc) {
                    cmc = creature.getConvertedCost();
                    break;
                }
            }
            if (permanent.getConvertedCost() == cmc) {
                permanent.addAbility(MagicAbility.ProtectionFromAllColors);
            }
        }
    }
]
