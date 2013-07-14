[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount = permanent.getOpponent().getNrOfPermanents(MagicType.Creature);
            pt.add(amount,amount);
        }
    }
]
