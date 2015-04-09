[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = 4 * source.getController().getNrOfPermanents(GIANT_YOU_CONTROL.except(source))
            pt.add(amount,amount);
        }
    }
]
