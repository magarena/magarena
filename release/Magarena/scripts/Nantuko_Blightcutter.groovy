[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amt = permanent.getOpponent().getNrOfPermanents(BLACK_PERMANENT);
            pt.add(amt,amt);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.THRESHOLD_CONDITION.accept(target);
        }
    }
]
