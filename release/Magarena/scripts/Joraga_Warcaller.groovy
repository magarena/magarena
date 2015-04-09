[
    new MagicStatic(
        MagicLayer.ModPT,
        ELF_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount = source.getCounters(MagicCounterType.PlusOne);
            pt.add(amount, amount);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    }
]
