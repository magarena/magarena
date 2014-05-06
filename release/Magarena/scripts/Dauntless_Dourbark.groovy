[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int forest = game.filterPermanents(player,MagicTargetFilterFactory.FOREST_YOU_CONTROL).size();
            final int treefolk = game.filterPermanents(player,MagicTargetFilterFactory.TREEFOLK_YOU_CONTROL).size();
            pt.set(forest+treefolk, forest+treefolk);
        }
    }
]
