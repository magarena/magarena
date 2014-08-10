[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilterFactory.single("Mountain")
    )  {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.loseAllAbilities();
            permanent.addAbility(new MagicTapManaActivation(MagicManaType.getList("{W}")));
        }
    },
    new MagicStatic(
        MagicLayer.Type,
        MagicTargetFilterFactory.single("Mountain")
    )  {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
          flags.clear();
          flags.add(MagicSubType.Plains);
        }
    }
]
