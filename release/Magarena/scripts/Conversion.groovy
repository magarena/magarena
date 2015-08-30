[
    new MagicStatic(MagicLayer.Ability, MOUNTAIN)  {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.loseAllAbilities();
            permanent.addAbility(MagicTapManaActivation.White);
        }
    },
    new MagicStatic(MagicLayer.Type, MOUNTAIN)  {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.clear();
            flags.add(MagicSubType.Plains);
        }
    }
]
