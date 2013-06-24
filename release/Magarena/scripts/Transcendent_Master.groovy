[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 12) {
                pt.set(9,9);
            } else if (charges >= 6) {
                pt.set(6,6);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 12) {
                flags.add(MagicAbility.Indestructible);
            }
            if (charges >= 6) {
                flags.add(MagicAbility.Lifelink);
            }
        }
    }
]
