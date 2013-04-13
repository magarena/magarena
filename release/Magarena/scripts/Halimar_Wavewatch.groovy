[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 5) {
                pt.set(6,6);
            } else if (charges >= 1) {
                pt.set(0,6);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            if (permanent.getCounters(MagicCounterType.Charge) >= 5) {
                flags.add(MagicAbility.Islandwalk);
            }
        }
    }
]
