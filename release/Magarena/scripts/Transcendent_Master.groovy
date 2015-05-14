[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int level = permanent.getCounters(MagicCounterType.Level);
            if (level >= 12) {
                pt.set(9,9);
            } else if (level >= 6) {
                pt.set(6,6);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            final int level = permanent.getCounters(MagicCounterType.Level);
            if (level >= 12) {
                permanent.addAbility(MagicAbility.Indestructible, flags);
            }
            if (level >= 6) {
                permanent.addAbility(MagicAbility.Lifelink, flags);
            }
        }
    }
]
