def EXCEPT_BY_BLACK = MagicAbility.getAbility("can't be blocked except by black creatures");

[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int level = permanent.getCounters(MagicCounterType.Level);
            if (level >= 3) {
                pt.set(5,5);
            } else if (level >= 1) {
                pt.set(3,3);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            if (permanent.getCounters(MagicCounterType.Level) >= 3) {
                permanent.addAbility(EXCEPT_BY_BLACK, flags);
            }
        }
    }
]
