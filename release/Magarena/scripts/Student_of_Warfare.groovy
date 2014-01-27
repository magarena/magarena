[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int level=permanent.getCounters(MagicCounterType.Level);
            if (level>=7) {
                pt.set(4,4);
            } else if (level>=2) {
                pt.set(3,3);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            final int level=permanent.getCounters(MagicCounterType.Level);
            if (level>=7) {
                flags.add(MagicAbility.DoubleStrike);
            } else if (level>=2) {
                flags.add(MagicAbility.FirstStrike);
            }
        }
    }
]
