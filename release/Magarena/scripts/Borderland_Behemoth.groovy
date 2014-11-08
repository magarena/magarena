[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = 4 * source.getGame().getNrOfPermanents(MagicSubType.Giant) - 1;
            if (amount > 0) {
            pt.add(amount,amount);
            }
        }
    }
]
