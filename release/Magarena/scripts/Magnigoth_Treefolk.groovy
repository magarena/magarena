[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            for (final MagicSubType landType : MagicSubType.ALL_BASIC_LANDS) {
                if (permanent.getController().controlsPermanent(landType)) {
                    permanent.addAbility(landType.getLandwalkAbility())
                }
            }
        }
    }
]
