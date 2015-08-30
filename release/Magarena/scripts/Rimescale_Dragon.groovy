def CREATURE_WITH_ICE_COUNTER = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasCounters(MagicCounterType.Ice) && target.isCreature();
    }
}

[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            CREATURE_WITH_ICE_COUNTER.filter(source) each {
                it.addAbility(MagicAbility.DoesNotUntap);
            }
        }
    }
]
