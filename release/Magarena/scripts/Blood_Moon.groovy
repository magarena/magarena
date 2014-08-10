[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilterFactory.NONBASIC_LAND
    )  {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.loseAllAbilities();
            permanent.addAbility(new MagicTapManaActivation(MagicManaType.getList("{R}")));
        }
    },
    new MagicStatic(
        MagicLayer.Type,
        MagicTargetFilterFactory.NONBASIC_LAND
    )  {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
          flags.clear();
          flags.add(MagicSubType.Mountain);
        }
    }
]
