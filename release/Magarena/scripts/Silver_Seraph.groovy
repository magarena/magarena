[
    new MagicStatic(
        MagicLayer.ModPT,
        CREATURE_YOU_CONTROL
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(2, 2);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target && MagicCondition.THRESHOLD_CONDITION.accept(source);
        }
    }
]
