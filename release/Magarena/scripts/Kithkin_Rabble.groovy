[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int size = player.getNrOfPermanents(MagicTargetFilter.TARGET_WHITE_PERMANENT_YOU_CONTROL);
            pt.set(size, size);
        }
    }
]
