[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int level = permanent.getCounters(MagicCounterType.Level);
            if (level >= 5) {
                pt.set(4,8);
            } else if (level >= 2) {
                pt.set(3,6);
            }
        }
    },
    new MagicStatic(MagicLayer.ModPT, CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int level = source.getCounters(MagicCounterType.Level);
            int amount = 0;
            if (level >= 5) {
                amount = 2;
            } else if (level >= 2) {
                amount = 1;
            }
            pt.add(amount, amount);
        }

        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    }
]
