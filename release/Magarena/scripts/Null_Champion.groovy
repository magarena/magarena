[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int level = permanent.getCounters(MagicCounterType.Level);
            if (level >= 4) {
                pt.set(7,3);
            } else if (level >= 1) {
                pt.set(4,2);
            }
        }
    }
]
