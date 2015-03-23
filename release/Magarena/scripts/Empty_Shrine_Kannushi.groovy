[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            for (final MagicColor color : MagicColor.values()) {
                if (permanent.getController().controlsPermanent(color)) {
                    permanent.addAbility(color.getProtectionAbility(), flags);
                }
            }
        }
    }
]
