[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicTargetFilter.NameTargetFilter(permanent.getName());
            final int size = game.filterPermanents(player,new MagicOtherPermanentTargetFilter(targetFilter,permanent)).size();
            pt.add(size,size);
        }
    }
]
