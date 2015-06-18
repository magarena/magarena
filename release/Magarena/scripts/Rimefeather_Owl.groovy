def PERMANENT_WITH_ICE_COUNTER = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasCounters(MagicCounterType.Ice);
    }
}

[
    new MagicStatic(MagicLayer.Type, PERMANENT_WITH_ICE_COUNTER) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Snow.getMask();
        }
    }
]
