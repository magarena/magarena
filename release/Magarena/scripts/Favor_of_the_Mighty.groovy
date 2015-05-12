[
    new MagicStatic(MagicLayer.Ability, CREATURE) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            final Collection<MagicPermanent> creatures = CREATURE.filter(source.getGame());
            int cmc = permanent.getConvertedCost();
            for (final MagicPermanent creature : creatures) {
                if (creature.getConvertedCost() > cmc) {
                    cmc = creature.getConvertedCost();
                    break;
                }
            }
            if (permanent.getConvertedCost() == cmc) {
                permanent.addAbility(MagicAbility.ProtectionFromAllColors, flags);
            }
        }
    }
]
