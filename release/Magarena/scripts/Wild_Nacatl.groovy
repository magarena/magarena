[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicPlayer you = permanent.getController();
            if (you.controlsPermanent(MagicSubType.Mountain)) {
                pt.add(1,1);
            }
            if (you.controlsPermanent(MagicSubType.Plains)) {
                pt.add(1,1);
            }
        }
    }
]
