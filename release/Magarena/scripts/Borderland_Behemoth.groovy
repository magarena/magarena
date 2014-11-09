def GIANT_YOU_CONTROL = MagicTargetFilterFactory.singlePermanent("Giant you control");

[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = 4 * source.getController().getNrOfPermanents(new MagicOtherPermanentTargetFilter(GIANT_YOU_CONTROL, source))
            pt.add(amount,amount);
        }
    }
]
