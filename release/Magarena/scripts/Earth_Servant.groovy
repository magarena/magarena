[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicGame game = source.getGame();
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(permanent.getController(),MOUNTAIN_YOU_CONTROL);
            pt.add(0,targets.size());
        }
    }
]
