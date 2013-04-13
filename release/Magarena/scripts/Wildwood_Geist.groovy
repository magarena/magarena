[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final MagicGame game = source.getGame();
            if (game.hasTurn(permanent.getController())) {
                pt.add(2,2);
            }
        }
    }
]
