[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (source.getController().controlsPermanent(MagicTargetFilter.TARGET_WHITE_CREATURE_YOU_CONTROL)) {
                pt.add(1,1);
            }
        }
    },

    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (source.getController().controlsPermanent(MagicTargetFilter.TARGET_BLUE_CREATURE_YOU_CONTROL)) {
                pt.add(1,1);
            }
        }
    }
]
