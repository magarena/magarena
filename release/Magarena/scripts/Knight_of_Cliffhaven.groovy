[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 4) {
                pt.set(4,4);
            } else if (charges >= 1) {
                pt.set(2,3);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 4) {
                flags.add(MagicAbility.Vigilance);
            }
            if (charges >= 1) {
                flags.add(MagicAbility.Flying);
            }
        }
    }
]
