[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicGame game = source.getGame();
            final int size = game.filterPermanents(permanent.getController(),MagicTargetFilter.TARGET_ENCHANTMENT).size();
            pt.add(size,size);
        }
    }
]
