[
    new MagicStatic(MagicLayer.Ability, NONBASIC_LAND)  {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.loseAllAbilities();
            permanent.addAbility(MagicTapManaActivation.Red);
        }
    },
    new MagicStatic(MagicLayer.Type, NONBASIC_LAND)  {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.clear();
            flags.add(MagicSubType.Mountain);
        }
    }
]
