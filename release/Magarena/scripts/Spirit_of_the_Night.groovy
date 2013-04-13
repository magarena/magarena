[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            if (permanent.isAttacking()) {
                flags.add(MagicAbility.FirstStrike);
            }
        }
    }
]
