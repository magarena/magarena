[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int size = game.filterPermanents(player,MagicTargetFilterFactory.TARGET_FOREST_YOU_CONTROL).size();
            pt.set(2, size);
        }
    }
]
