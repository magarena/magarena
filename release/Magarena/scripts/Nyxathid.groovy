[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicPlayer opponent = permanent.getChosenPlayer();
            final int amount = opponent.getHandSize();
            pt.add(-amount,-amount);
        }
    }
]
