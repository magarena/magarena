[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicGame game = source.getGame();
            if (game.hasTurn(permanent.getController())) {
                pt.add(2,0);
            } else {
                pt.add(0,2);
            }
        }
    }
]
