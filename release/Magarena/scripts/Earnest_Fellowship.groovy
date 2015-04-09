[
    new MagicStatic(
        MagicLayer.Ability,
        CREATURE
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent target, final Set<MagicAbility> flags) {
            for (final MagicColor color : MagicColor.values()) {
                if (target.hasColor(color)) {
                    target.addAbility(color.getProtectionAbility(), flags);
                }
            }
        }
    }
]
