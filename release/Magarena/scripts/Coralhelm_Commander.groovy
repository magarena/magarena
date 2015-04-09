[
    new MagicStatic(
        MagicLayer.ModPT,
        multiple("merfolk creatures you control")
    ) {

        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }

        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getCounters(MagicCounterType.Level) >= 4 && source != target;
        }
    },
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int level = permanent.getCounters(MagicCounterType.Level);
            if (level >= 4) {
                pt.set(4,4);
            } else if (level >= 2) {
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
            final int level = permanent.getCounters(MagicCounterType.Level);
            if (level >= 2) {
                permanent.addAbility(MagicAbility.Flying, flags);
            }
        }
    }
]
