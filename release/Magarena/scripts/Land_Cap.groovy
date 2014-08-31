[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            if (permanent.getCounters(MagicCounterType.Depletion) >= 1) {
                permanent.addAbility(MagicAbility.DoesNotUntap, flags);
            }
        }
    }
]
