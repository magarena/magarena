def CREATURE_AT_LEAST_3_LEVEL_COUNTERS = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isCreature() && 
               target.getCounters(MagicCounterType.Level) >= 3;
    }
};

[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(3,3);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getController().controlsPermanent(CREATURE_AT_LEAST_3_LEVEL_COUNTERS);
        }
    }
]
