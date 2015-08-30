[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int level = permanent.getCounters(MagicCounterType.Level);
            if (level >= 4) {
                pt.set(3,10);
            } else if (level >= 1) {
                pt.set(2,6);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            if (permanent.getCounters(MagicCounterType.Level) >= 1) {
                permanent.addAbility(MagicAbility.Vigilance, flags);
            }
        }
    }
]
