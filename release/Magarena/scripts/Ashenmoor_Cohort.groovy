[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicGame game = source.getGame();
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(permanent.getController(),MagicTargetFilter.TARGET_BLACK_CREATURE_YOU_CONTROL);
            if (targets.size() > 1) {
                pt.add(1,1);
            }        
        }
    }
]
