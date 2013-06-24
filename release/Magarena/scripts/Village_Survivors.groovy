[
    new MagicStatic(
            MagicLayer.Ability,
            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.getController().getLife() <= 5) {
                flags.add(MagicAbility.Vigilance);
            }
        }
    }
]
