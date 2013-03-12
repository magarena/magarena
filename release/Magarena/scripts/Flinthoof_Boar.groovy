[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (source.getController().getNrOfPermanentsWithSubType(MagicSubType.Mountain) > 0) {
                pt.add(1,1);
            }
        }
    }
]
