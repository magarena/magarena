[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_CREATURE
    ) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int amount = source.getCounters(MagicCounterType.Charge);
            pt.add(amount,0);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return target.isAttacking() || (target.isBlocking() && source.isFriend(target));
        }
    }
]
