[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
            final MagicPermanent source,
            final MagicPermanent permanent,
            final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Trample);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getCounters(MagicCounterType.PlusOne) > 0;
        }
    }
]
