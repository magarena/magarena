[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicGame game = source.getGame();
            final int size =
                game.filterCards(player,MagicTargetFilterFactory.CREATURE_CARD_FROM_OPPONENT_GRAVEYARDS).size();         
            pt.add(size,size);
        }
    }
]
