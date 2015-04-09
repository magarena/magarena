[
    new MagicStatic(
            MagicLayer.Ability,
            CREATURE_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.getController().getLife() <= 5) {
                permanent.addAbility(MagicAbility.Vigilance, flags);
            }
        }
    }
]
