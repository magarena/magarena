[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount = permanent.getOpponent().getNrOfPermanents(MagicSubType.Plains);
            pt.add(amount,amount);
        }
    }
]
