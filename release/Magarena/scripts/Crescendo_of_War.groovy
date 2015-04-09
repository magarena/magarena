[
    new MagicStatic(
        MagicLayer.ModPT,
        CREATURE
    ) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int amount = source.getCounters(MagicCounterType.Strife);
            pt.add(amount,0);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return target.isAttacking() || (target.isBlocking() && source.isFriend(target));
        }
    }
]
