[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicNameTargetFilter(
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                source.getName()
            );
            final int size = source.getGame().filterPermanents(
                source.getController(),
                new MagicOtherPermanentTargetFilter(targetFilter,source)
            ).size();
            pt.add(size,size);
        }
    }
]
