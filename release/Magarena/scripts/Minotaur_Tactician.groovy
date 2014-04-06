[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (source.getController().controlsPermanent(MagicTargetFilterFactory.WHITE_CREATURE_YOU_CONTROL)) {
                pt.add(1,1);
            }
        }
    },

    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (source.getController().controlsPermanent(MagicTargetFilterFactory.BLUE_CREATURE_YOU_CONTROL)) {
                pt.add(1,1);
            }
        }
    }
]
