[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(-2,-2);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            final int c = game.getNrOfPermanents(MagicColor.Red);
            return c >= game.getNrOfPermanents(MagicColor.White) &&
                   c >= game.getNrOfPermanents(MagicColor.Blue) &&
                   c >= game.getNrOfPermanents(MagicColor.Black) &&
                   c >= game.getNrOfPermanents(MagicColor.Red) &&
                   c >= game.getNrOfPermanents(MagicColor.Green);
        }
    }
]
