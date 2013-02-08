[
    new MagicStatic(
        MagicLayer.Ability, 
        MagicTargetFilter.TARGET_CREATURE) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Haste);
        }
    }
]
