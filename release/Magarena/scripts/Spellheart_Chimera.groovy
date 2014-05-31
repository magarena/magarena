[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int size = game.filterCards(player,MagicTargetFilterFactory.INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD).size();
            pt.set(size, 3);
        }
    }
]
