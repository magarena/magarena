[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_MERFOLK_YOU_CONTROL
    ) {

        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }

        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getCounters(MagicCounterType.Charge) >= 4 && source != target;
        }
    },
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 4) {
                pt.set(4,4);
            } else if (charges >= 2) {
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
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 2) {
                flags.add(MagicAbility.Flying);
            }
        }
    }
]
