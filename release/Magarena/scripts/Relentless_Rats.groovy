[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicGame game = source.getGame();
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicTargetFilter.NameTargetFilter(permanent.getName());
            final int size = game.filterPermanents(game.getPlayer(0),targetFilter).size() - 1;
            pt.add(size,size);
        }
    }
]
